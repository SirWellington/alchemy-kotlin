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

import java.util.Collections


/**
 *
 * @author SirWellington
 */

/**
 * Checks whether [index] is a valid index in `this` [List].
 *
 * @return `true` if [index] is a valid index in `this`, `false` otherwise.
 */
fun <T> List<T>.isValidIndex(index: Int): Boolean
{
    if (index < 0) return false

    return index < size
}

/**
 * Checks to make sure [index] is not a valid index in this [List].
 *
 * @return `true` if [index] is an invalid index in `this`, `false` if it a valid index.
 */
fun <T> List<T>.notValidIndex(index: Int) = !isValidIndex(index)

/**
 * @return `true` if `this` [Int] is a valid index in [list], false if it is not.
 */
fun <T> Int.isValidIndexIn(list: List<T>): Boolean
{
    if (this < 0) return false

    return this < list.size
}

/**
 * @return true if this [Number][Int] is a valid index in the given [List],
 * false otherwise.
 */
fun <T> Int.notValidIndexIn(list: List<T>) = !isValidIndexIn(list)

/**
 * @return A shuffled version of this [List].
 */
fun <T> List<T>.shuffled(): List<T>
{
    val result = this.toMutableList()
    Collections.shuffle(result)
    return result
}

/**
 *
 * @return a random element from the list, null if the list is empty or null.
 */
val <T> List<T>?.anyElement: T?
    get()
    {
        if (this == null) return null

        val index = (0..size).random()

        return if (index.isValidIndexIn(this)) this[index] else null
    }

/**
 * Removes all elements from the collection that match the given predicate.
 *
 * @return `true` if any elements were removed, `false` if none were found to match the predicate.
 */
inline fun <reified T> MutableCollection<T>.removeElementIf(predicate: (T) -> (Boolean)): Boolean
{
    val elementsToRemove = this.filter(predicate).toList()
    return this.removeAll(elementsToRemove)
}

/**
 * Adds an element to be front of a [List].
 */
inline fun <reified T> MutableList<T>.addToFront(element: T)
{
    add(0, element)
}

/**
 * Simple alias for [addToFront].
 */
inline fun <reified T> MutableList<T>.prepend(element: T)
{
    addToFront(element)
}

/**
 * @return `true` if [element] is not present in this List, `false` otherwise.
 */
inline fun <reified T> List<T>.doesNotContain(element: T): Boolean = !contains(element)

/**
 * An alias for [any] that returns true if any of the elements in this collection
 * match the [predicate].
 *
 * @param predicate Ran on elements in the list to test whether the condition is met or not.
 *
 * @return `true` If this collection contains an element matching the [predicate], `false` otherwise.
 */
inline fun <reified T> Collection<T>.containsWhere(predicate: (T) -> Boolean): Boolean
{
    return this.any(predicate)
}

/**
 * Simple alias for [removeElementIf].
 *
 * @see removeElementIf
 */
inline fun <reified T> MutableCollection<T>.removeWhere(predicate: (T) -> Boolean): Boolean
{
    return this.removeElementIf(predicate)
}