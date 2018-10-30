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

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.or
import junit.framework.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.AlchemyGenerator.Get.one
import tech.sirwellington.alchemy.generator.BooleanGenerators.Companion.booleans
import tech.sirwellington.alchemy.generator.NumberGenerators
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.positiveIntegers
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.smallPositiveIntegers
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.test.hamcrest.notNull
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.DontRepeat
import tech.sirwellington.alchemy.test.junit.runners.Repeat

@RunWith(AlchemyTestRunner::class)
@Repeat
class PrimitivesPlusKtTest
{

    @Test
    fun testRandomInt()
    {
        val begin = one(NumberGenerators.integers(0, 100))
        val end = one(NumberGenerators.integers(100, 1_000))

        val result = Int.random(begin, end)
        assertTrue(result in begin until end)
    }

    @Test
    fun testRandomDouble()
    {
        val begin = one(NumberGenerators.doubles(0.0, 100.0))
        val end = one(NumberGenerators.doubles(100.0, 1_000.0))

        val result = Double.random(begin, end)
        assertTrue(result >= begin)
        assertTrue(result <= end)
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

        times.repeat()
        {
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

    @Repeat(25)
    @Test
    fun testBooleansAny()
    {
        val results = mutableSetOf<Boolean>()
        20.repeat()
        {
            val result = Booleans.any
            assertThat(result, notNull)
            results.add(result)
        }

        assertThat(results.size, equalTo(2))
    }

    @Test
    fun testEitherOr()
    {
        val first = StringGenerators.alphabeticStrings().get()
        val second = StringGenerators.alphabeticStrings().get()
        val result = first eitherOr second
        assertThat(result, equalTo(first) or equalTo(second))
    }

    @DontRepeat
    @Test
    fun testEitherOrVariance()
    {
        val first = StringGenerators.alphabeticStrings().get()
        val second = StringGenerators.alphabeticStrings().get()
        val results = mutableSetOf<String>()

        100.repeat()
        {
            val result = first eitherOr second
            results.add(result)
        }

        assertThat(results.size, equalTo(2))
    }

}