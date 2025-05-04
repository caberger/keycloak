/** Make settings from Webpack Define Plugin available in typescript */

interface AuthenticationSettings {
    url: string
    realm: string,
    clientId: string
}

export { AuthenticationSettings}
