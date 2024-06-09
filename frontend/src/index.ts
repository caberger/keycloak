import "@picocss/pico"
import "./css/styles.css"
import '@webcomponents/custom-elements'
import "./components/nav/navbar"
import "./components/content/content-component"
import { checkIfUserIsAuthenticated } from "./auth"

checkIfUserIsAuthenticated()
