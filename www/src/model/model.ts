import { User } from "./user"

export interface Model {
    /** the json web token */
    token?: string
    user: User
}
export function isLoggedIn(model: Model) {
    return !!model.token
}