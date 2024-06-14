import { headers } from "../../auth"
import { set } from "../../model"
import { Post } from "./post"


export async function loadAllPosts() {
    const response = await fetch("./api/posts", {headers: headers()})
    if (response.ok) {
        const posts: Post[] = await response.json()
        set(model => model.posts = posts)
    } else {
        console.warn("failed to load hello", response)
    }
}
