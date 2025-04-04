import { Component } from '@angular/core'
import { store } from "../model"
import { checkIfUserIsAuthenticated, login, logout } from '../auth'
import { loadAllPosts, Post } from '../feature'

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
        onLoad()
    }
    onLogout() {
        logout()
    }
}

async function onLoad() {
    const isLoggedIn = await checkIfUserIsAuthenticated()
    if (!isLoggedIn) {
        login()
    } else {
        loadAllPosts()
    }
}
