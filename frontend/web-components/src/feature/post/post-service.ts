import { headers } from "../../auth"
import { set } from "../../model"
import { Post } from "./post"

export async function loadPosts() {
    const url = "./api/users/posts"
    const response = await fetch(url, {headers: headers()})
    if (response.ok) {
        const posts: Post[] = await response.json()
        console.log("posts loaded", posts)
        set(model => model.posts = posts)
    } else {
        console.warn("failed to load hello", response)
    }
}
