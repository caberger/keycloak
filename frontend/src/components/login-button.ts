import { login, logout } from "../auth"
import { isLoggedIn, model } from "../model"

class LoginButtonElement extends HTMLElement {
    connectedCallback() {
        this.innerHTML = /*html*/`<a href="#"><span class="material-icons"></span></a>`
        console.log("login button connected")
        model.subscribe(model => {
            const {title, icon} = isLoggedIn(model) ? {title: "Logout", icon: "logout"} : {title: "Login", icon: "person"}
            const a = this.querySelector("a")
            a.title = title
            a.querySelector("span").innerText = icon
            a.onclick = isLoggedIn(model) ? logout : login    
        })
    }
}
customElements.define("login-button", LoginButtonElement)
