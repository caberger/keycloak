import Keycloak from 'keycloak-js'
import { AUTHENTICATION_SETTINGS } from '../env'
import { set } from "../model"


const keycloak = new Keycloak(AUTHENTICATION_SETTINGS)    

/** check if the user is authenticated*/
async function checkIfUserIsAuthenticated() {
    const initOptions = {enableLogging: true}

    try {
        const authenticated = await keycloak.init(initOptions)
        if (authenticated) {
            loadProfile()
            console.log("token", keycloak.token)
            console.log("parsed", keycloak.tokenParsed)
        } else {
           set(model => delete model.token)
        }
        set(model => model.token = keycloak.token)

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
}
async function login() {
    await keycloak.login()
}
async function logout() {
    await keycloak.logout()
    set(model => delete model.token)
}

async function loadUserProfile() {
    const profile = await keycloak.loadUserProfile()
    const roles = keycloak.realmAccess.roles
    set(model => {
        model.user.firstName = profile.firstName 
        model.user.lastName = profile.lastName
        model.user.email = profile.email
        model.user.id = profile.id
        model.user.token = keycloak.token
        model.user.roles = roles
    })
}


export { checkIfUserIsAuthenticated, login, logout }
