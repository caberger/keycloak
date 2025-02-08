import { Configuration, Credentials } from "lib/configuration"
import { Wellknown } from "./wellknown"
import { OidcTokens } from "./oidc"

async function login(wellknown: Wellknown, configuration: Configuration) {
    const creds = configuration.credentials
    const params = new URLSearchParams()
    if (configuration.credentials.clientSecret) {
        params.set("client_secret", creds.clientSecret)
    }
    params.set("client_id", configuration.authServer.clientId)
    params.set("username", creds.username)
    params.set("password", creds.password)
    params.set("grant_type", "password")
    params.set("redirect_uri", "https://localhost:4200")

    const response = await fetch(wellknown.token_endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
            "Accept": "application/json"
        },
        body: params
    })
    let tokens
    if (!response.ok) {
        console.warn(`login failed with status ${response.status}. Reason: ${response.statusText}`)
        console.log("response is", response)
        if (response.headers.get("content-type") == "application/json") {
            const json = await response.json()
            console.log("response body:", json)
        }
    } else {
        tokens = await response.json() as OidcTokens
    }
    return tokens
}

export { login }