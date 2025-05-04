
import "./components/login-button"
import "./components/content"
import "./components/app"
import "./components/welcome"

import { checkIfUserIsAuthenticated } from "./auth"
import { model } from "./model/store"
import { distinctUntilChanged, filter} from "rxjs"
import { loadPosts } from "./feature/post"
import { isLoggedIn } from "./model"
import { equals } from "./lib"

async function start() {
    checkIfUserIsAuthenticated()

    model
        .pipe(
            filter(model => isLoggedIn(model)),
            filter(model => model.posts.length == 0),
            distinctUntilChanged(equals)
        )
        .subscribe(() => {
            loadPosts()
        })
}

start()


