import { equals, html } from "../lib"
import { isLoggedIn, Model, model, User } from "../model"

import { distinctUntilChanged, map } from "rxjs"
import { Post } from "../feature/post"

interface ViewModel {
    user: User
    roles: string[],
    posts: Post[]
}

class ContentComponent extends HTMLElement {
    async connectedCallback() {
        this.innerHTML = await html("content")
        model
            .pipe(
                map(toViewModel),
                distinctUntilChanged(equals)
            )
            .subscribe(render.bind(this))
    }
}
customElements.define("content-component", ContentComponent)

function toViewModel(model: Model) {
    const vm: ViewModel = {
        user: model.user,
        roles: model.user.roles,
        posts: model.posts
    }
    return vm
}
function render(this: ContentComponent, vm: ViewModel) {
    const nameElement = this.querySelector("hgroup h2 span") as HTMLElement
    nameElement.innerText = `${vm.user.firstName} ${vm.user.lastName}`
    const emailElement = this.querySelector("hgroup p") as HTMLParagraphElement
    emailElement.innerText = vm.user.email
    const tbody = this.querySelector("tbody")
    tbody.innerHTML = ""
    vm.posts.forEach(post => {
        var text = `<td>${post.title}</td><td>${post.body}</td><td>${post.published ? '👍' : '✋'}`
        var row = tbody.insertRow()
        row.innerHTML = text
        row.onclick = () => alert(`post ${post.title} selected`)
    })
    const rolesContainer = this.querySelector(".grid") as HTMLDivElement
    rolesContainer.innerHTML = ""
    vm.roles.forEach(role => {
        var div = document.createElement("div")
        div.innerText = role
        rolesContainer.appendChild(div)
    })
}
