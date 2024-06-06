import { html, render } from "lit-html"
import { login, logout } from "../../auth/keycloak"
import { Model, model } from "../../model"

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
    const icon = model.token ? "logout" : "person"
    return html`
        <ul>
            <li><strong>HTL-Leonding</strong></li>
        </ul>
        <ul>
            <li><a href="#" title="Account"><span class="material-icons" @click=${() => onClick(model)}>${icon}</span></a></li>
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

