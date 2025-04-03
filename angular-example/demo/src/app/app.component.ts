import { Component } from '@angular/core'
import { store, set, Post } from "../model"
import { checkIfUserIsAuthenticated, login, logout, headers } from '../auth'

const loadAllPosts = async () => {
    const response = await fetch("/api/posts", { headers: headers() })
    const posts = await response.json()
    console.log("posts loaded", posts)
    set(model => { model.posts = posts })
}

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html'
})
export class AppComponent {
    title?: string
    posts: Post[] = []

    async ngOnInit() {
        store.subscribe(model => {
            this.title = model.title
            this.posts = model.posts
        })
        const isLoggedIn = await checkIfUserIsAuthenticated()
        if (!isLoggedIn) {
            login()
        } else {
            loadAllPosts()
        }
    }
    onLogout() {
        logout()
    }
}
