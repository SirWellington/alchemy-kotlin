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

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one
import tech.sirwellington.alchemy.generator.BooleanGenerators.Companion.booleans
import tech.sirwellington.alchemy.generator.NumberGenerators
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.positiveIntegers
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.smallPositiveIntegers
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat

@RunWith(AlchemyTestRunner::class)
@Repeat
class PrimitivesPlusKtTest
{
    @Test
    fun testRandom()
    {
        val begin = one(NumberGenerators.integers(0, 100))
        val end = one(NumberGenerators.integers(100, 1_000))

        val result = Int.random(begin, end)
        assertTrue(result in begin until end)
    }

    @Test
    fun testIsEven()
    {
        val number = NumberGenerators.anyIntegers().get()
        val expected = number % 2 == 0
        val result = number.isEven

        assertThat(result, equalTo(expected))
    }

    @Test
    fun testIsOdd()
    {
        val number = NumberGenerators.anyIntegers().get()
        val expected = number % 2 != 0
        val result = number.isOdd

        assertThat(result, equalTo(expected))
    }

    @Test
    fun testClosedRangeRandom()
    {
        val begin = one(NumberGenerators.integers(0, 100))
        val end = one(NumberGenerators.integers(100, Int.MAX_VALUE))

        val range = begin until end
        val result = range.random()
        assertTrue(result in range)
    }

    @Test
    fun testInversed()
    {
        val value = one(booleans())
        val result = value.inversed()

        assertThat(result, equalTo(!value))
    }

    @Test
    fun testRepeat()
    {
        var number = 0
        val times = one(smallPositiveIntegers())

        times.repeat {
            number += 1
        }

        assertThat(number, equalTo(times))
    }

    @DontRepeat
    @Test
    fun testRepeatWhenZero()
    {
        var counter = one(positiveIntegers())
        val original = counter

        0.repeat { counter += 1 }

        assertThat(counter, equalTo(original))
    }

}