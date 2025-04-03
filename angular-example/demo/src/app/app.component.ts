import { Component, OnInit } from '@angular/core'
import { RouterOutlet } from '@angular/router'
import { store, set, Post } from "../model"
import { checkIfUserIsAuthenticated, login, logout, headers } from '../auth'

const POSTS_URL = "/api/posts"
@Component({
    selector: 'app-root',
    imports: [RouterOutlet],
    templateUrl: './app.component.html',
    styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
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
            const response = await fetch(POSTS_URL, { headers: headers() })
            const posts = await response.json()
            console.log("posts loaded", posts)
            set(model => model.posts = posts)
        }
    }
    onLogout() {
        logout()
    }
}
