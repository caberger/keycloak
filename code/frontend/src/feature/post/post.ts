import { Category } from "./category"

interface Post {
    title: string,
    body: string
    published: boolean,
    category: Category
}

export type { Post }
