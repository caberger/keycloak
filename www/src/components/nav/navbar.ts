import { html, render } from "lit-html"
import { login, logout } from "../../auth/keycloak"
import { Model, isLoggedIn, model } from "../../model"

class NavBarElement extends HTMLElement {
    connectedCallback() {
        model.subscribe(model => this.render(model))
    }
    render(model: Model) {
        render(template(model), this)
    }
}

customElements.define("nav-bar", NavBarElement, {extends: "nav"})

function template(model: Model) {
    let title, icon
    if (isLoggedIn(model)) {
        title = "Logout"
        icon = "logout"
    } else {
        title = "Login"
        icon = "person"

    }
    return html`
        <ul>
            <li><strong>HTL-Leonding</strong></li>
        </ul>
        <ul>
            <li><a href="#" title=${title}><span class="material-icons" @click=${() => onClick(model)}>${icon}</span></a></li>
        </ul>
    `
}
function onClick(model: Model) {
    if (!!model.token) {
        logout()
    } else {
        login()
    }
}

