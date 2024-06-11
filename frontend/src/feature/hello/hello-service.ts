import { token } from "../../auth"
import { set } from "../../model"
import { Hello } from "./hello"


export async function loadHello() {
    const headers = {
        Authorization: `Bearer ${token()}`,
        Accept: "application/json"
    }
    const response = await fetch("./api/hello", {headers})
    if (response.ok) {
        const greeting: Hello = await response.json()
        console.log("greeting loaded:", greeting)
        set(model => model.hello = greeting)
    } else {
        console.warn("failed to load hello", response)
    }
}
