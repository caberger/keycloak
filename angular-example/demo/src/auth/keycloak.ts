import Keycloak, { KeycloakInitOptions } from "keycloak-js"
interface AuthenticationSettings {
    url: string
    realm: string
    clientId: string
}
const AUTHENTICATION_SETTINGS: AuthenticationSettings = {
    url: "http://localhost:8000",
    realm: "demo",
    clientId: "frontend"
}

function headers() {
    const headers: HeadersInit = {
        Accept: "application/json"
    }
    const jwt = token()
    if (jwt) {
        headers["Authorization"] = `Bearer ${jwt}`
    }
    return headers
}

const keycloak = new Keycloak(AUTHENTICATION_SETTINGS)
/*
const _ = setInterval(() => {
    if (keycloak.authenticated) {
        keycloak.updateToken();
        console.log("token updated", keycloak.token)
    }
}, 30000)
*/

/** check if the user is authenticated */
async function checkIfUserIsAuthenticated() {
    let authenticated = false
    const initOptions: KeycloakInitOptions = {
        onLoad: "check-sso",
        enableLogging: true
    }
    try {
        authenticated = await keycloak.init(initOptions)
        if (authenticated) {
            console.log("token is", keycloak.token)
        }
    } catch (error) {
        console.error('Failed to initialize adapter:', error)
    }
    return authenticated
}
async function loadProfile() {
    const profile = await keycloak.loadUserProfile()
    return profile
}
async function login() {
    await keycloak.login()
}
async function logout() {
    await keycloak.logout()
    keycloak.clearToken()
}
function token() {
    return keycloak.token
}
export { checkIfUserIsAuthenticated, login, logout, token, headers }
