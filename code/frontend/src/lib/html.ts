
const replaceValues = (text: string, props: {[key: string]: any}) => text.replace(/\$\{(\w+)\}/g, (match: string, expr: any) => props[expr])

const html = async (name: string, model: {[key: string]: any} = {}) => {
    const response = await fetch(`./html/${name}.html`)
    const text = await response.text()
    return replaceValues(text, model)
}

export { html }