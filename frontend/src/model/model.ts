import type { User } from "./user"

import { Post } from "../feature/post"

interface Model {
    /** the json web token */
    token?: string
    user: User
    posts: Post[]
}
const isLoggedIn = (model: Model) => !!model.token

export type { Model }
export { isLoggedIn }