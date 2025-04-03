import { headers } from "../auth"
import { set } from "../model"

async function loadAllPosts() {
    const response = await fetch("/api/posts", { headers: headers() })
    const posts = await response.json()
    console.log("posts loaded", posts)
    set(model => { model.posts = posts })
}
export { loadAllPosts }
