import { html, render } from "lit-html"
import { Model, model } from "../../model"
import { distinctUntilChanged } from "rxjs"

class ContentComponent extends HTMLElement {
    connectedCallback() {
        model
            .pipe(distinctUntilChanged())    
            .subscribe(model => this.render(model))
    }
    render(model: Model) {
        render(template(model), this)
    }
}

function template(model: Model) {
    console.log("model is", model)

    const all = html`
        <div>Welcome to the Demo. For more information please log in</div>
    `   
    const loggedIn = html`
        <div>Welcome ${model.user.email}. Please log out again.</div>
    `
    const isLoggedIn = model.token?.length > 0
    const template = isLoggedIn ? loggedIn : all
    return html`
        <div class="centered">
            ${template}
        </div>
        `
}
customElements.define("main-content", ContentComponent, {extends: "article"})