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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.CollectionGenerators.Companion.listOf
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.integers
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.negativeIntegers
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.strings
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AlchemyTestRunner::class)
@Repeat
class ListsPlusKtTest
{

    private lateinit var list: List<String>

    private lateinit var mutable: MutableList<String>

    private lateinit var element: String

    @GenerateString
    private lateinit var newElement: String


    @Before
    fun setup()
    {
        list = listOf(strings())
        mutable = list.toMutableList()
        element = list.anyElement!!
    }

    @Repeat(5_000)
    @Test
    fun testIsValidIndexWhenInvalid()
    {
        val negativeIndex = one(negativeIntegers())
        val outOfBoundsIndex = one(integers(list.size, Int.MAX_VALUE))

        assertFalse { list.isValidIndex(negativeIndex) }
        assertFalse(list.isValidIndex(outOfBoundsIndex))
    }

    @Test
    fun testIsValidIndexWhenValid()
    {
        val goodIndex = one(integers(0, list.size))
        assertTrue(list.isValidIndex(goodIndex))
        assertTrue(list.isValidIndex(0))
    }

    @Test
    fun testIsValidIndexInWhenValid()
    {
        val goodIndex = one(integers(0, list.size))
        assertTrue(goodIndex.isValidIndexIn(list))
        assertTrue(0.isValidIndexIn(list))
    }

    @Test
    fun testIsValidIndexInWhenInvalid()
    {
        val badIndex1 = one(negativeIntegers())
        val badIndex2 = one(integers(list.size, Int.MAX_VALUE))

        assertFalse(badIndex1.isValidIndexIn(list))
        assertFalse(badIndex2.isValidIndexIn(list))
    }

    @Test
    fun testAddToFront()
    {
        mutable.addToFront(newElement)

        val expected = listOf(newElement) + list

        assertThat(mutable, equalTo(expected))
    }

    @Test
    fun testAddToFrontWhenListEmpty()
    {
        val emptyList = mutableListOf<String>()
        emptyList.addToFront(newElement)

        val expected = mutableListOf(newElement)

        assertThat(emptyList, equalTo(expected))
    }

    @Test
    fun testPrepend()
    {
        mutable.prepend(newElement)

        val expected = listOf(newElement) + list

        assertThat(mutable, equalTo(expected))
    }

    @Test
    fun testPrependWhenListEmpty()
    {
        val mutable = mutableListOf<String>()
        mutable.prepend(newElement)

        val expected = mutableListOf(newElement)

        assertThat(mutable, equalTo(expected))
    }

    @Test
    fun testDoesNotContain()
    {
        assertTrue { list.doesNotContain(newElement) }

        assertFalse { list.doesNotContain(element) }
    }

    @Test
    fun testRemoveElementIfWhenContainsElement()
    {
        val result = mutable.removeIf { it == element }
        assertTrue { result }
        assertFalse { mutable.contains(element) }

    }

    @Test
    fun testRemoveElementIfWhenDoesNotContainElement()
    {
        val result = mutable.removeIf { it == newElement }
        assertFalse { result }
        assertFalse { mutable.contains(newElement) }
    }

    @Test
    fun testContainsWhere()
    {
        val result = list.containsWhere { it == element }
        assertTrue { result }
    }

    @Test
    fun testContainsWhereWhenNotContains()
    {
        val result = list.containsWhere { it == null }
        assertFalse { result }
    }

    @Test
    fun testRemoveWhere()
    {
        val result = mutable.removeWhere { it == element }
        assertTrue { result }
    }

    @Test
    fun testRemoveWhereWhenNotContains()
    {
        val result = mutable.removeWhere { it == newElement }
        assertFalse { result }
    }

}