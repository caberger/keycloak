import "@picocss/pico"
import "./css/styles.css"
import "./components/nav/login-button"
import "./components/content/content-component"
import { checkIfUserIsAuthenticated } from "./auth"
import { model } from "./model/store"
import { distinctUntilChanged, filter} from "rxjs"
import { loadPosts } from "./feature/post"
import { isLoggedIn } from "./model"
import _ from "lodash"

checkIfUserIsAuthenticated()

model
    .pipe(
        filter(model => isLoggedIn(model)),
        filter(model => model.posts.length == 0),
        distinctUntilChanged((prev, cur) => _.isEqual(prev, cur))
    )
    .subscribe(model => {
        loadPosts()
    })


