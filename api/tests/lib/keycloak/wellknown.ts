import { AuthServerConfig } from "lib/configuration"

interface Wellknown {
    issuer: string,
    authorization_endpoint: string,
    token_endpoint: string
}
async function loadWellknown(authServer: AuthServerConfig) {
    const url = new URL(`${authServer.authUrl}/realms/${authServer.realm}/.well-known/openid-configuration`)
    const response = await fetch(url)
    const wellknown: Wellknown = await response.json()
    return wellknown
}
export { Wellknown, loadWellknown }
