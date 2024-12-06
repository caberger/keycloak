import "@picocss/pico"
import "./css/styles.css"
import '@webcomponents/custom-elements'
import "./components/nav/navbar"
import "./components/content/content-component"
import { checkIfUserIsAuthenticated, isUserInRole } from "./auth"
import { model } from "./model/store"
import { distinctUntilChanged, filter, map } from "rxjs"
import { loadHello } from "./feature/hello"
import { isLoggedIn } from "./model"
import { loadPosts } from "./feature/post"

checkIfUserIsAuthenticated()

model
    .pipe(
        filter(isLoggedIn),
        filter(model => model.posts.length == 0),
        distinctUntilChanged()
    )
    .subscribe(model => {
        const isEditor = !isUserInRole(model, "editor")
        loadHello()
        loadPosts(isEditor)
    })


