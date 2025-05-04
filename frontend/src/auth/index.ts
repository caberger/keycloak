import { token } from "./keycloak"
export { checkIfUserIsAuthenticated, login, logout } from "./keycloak"
export { headers, token }

function headers() {
    const jwt = token()
    const headers = {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json"
    }
    if (!jwt) {
        delete headers.Authorization
    }
    return headers
}