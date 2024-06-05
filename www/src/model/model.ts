import { Credentials } from "./credentials"

export interface Model {
    credentials: Credentials

    /** the json web token */
    token: string
}
