/*
 * Copyright 2017 SirWellington Tech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.kotlin.extensions

import java.lang.ref.WeakReference


/**
 *
 * @author SirWellington
 */

/**
 * Creates a Weak Reference to [ref], and returns a closure that, when called, passes it to [block].
 *
 * @param [ref] The object to turn into a weak reference
 * @param [block] The closure to call with the weak reference.
 *
 * @return A closure that, when invoked, calls [block] with a [weak reference][WeakReference] to [ref].
 */
inline fun <reified T> weak(ref: T, crossinline block: (ref: WeakReference<T>) -> Unit): () -> Unit
{
    val weakRef = ref.asWeak()

    return {
        block(weakRef)
    }
}

/**
 * Similar to [weak], except that the closure [block] is only called if
 * [ref] hasn't been garbage-collected, and unwraps to a non-null reference.
 */
inline fun <reified T> weakNonNull(ref: T, crossinline block: (ref: T) -> Unit): (T) -> Unit
{
    val weakReference = ref.asWeak()

    return function@ {
        val strongReference = weakReference.get() ?: return@function
        block(strongReference)
    }
}

/**
 * @return A [weak reference][WeakReference] to `this`.
 */
fun <T> T.asWeak(): WeakReference<T> = WeakReference(this)

/**
 * @return `true` if `this` [WeakReference] points to the same object as [other].
 */
fun <T> WeakReference<T>.isTheSameReferenceAs(other: WeakReference<T>): Boolean
{
    return this.get() === other.get()
}

/**
 * @return `true` if this [List] of [WeakReference][WeakReference] already contains a reference to [weakReference],
 *         `false` otherwise.
 */
fun <T> List<WeakReference<T>>.alreadyContainsWeakRef(weakReference: WeakReference<T>): Boolean
{
    forEach {
        if (it.isTheSameReferenceAs(weakReference))
        {
            return true
        }
    }

    return false
}

/**
 * @return `true` if `this == null`, `false` otherwise.
 */
val Any?.isNull: Boolean get() = this == null

/**
 * @return `true` if `this != null`, `false` otherwise.
 */
val Any?.notNull: Boolean get() = this != null