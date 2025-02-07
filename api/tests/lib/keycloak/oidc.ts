export interface OidcTokens {
    access_token: string,
    expires_in: number,
    refresh_token: string
    refresh_expires_in: number,
    token_type: string
    token_endpoint: string
}
