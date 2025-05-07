import Keycloak, { KeycloakInitOptions } from "keycloak-js"
import { AuthenticationSettings } from "../env"
import { set } from "../model"


let settings: AuthenticationSettings
let keycloak: Keycloak
/*
const _ = setInterval(() => {
    if (keycloak && keycloak.authenticated) {
        keycloak.updateToken();
        console.log("token updated", keycloak.token)
    }
}, 30000)
*/

async function authenticationSettings() {
    const response = await fetch("./html/config.json")
    const config = await response.json() as AuthenticationSettings
    return config
}

/** check if the user is authenticated */
async function checkIfUserIsAuthenticated() {
    settings = await authenticationSettings()
    keycloak = new Keycloak(settings)
    const initOptions: KeycloakInitOptions = {
        onLoad: "check-sso",
        enableLogging: true
    }
    try {
        let authenticated = await keycloak.init(initOptions)

        if (authenticated) {
            set(model => model.token = keycloak.token)
            console.log("token is", keycloak.token)
            loadProfile()
        } else {
            set(model => delete model.token)
        }
        console.log("authenticated:", authenticated)
    } catch (error) {
        console.error('Failed to initialize adapter:', error)
    }
}
async function loadProfile() {
    const profile = await keycloak.loadUserProfile()
    set(model => {
        model.token = keycloak.token
        model.user.firstName = profile.firstName
        model.user.lastName = profile.lastName
        model.user.email = profile.email
        model.user.id = profile.id
        model.user.token = keycloak.token
        model.user.roles = keycloak.realmAccess.roles
    })
    console.log("profile loaded")
}
async function login() {
    await keycloak.login()
}
async function logout() {
    await keycloak.logout()
    keycloak.clearToken()
    set(model => delete model.token)
}
function token() {
    return keycloak.token
}
export { checkIfUserIsAuthenticated, login, logout, token, authenticationSettings }
