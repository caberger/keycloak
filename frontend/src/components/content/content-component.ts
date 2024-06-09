import { html, render } from "lit-html"
import { Model, isLoggedIn, model } from "../../model"
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
customElements.define("main-content", ContentComponent, {extends: "article"})

function template(model: Model) {
    console.log("model is", model)
    const user = model.user

    const all = html`
        <h2>Welcome to the Demo</h2>
        <p>For more information please log in</p>
    `   
    const loggedIn = html`
        <h2>Welcome ${user.firstName} ${user.lastName}</h2>
        <p>${user.email} - Please log out again.</p>
    `
    const isUserLoggedIn = isLoggedIn(model)
    const template = isUserLoggedIn ? loggedIn : all
    const rolesTmpl = isUserLoggedIn ? rolesTemplate(user.roles) : ""
    return html`
        <hgroup>
            ${template}
        </hgroup>
        ${rolesTmpl}
        `
}
function rolesTemplate(roles: string[]) {
    const roleTemplates = roles.map(role => html`<div>${role}</div>`)
    return html`
        <hr/>
        <div class="container-fluid">
            <h3>Roles</h3>
            <div class="grid">
                ${roleTemplates}
            </div>
        </div>
    `
}
