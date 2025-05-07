import { authenticationSettings } from "../auth/keycloak"
import { html } from "../lib"

class WelcomeComponent extends HTMLElement {
    async connectedCallback() {
        const settings = await authenticationSettings()
        console.log("settings:", settings)
        this.innerHTML = await html("welcome", settings)
    }
}
customElements.define("welcome-component", WelcomeComponent)