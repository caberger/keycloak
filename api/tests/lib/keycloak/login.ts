import { Configuration, Credentials } from "lib/configuration"
import { Wellknown } from "./wellknown"
import { OidcTokens } from "./oidc"

async function login(wellknown: Wellknown, configuration: Configuration) {
    const params = new URLSearchParams()
    params.set("client_secret", configuration.credentials.clientSecret)
    params.set("client_id", configuration.authServer.clientId)
    params.set("username", configuration.credentials.username)
    params.set("password", configuration.credentials.password)
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
    const tokens = await response.json() as OidcTokens
    return tokens
}

export { login }