

import { loadWellknown, OidcTokens } from "lib/keycloak"
import { it, describe, assert, Wellknown } from "./lib"
import { Configuration, loadConfiguration } from "./lib/configuration"
import { beforeEach } from "node:test"
import { login } from "lib/keycloak/login"

const BEARER = "Bearer"

describe("wellknown", () => {
    let configuration: Configuration
    let wellknown: Wellknown
    let tokens: OidcTokens

    beforeEach( async () => {
        configuration = loadConfiguration()
        wellknown = await loadWellknown(configuration.authServer)
        assert(wellknown, "must have wellknown")
    })
     
    it("should have a valid wellknown", async () => {
        assert.equal(wellknown.issuer, `${configuration.authServer.authUrl}/realms/${configuration.authServer.realm}`)
    })
    it("should be able to login", async () => {
        const tokens = await login(wellknown, configuration)
        assert(tokens, "login should return tokens")
        if (tokens.token_type != BEARER) {
            console.error("invalid tokens:", tokens)
        }
        assert.equal(tokens.token_type, BEARER, "should be a Bearer token")
        const decoded = parseJwt(tokens.access_token)

        console.log("access token is", JSON.stringify(decoded, null, 4))
    })
})
function parseJwt(token: string) {
    return JSON.parse(Buffer.from(token.split('.')[1], 'base64').toString())
}

