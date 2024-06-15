import { headers } from "../../auth"
import { set } from "../../model"
import { Post } from "./post"


export async function loadPosts(publishedOnly: boolean) {
//    const published = publishedOnly ? "/published" : ""
    const url = publishedOnly ? "./api/posts/published" : "./api/posts"
    console.log("LoadLoades", publishedOnly, url)
    load(url)
}
async function load(url: string) {
    const response = await fetch(url, {headers: headers()})
    if (response.ok) {
        const posts: Post[] = await response.json()
        set(model => model.posts = posts)
    } else {
        console.warn("failed to load hello", response)
    }
}
