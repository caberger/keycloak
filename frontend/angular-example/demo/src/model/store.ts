/** Smalle implementation of State Handling in web applications
 * Do not use in production.
 * For educational purposes only.
 * Three Design Principles
 * @see https://redux.js.org/understanding/thinking-in-redux/three-principles
 * @author Christian Aberger
 * (c) Christian Aberger (2025)
 * https://www.aberger.at
*/

import { produce, WritableDraft } from 'immer'
import { BehaviorSubject } from 'rxjs'
import { Post } from '../feature'

/* 1. single source of truth */
const store = new BehaviorSubject<Model>({
    posts: [],
    title: "Demo"
})

/* 2. State is read-only */

interface Model {
    readonly posts: Post[],
    readonly jwt?: string,
    readonly title: string,
}

/** 3. Changes are made with pure functions */
function set(recipe: (draft: WritableDraft<Model>) => void) {
    store.next(produce(store.getValue(), recipe))
}

export { store, set }
