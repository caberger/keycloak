import { html, render } from "lit-html"
import { isLoggedIn, Model, model } from "../../model"
import { distinctUntilChanged } from "rxjs"
import { AUTHENTICATION_SETTINGS } from "../../env"
import { Post } from "../../feature/post"
import { isUserInRole } from "../../auth"
import _ from "lodash"

class ContentComponent extends HTMLElement {
    connectedCallback() {
        model
            .pipe(distinctUntilChanged((prev, cur) => _.isEqual(prev, cur)))    
            .subscribe(model => this.render(model))
    }
    render(model: Model) {
        console.log("render", model)
        render(template(model), this)
    }
}
customElements.define("app-component", ContentComponent)

function loggedOutTemplate() {
    return html`
        <h2>Welcome to the Demo</h2>
        <div class="container-fluid">
            <p>For more information please log in with a user from below.</p>
            <hr/>
            <article>
                <table>
                    <thead>
                        <tr>
                            <th>Username</th><th>Password</th><th>Description</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr><td>admin</td><td>password</td><td>system adminstrator</td></tr>
                        <tr><td>john.doe</td><td>password</td><td>customer allowed to read published posts</td></tr>
                        <tr><td>joe</td><td>password</td><td>editor of posts</td></tr>
                    </tbody>
                </table>
            </article>
            <p>The <a href=${AUTHENTICATION_SETTINGS.url}>authentication server</a>
                uses the realm <strong>${AUTHENTICATION_SETTINGS.realm}</strong> with client name <strong>${AUTHENTICATION_SETTINGS.clientId}</strong>.
                The admin user for keycloak is also <i>admin</i> with the password <i>password</i>.
            </p>
        </div>
`
}
function template(model: Model) {
    const user = model.user

    const all = loggedOutTemplate()
    const loggedIn = html`
        <h2>Welcome ${user.firstName} ${user.lastName}</h2>
        <p>${user.email}</p>
    `
    const isUserLoggedIn = isLoggedIn(model)
    const template = isUserLoggedIn ? loggedIn : all
    const rolesTmpl = isUserLoggedIn ? rolesTemplate(user.roles) : ""
    const postsTmpl = isUserLoggedIn ? postsTemplate(model.posts, isUserInRole(model, "editor")) : ""
    return html`
        <hgroup>
            ${template}
        </hgroup>
        ${rolesTmpl}
        ${postsTmpl}
        `
}
function rolesTemplate(roles: string[]) {
    const roleTemplates = roles.map(role => html`<div>${role}</div>`)
    return html`
        <hr/>
        <div class="container-fluid">
            <h3>Your Roles</h3>
            <div class="grid">
                ${roleTemplates}
            </div>
            <br/>
        </div>
    `
}

function postsTemplate(posts: Post[], isEditor: boolean) {
    const postsTemplate = posts.map(post => {
        return html`
            <tr>
                <td>${post.title}</td>
                <td class="shorten">${post.body}</td>
                <td>${post.published}</span></td>
            </tr>
        `
    })
    const text = isEditor ? "you see all posts, because you are an editor" : "you see the published posts, because you are logged in"
    return html`
    <style>
        .shorten {
            text-overflow: ellipsis
        }
    </style>
    <div class="container-fluid">
        <hgroup>
            <h3>Published Posts</h3>
            <div>(${text})</div>
        </hgroup>
        <table>
            <thead>
                <th>Title</th><th>Body</th><th>Published</th>
            </thead>
            <tbody>
                ${postsTemplate}
            </tbody>
        </table>
    </div>
    `
}
