
import { distinctUntilChanged, map } from "rxjs"
import { headers, login, logout } from "../auth"
import { html } from "../lib"
import { isLoggedIn, model } from "../model"
import { loadPosts } from "../feature/post"

const XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"

class NavigationBarElement extends HTMLElement {
    async connectedCallback() {
        this.innerHTML = await html("navigation-bar")

        const downloadButton = this.button("download-button")
        downloadButton.onclick = () => window.open("/api/portation/xlsx")

        const uploadButton = this.button("upload-button")
        uploadButton.onclick = () => this.dialog.showModal()

        const input = this.uploadInput
        input.onchange = e => this.uploadImportFile(e)
        input.accept = XLSX_CONTENT_TYPE
        const loginButton = this.button('login-button')
        const buttonsThatAreOnlyVisibleWhenAuthorized = [uploadButton, downloadButton]

        model.
            pipe(
                map(model => isLoggedIn(model)),
                distinctUntilChanged()
            )
            .subscribe(isLoggedIn => {
                const { title, icon } = isLoggedIn ? { title: "Logout", icon: "logout" } : { title: "Login", icon: "person" }
                loginButton.title = title
                loginButton.innerText = icon
                loginButton.onclick = isLoggedIn ? logout : login

                buttonsThatAreOnlyVisibleWhenAuthorized.forEach(button => button.hidden = !isLoggedIn)
            })
    }
    async uploadImportFile(e: Event) {
        const input = e.target as HTMLInputElement
        if (input.files.length > 0) {
            const file = input.files[0]
            const yes = confirm(`do your really want to import ${file.name} ?`)
            if (yes) {
                const response = await fetch("/api/portation/xlsx", {
                    method: "POST",
                    headers: { ...headers(), "content-type": XLSX_CONTENT_TYPE },
                    body: file
                })
                if (response.ok) {
                    alert("import done")
                } else {
                    const error = await response.json()
                    console.error("upload failed", error)
                    var trace = JSON.stringify(error)
                    alert(`Upload failed: ${response.statusText}: ${trace}`)
                }
                loadPosts()
                this.dialog.close()
            }
        }
    }
    get dialog() {
        return this.querySelector("dialog")
    }
    get uploadInput() {
        return this.dialog.querySelector("input")
    }
    button(name: string) {
        return this.querySelector(`[name='${name}']`) as HTMLElement;
    }
    
}
customElements.define("navigation-bar", NavigationBarElement)

function getByName(this: HTMLElement | ShadowRoot, name: string) {
    return this.querySelector(`[name='${name}']`) as HTMLElement;
}
