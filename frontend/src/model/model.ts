import { User } from "./user"
import { Hello } from "../feature/hello"

export interface Model {
    /** the json web token */
    token?: string
    
    user: User,
    hello: Hello
}
export function isLoggedIn(model: Model) {
    return !!model.token
}

export { Hello }