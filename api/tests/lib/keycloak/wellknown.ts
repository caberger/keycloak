import { AuthServerConfig } from "lib/configuration"

interface Wellknown {
    issuer: string,
    authorization_endpoint: string,
    token_endpoint: string
}
async function loadWellknown(authServer: AuthServerConfig) {
    let wellknown: Wellknown
    const url = new URL(`${authServer.authUrl}/realms/${authServer.realm}/.well-known/openid-configuration`)
    try {
        const response = await fetch(url)
        if (!response.ok) {
            console.error(`failed to fetch wellknown status: ${response.status}. text: ${response.statusText}`, response)
        }
        wellknown = await response.json()
    } catch(error) {
        console.error("exception fetching welcome", error)
    }
    return wellknown
}
export { Wellknown, loadWellknown }
