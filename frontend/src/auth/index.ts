import { token } from "./keycloak"
export { checkIfUserIsAuthenticated, login, logout } from "./keycloak"
export { headers, token }


function headers() {
    const headers = {
        Authorization: `Bearer ${token()}`,
        Accept: "application/json"
    }
    return headers
}