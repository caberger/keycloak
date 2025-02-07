import {readFileSync} from "node:fs" 

import { it, describe, assert } from "./lib"
import { Configuration } from "./lib/configuration"
import { beforeEach } from "node:test"

describe("wellknown", () => {
    let configuration
    beforeEach(() => {
        configuration = loadConfiguration()
    })
     
    it("should get a valid wellknown", () => {
        console.log("DEMO")
        assert(true)
    })
})

function loadConfiguration() {
    const contents = readFileSync("./configuration.json", { encoding: 'utf8', flag: 'r' })
    return JSON.parse(contents) as Configuration

}
