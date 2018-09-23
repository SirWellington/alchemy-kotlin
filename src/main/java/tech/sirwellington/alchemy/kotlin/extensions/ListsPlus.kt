/*
 * Copyright © 2018. Sir Wellington.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tech.sirwellington.alchemy.kotlin.extensions

import tech.sirwellington.alchemy.annotations.concurrency.ThreadUnsafe
import java.util.Collections
import java.util.LinkedList


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
 * @return The first element of the list, or `null` if the list is empty.
 */
val <T> List<T>?.first: T? get() = this?.firstOrNull()


/**
 * @return the last element of the list, or `null` if the list is empty.
 */
val <T> List<T>?.last: T? get() = this?.lastOrNull()

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

/**
 * Removes and returns the first element, and then adds it to the back of the list.
 * Use this function to turn any list into a circular array.
 *
 * @return The element currently at the front.
 */
@ThreadUnsafe
inline fun <reified T> MutableList<T>.circulateNext(): T?
{
    if (this.isEmpty()) return null
    val first = removeAt(0)
    add(first)

    return first
}

/**
 * Creates a list of size [size], using the specified [generator].
 */
fun <E> createListOf(size: Int = 10, generator: () -> E): List<E>
{
    when
    {
        size < 0 -> throw IllegalArgumentException("size must be >= 0")
        size == 0 -> return emptyList()
    }

    return (0 until size).map { generator() }
}

/**
 * @return `true` if this collection is `null` or [empty][Collection.isEmpty].
 */
val <E> Collection<E>?.isNullOrEmpty
    get() = this == null || this.isEmpty()

/**
 * @return `true` if this collection is neither `null` nor [empty][Collection.isEmpty].
 */
val <E> Collection<E>?.notNullOrEmpty
    get() = !isNullOrEmpty

/**
 * Remove and returns the first element of this list.
 */
fun <E> MutableList<E>.popFirst(): E?
{
    return if (this.isEmpty())
    {
        null
    }
    else
    {
        this.removeAt(0)
    }
}

/**
 * Removes and returns the last element of this list.
 */
fun <E> MutableList<E>.popLast(): E?
{
    return if (this.isEmpty())
    {
        null
    }
    else
    {
        this.removeAt(lastIndex)
    }

}

//===========================================
// LINKED LIST EXTENSIONS
//===========================================

/**
 * Creates a [LinkedList] from [this].
 *
 * @author SirWellington
 */
fun <E> Collection<E>.toLinkedList(): LinkedList<E>
{
    return LinkedList(this)
}

/**
 * Removes and returns the first element in the LinkedList, or returns `null` if the list
 * is empty.
 */
fun <E> LinkedList<E>.popSafe(): E?
{
    return if (isEmpty()) null else pop()
}

/**
 * @return `true` if the list is [List.isEmpty], `false` otherwise.
 */
val <E> List<E>.notEmpty get() = this.isNotEmpty()

/**
 * Removes and returns the remaining elements in the LinkedList.
 * This function essentially returns the list in reverse.
 */
fun <E> LinkedList<E>.popRemaining(collector: (E) -> (Unit))
{
    while (notEmpty)
    {
        val value = popSafe()

        if (value != null)
            collector(value)
        else
            return
    }
}