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

import java.util.Random


/**
 *
 * @author SirWellington
 */

/**
 * @return A Random [Int] from [fromInclusive] to [toExclusive].
 * @throws IllegalArgumentException if [fromInclusive] `>` [toExclusive].
 */
fun Int.Companion.random(fromInclusive: Int, toExclusive: Int): Int
{
    if (fromInclusive == toExclusive) return fromInclusive

    if (fromInclusive > toExclusive) throw IllegalArgumentException("Expected from[$fromInclusive] to be < to[$toExclusive]")

    val random = Random()
    val diff = toExclusive - fromInclusive
    return fromInclusive + random.nextInt(diff)
}

/**
 * @return `true` if this number is even, `false` if it isn't.
 */
val Int.isEven get() = this % 2 == 0

/**
 * @return `true` if this number if odd, `false` otherwise.
 */
val Int.isOdd get() = !isEven

/**
 * @return A random [Int] from the range of numbers supplied.
 */
fun ClosedRange<Int>.random() = Int.random(this.start, this.endInclusive)

/**
 * Returns a `!` version of `this`.
 *
 * For example:
 * ```
 *
 * val willBeTrue = false.inversed()
 * ```
 */
fun Boolean.inversed(): Boolean = !this

/**
 * Repeats a block of code `this` times.
 *
 * ---
 * Example:
 *
 * ```
 *  3.times {
 *      println("Hello")
 *  }
 *  //prints "Hello" 3 different times.
 * ```
 *
 */
inline fun Int.repeat(block: () -> (Unit))
{
    (0 until this).forEach { block() }
}