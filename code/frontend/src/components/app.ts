import { distinctUntilChanged, map } from "rxjs"
import { html } from "../lib"
import { isLoggedIn, model } from "../model"

class ApplicationComponent extends HTMLElement {
    async connectedCallback() {
        this.innerHTML = await html("app-component")
        const loggedOutPane = this.querySelector("welcome-component") as HTMLElement
        const loggedInPane = this.querySelector("content-component") as HTMLElement
        model
            .pipe(
                map(model => isLoggedIn(model)),
                distinctUntilChanged()
            )    
            .subscribe(loggedIn => {
                loggedOutPane.hidden = loggedIn
                loggedInPane.hidden = !loggedIn
            })
    }
}
customElements.define("app-component", ApplicationComponent)
