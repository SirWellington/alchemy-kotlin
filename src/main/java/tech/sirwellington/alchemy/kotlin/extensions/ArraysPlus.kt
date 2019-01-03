/*
 * Copyright © 2019. Sir Wellington.
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


/**
 *
 * @author SirWellington
 */

/**
 * Checks whether [index] is a valid index in `this`
 * array.
 *
 * @return `true` if [index] is a valid index in this array, false otherwise.
 * @see [notValidIndex]
 */
fun <T> Array<T>.isValidIndex(index: Int): Boolean
{
    if (index < 0) return false

    return index < size
}

/**
 * Reversed condition of [isValidIndex].
 * This function checks to make sure [index] is an invalid index in `this` array.
 *
 * @return `true` if [index] is not a valid index in `this` array, `false` if it is.
 */
fun <T> Array<T>.notValidIndex(index: Int) = !isValidIndex(index)


/**
 * Checks whether `this` [Int] is a valid index in [array].
 *
 * @return `true` if `this` Int is a valid index in [array], `false, otherwise.
 */
fun <T> Int.isValidIndexIn(array: Array<T>): Boolean
{
    if (this < 0) return false

    return this < array.size
}

/**
 * A reverse condition of [isValidIndexIn]
 * Checks whether `this` [Int] is an invalid index in [array].
 *
 * @return `true` if `this` is an invalid index in [array], false if it is valid.
 * @see [notValidIndex]
 * @see [isValidIndexIn]
 */
fun <T> Int.notValidIndexIn(array: Array<T>) = !isValidIndexIn(array)

/**
 * Access a random element in this array.
 *
 * @return A random element in this array, `null` if the array is empty.
 */
val <T> Array<T>.anyElement: T?
    get()
    {
        if (isEmpty()) return null

        val range = (0 until size)
        val index = range.random()

        return this.getOrNull(index)
    }