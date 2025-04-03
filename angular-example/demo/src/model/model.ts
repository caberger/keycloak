
interface Post {
    id: number,
    title: string,
    body: string
    published: boolean
}

interface Model {
    readonly numberOfClicks: number,
    readonly jwt?: string,
    readonly title: string,
}
export { Model, Post }
