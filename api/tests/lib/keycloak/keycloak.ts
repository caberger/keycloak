import { OidcTokens } from "./oidc"
import { Wellknown } from "./wellknown"

const IS_DEBUG=false

const REDIRECT_PARAM_NAME="aid"
const REDIRECT_STATUS = "11"
const REDIRECT_URI_NAME="REDIRECT_URI"
const REMEBER_ORIGINAL_URL_PARAM_NAME="original-url"
const SKEW = 5
const TIMEOUT_FOR_REDIRECT = IS_DEBUG ? 2000 : 1 // increase this to debug redirect loops

interface KeycloakInit {
    readonly url: string,
    readonly realm: string,
    readonly clientId: string
    readonly applicationServer: string
}

interface AuthInfo {
    uri: string
    token: string,
    clientToken: string
}

interface ErrorResponse {
    error: string,
    error_description: string
}
enum State {
    START,
    WELLKNOWN_LOADED,
    AUTH_INFO_LOADED,
    REDIRECT_TO_LOGIN,
    GET_TOKEN_FROM_CODE,
    AUTHENTICATION_CODE_RECEIVED,
    CODE_INVALID,
    DONE
}
type StateHandler = () => Promise<State>

let lastTokenGrantedAt: Date
let loginResponse: OidcTokens 
let wellknown: Wellknown
let authInfo: AuthInfo
let settings: KeycloakInit
let authenticationCode: string

/** the html is loaded, let us find out how our authentication state is and the process... 
*/
async function onload(init: KeycloakInit) {
    settings = init
    await process()
    function setupRefreshInterval() {
        if (isTokenValid()) {
            if (loginResponse && loginResponse.refresh_token) {
                let interval = loginResponse?.refresh_expires_in / 2
                if (IS_DEBUG) {
                    interval = 5
                }
                setInterval(async () => {
                    await refreshToken()
                }, interval * 1000)        
            }
        }   
    }
    setupRefreshInterval()
}

const handlers =  new Map<State, StateHandler>()
handlers.set(State.START, startHandler)
handlers.set(State.WELLKNOWN_LOADED, loadAuthInfo)
handlers.set(State.AUTH_INFO_LOADED, authInfoLoadedHandler)
handlers.set(State.GET_TOKEN_FROM_CODE, authenticationCodeReceivedHandler)
handlers.set(State.REDIRECT_TO_LOGIN, redirectToLogin)
handlers.set(State.CODE_INVALID, stateInvalidSoRedirectToOriginal)

async function process() {
    let state = State.START
    while(state != State.DONE) {
        const name = State[state]
        if (IS_DEBUG) {
            console.log("processing state", name)
        }
        const handler = handlers.get(state)
        if (handler) {
            state = await handler()
        } else {
            console.error("Unknow state entered", name)
            state = State.DONE
        }
    }
}
async function startHandler() {
    wellknown = await loadWellknown()
    return State.WELLKNOWN_LOADED
}
function delayedRedirectTo(href: string) {
    console.log(`redirect to ${href} in ${TIMEOUT_FOR_REDIRECT}s...`)
    setTimeout(() => window.location.href = href, TIMEOUT_FOR_REDIRECT)
}
/** let us find out why we are loaded again and let us find our state.
 * This function is called only once when the page is loaded.
*/
async function authInfoLoadedHandler() {
    const urlParams = new URLSearchParams(window.location.search)
    let code = urlParams.get("code")
    let nextState = State.REDIRECT_TO_LOGIN
    if (code) {
        authenticationCode = code
        nextState = State.GET_TOKEN_FROM_CODE
    }
    return nextState
}

