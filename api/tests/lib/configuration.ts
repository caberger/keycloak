import { parseArgs } from "node:util"

interface AuthServerConfig {
    authUrl: string,
    realm: string,
    clientId: string
}
interface Credentials {
    username: string,
    password: string,
    clientSecret: string,
    realm: string
}
 interface Configuration {
    authServer: AuthServerConfig,
    credentials: Credentials
 }

function loadConfiguration() {
    const args = process.argv.slice(2)
    const options = {
        username: {
            type: "string"
        },
        password: {
            type: "string"
        },
        realm: {
            type: "string"
        },
        "client-id": {
            type: "string"
        },
        "auth-url": {
            type: "string"
        },
        "client-secret": {
            type: "string",
            default: ""
        }
    } as const

    const {
        values
    } = parseArgs({ args, options })
    const username = values.username
    const password = values.password
    const authUrl = values["auth-url"]
    const realm = values.realm
    const clientId = values["client-id"]
    const clientSecret = values["client-secret"]
    let missing = ""
    if (!username) {
        missing = "username"
    }
    if (!password) {
        missing = "password"
    }
    if (!authUrl) {
        missing = "auth-url"
    }
    if (!clientSecret) {
    }
    if (!realm) {
        missing = "realm"
    }
    if (!clientId) {
        missing = "client-id"
    }
    if (missing) {
        console.error(
            `you must call test test like this:
                npm test -- --username <username> --password <password> --auth-url <https://...keycloak> --realm <your realm> --client-id <Client ID> [--client-secret <client-secret>]
            `)
        console.log("your values", values)
        console.error(`parameter "${missing}" missing`)
        throw new Error("invalid command line args")
    }
    const authConfig: AuthServerConfig = {
        authUrl,
        realm,
        clientId
    }
    const config: Configuration = {
        authServer: authConfig,
        credentials: {
            username,
            password,
            clientSecret,
            realm
        }
    }
    return config
}

export { Configuration, Credentials, loadConfiguration, AuthServerConfig }