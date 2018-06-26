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
import com.natpryce.hamkrest.isIn
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.CollectionGenerators
import tech.sirwellington.alchemy.generator.NumberGenerators
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.Repeat

@RunWith(AlchemyTestRunner::class)
@Repeat
class ArraysPlusKtTest
{

    private lateinit var array: Array<String>

    private var negativeIndex = -1
    private var outOfBoundsIndex = 0
    private var goodIndex = 0

    @Before
    fun setUp()
    {
        array = CollectionGenerators.listOf(StringGenerators.strings()).toTypedArray()

        negativeIndex = one(NumberGenerators.negativeIntegers())
        outOfBoundsIndex = one(NumberGenerators.integers(array.size, Int.MAX_VALUE))
        goodIndex = one(NumberGenerators.integers(0, array.size))
    }

    @Test
    fun isValidIndexWhenValid()
    {
        assertTrue(array.isValidIndex(0))
        assertTrue(array.isValidIndex(goodIndex))
    }

    @Test
    fun isValidIndexWhenInvalid()
    {
        assertFalse(array.isValidIndex(negativeIndex))
        assertFalse(array.isValidIndex(outOfBoundsIndex))
    }

    @Test
    fun isValidIndexIn()
    {
        assertTrue(0.isValidIndexIn(array))
        assertTrue(goodIndex.isValidIndexIn(array))
    }

    @Test
    fun isValidIndexInWhenInvalid()
    {
        assertFalse(negativeIndex.isValidIndexIn(array))
        assertFalse(outOfBoundsIndex.isValidIndexIn(array))
    }

    @Test
    fun testAnyElement()
    {
        val element = array.anyElement

        assertTrue(element.isNotNull)
        assertThat(element!!, isIn(array.toList()))
    }

    @Test
    fun testAnyElementWhenEmpty()
    {
        val emptyList = listOf<String>()
        val element = emptyList.anyElement
        assertTrue(element.isNull)
    }

}