function headers() {
    const headers = new Headers()
    headers.set("Content-Type", "application/x-www-form-urlencoded")
    headers.set("Accept", "application/x-www-form-urlencoded")
    return headers
}
/** user already has logged in successfully, now we handle that */
async function authenticationCodeReceivedHandler() {
    const redirect_uri = loadRedirectUriName()
    const client_id = settings.clientId 
    const client_secret = authInfo.clientToken
    const code = authenticationCode
    const body = new URLSearchParams({
        client_id,
        client_secret,
        code,
        "scope": "openid",
        "grant_type": "authorization_code",
        redirect_uri
    })
    lastTokenGrantedAt = new Date()
    const response = await fetch(wellknown.token_endpoint, {
        method: "POST",
        headers: headers(),
        body
    })
    let state = State.DONE
    if (response.ok) {
       loginResponse = await response.json()
       if (!IS_DEBUG) {
           localStorage.removeItem(REDIRECT_URI_NAME)
       }
    } else {
        const errorResponse: ErrorResponse = await response.json()
        console.error("Code was not ok, propably a reload: failed to get token:", response.statusText, "status: ", response.status, errorResponse)
        state = State.CODE_INVALID
    }
    return state
}
async function stateInvalidSoRedirectToOriginal() {
    const urlParams = new URLSearchParams(window.location.search)

    const originalUrl = urlParams.get(REMEBER_ORIGINAL_URL_PARAM_NAME)
    if (originalUrl) {
        delayedRedirectTo(originalUrl)
    }
    return State.DONE
}
async function loadAuthInfo() {
    const response = await fetch(`${settings.applicationServer}/auth`)
    authInfo = await response.json()
    return State.AUTH_INFO_LOADED
}
async function loadWellknown() {
    const url = `${settings.url}/.well-known/openid-configuration`
    const response = await fetch(url)
    const wellknown = await response.json()
    return wellknown
}
async function redirectToLogin() {
    const loc = encodeURIComponent(createAndStoreRedirectUri())
    const uri = `${wellknown.authorization_endpoint}?response_type=code&scope=openid&client_id=${settings.clientId}&redirect_uri=${loc}`
    console.log("no jwt, redirect to", uri)
    delayedRedirectTo(uri)
    return State.DONE
}

function createAndStoreRedirectUri() {
    const href = window.location.href
    const original = encodeURIComponent(href)
    const loc = `${href}?${REDIRECT_PARAM_NAME}=${REDIRECT_STATUS}&${REMEBER_ORIGINAL_URL_PARAM_NAME}=${original}`
    const urlParams = new URLSearchParams(window.location.search)    
    const authState = urlParams.get(REDIRECT_PARAM_NAME)
    if (!authState || authState != REDIRECT_STATUS) {
        localStorage.setItem(REDIRECT_URI_NAME, loc)
    } else {  // ...we are back from redirect and already used the code
        console.log("dont store", loc)
    }    
    return loc
}
function loadRedirectUriName() {
    return localStorage.getItem(REDIRECT_URI_NAME)
}

async function access_token() {
    const token = await isTokenValid() ? loginResponse?.access_token : undefined
    if (!token) {
        console.warn("TODO: refresh token", token)
    }
    return token
}
async function isTokenValid() {
    let valid = !!loginResponse
    if (valid) {
        const now = new Date()
        const tokenExpires = lastTokenGrantedAt.getTime() + (loginResponse.expires_in - SKEW) * 1000
        const maxValidUntilDate = new Date(tokenExpires)
        if (now >= maxValidUntilDate) {
            valid = false
        } else {
            const validUntiltime = new Date(tokenExpires)
            console.log("token valid. Generated at", lastTokenGrantedAt.toLocaleTimeString(), "expires", validUntiltime.toLocaleTimeString())
        }
    } else {
        console.log("token not valid")
    }
    return valid
}
async function refreshToken() {
    const client_id = settings.clientId 
    const client_secret = authInfo.clientToken
    const refresh_token = loginResponse.refresh_token
    const body = new URLSearchParams({
        client_id,
        client_secret,
        refresh_token,
        "grant_type": "refresh_token"
    })
    lastTokenGrantedAt = new Date()
    const response = await fetch(wellknown.token_endpoint, {
        method: "POST",
        headers: headers(),
        body
    })
    if (response.ok) {
        loginResponse = await response.json()
        console.log("token refreshed", loginResponse)
    } else {
        console.warn("failed to refresh access token")
    }
    return response.ok
}
export { onload, access_token }