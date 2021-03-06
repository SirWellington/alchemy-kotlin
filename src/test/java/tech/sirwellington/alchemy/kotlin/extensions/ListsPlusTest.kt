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

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.hasElement
import com.natpryce.hamkrest.isEmpty
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.CollectionGenerators.Companion.listOf
import tech.sirwellington.alchemy.generator.NumberGenerators
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.integers
import tech.sirwellington.alchemy.generator.NumberGenerators.Companion.negativeIntegers
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.generator.StringGenerators.Companion.strings
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.hamcrest.hasSize
import tech.sirwellington.alchemy.test.hamcrest.isNull
import tech.sirwellington.alchemy.test.hamcrest.notNull
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateList
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import tech.sirwellington.alchemy.test.kotlin.assertThrows
import java.util.LinkedList
import kotlin.test.*

@RunWith(AlchemyTestRunner::class)
@Repeat(200)
class ListsPlusTest
{

    private lateinit var list: List<String>

    private lateinit var mutable: MutableList<String>

    private val emptyList = mutableListOf<String>()

    private lateinit var element: String

    @GenerateString
    private lateinit var newElement: String

    private val generateNewString
        get() = StringGenerators.alphanumericStrings().get()


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
    fun testContainsWhereWhenContains()
    {
        val result = list.containsWhere { it == element }
        assertTrue { result }
    }

    @Test
    fun testContainsWhereWhenNotContains()
    {
        val result = list.containsWhere { it.isEmptyOrNull }
        assertFalse { result }
    }

    @Test
    fun testDoesNotContainWhereWhenContains()
    {
        val result = list.doesNotContainWhere { it.isEmptyOrNull }
        assertTrue { result }
    }

    @Test
    fun testDoesNotContainWhereWhenDoesNotContain()
    {
        val result = list.doesNotContainWhere { it.length > 1 }
        assertFalse { result }
    }

    @Test
    fun testContainsAnyOfWithNoArgs()
    {
        assertFalse { list.containsAnyOf() }
    }

    @Test
    fun testContainsAnyOfWithOneArg()
    {
        val element = list.anyElement!!
        assertTrue { list.containsAnyOf(element) }
    }

    @Test
    fun testContainsAnyOfWithTwo()
    {
        val first = list.anyElement!!
        val second = generateNewString
        assertTrue { list.containsAnyOf(first, second) }
    }

    @Test
    fun testContainsAnyOfWithMultiple()
    {
        val first = list.shuffled().takeLast(3).toTypedArray()
        assertTrue { list.containsAnyOf(*first) }

        val existing = list.anyElement!!
        val second = createListOf(10) { generateNewString } + listOf(existing)
        assertTrue { list.containsAnyOf(*second.toTypedArray()) }
    }

    @Test
    fun testContainsAnyOfWhenNotContains()
    {
        val string = generateNewString
        assertFalse { list.containsAnyOf(string) }

        val multiple = createListOf(10) { generateNewString }
        assertFalse { list.containsAnyOf(*multiple.toTypedArray()) }
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

    @Test
    fun testAnyElement()
    {
        val result = list.anyElement
        assertThat(list, hasElement(result))
    }

    @Test
    fun testFirst()
    {
        val result = list.first
        assertNotNull(result)
        assertEquals(result, list[0])
    }

    @Test
    fun testFirstWhenListIsEmpty()
    {
        val first = emptyList.first
        assertNull(first)
    }

    @Test
    fun testSecond()
    {
        val result = list.second
        assertThat(result, notNull and equalTo(list[1]))
    }

    @Test
    fun testSecondWhenEmpty()
    {
        val result = emptyList.second
        assertThat(result, isNull)
    }

    @Test
    fun testThird()
    {
        val result = list.third
        assertThat(result, equalTo(list[2]))
    }

    @Test
    fun testThirdWhenEmpty()
    {
        val result = emptyList.third
        assertThat(result, isNull)
    }

    @Test
    fun testLast()
    {
        val result = list.last
        assertNotNull(result)
        assertEquals(result, list[list.size-1])
    }

    @Test
    fun testLastWhenListIsEmpty()
    {
        val emptyList = emptyList<String>()
        val last = emptyList.last
        assertNull(last)
    }

    @Test
    fun testCirculateNext()
    {
        val copy = list.toMutableList()
        val first = copy.circulateNext()
        assertThat(first, equalTo(list.first))
        assertThat(copy.last, equalTo(first))
    }

    @Test
    fun testCirculateNextCompletesList()
    {
        val copy = list.toMutableList()
        val collect = mutableListOf<String>()

        (0 until list.size).forEach()
        {
            val next = copy.circulateNext()
            collect.add(next!!)
        }

        assertThat(collect, equalTo(list))
        assertThat(copy, equalTo(list))
    }

    @Test
    fun testCirculateNextDoesNotRunOut()
    {
        val copy = list.toMutableList()
        val iterations = list.size * 3

        (0 until iterations).forEach()
        {
            val result = copy.circulateNext()
            assertThat(list, hasElement(result))
        }
    }

    @Test
    fun testCirculateNextWhenEmpty()
    {
        val emptyList = mutableListOf<String>()
        val next = emptyList.circulateNext()
        assertNull(next)
    }

    @Test
    fun testCreateListOf()
    {
        val values = list
        val size = Int.random(5, 100)

        val result = createListOf(size) {
            values.anyElement!!
        }

        assertThat(result, hasSize(size))

        result.forEach { assertThat(values, hasElement(it)) }
    }

    @Test
    fun testCreateListWithSizeZero()
    {
        val result = createListOf(0) { list.anyElement!! }
        assertThat(result, notNull and isEmpty)
    }

    @Test
    fun testCreateListWithSameValue()
    {
        val size = Int.random(10, 100)
        val result = createListOf(size) { this.element }
        assertThat(result, hasSize(size))
        result.forEach { assertThat(it, equalTo(element)) }
    }

    @Test
    fun testCreateListWithInvalidSize()
    {
        val badSize = NumberGenerators.negativeIntegers().get()

        assertThrows { createListOf(badSize) { "" } }
                .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testIsNullOrEmpty()
    {
        assertFalse { list.isNullOrEmpty }
        assertTrue { emptyList<String>().isNullOrEmpty }
        assertTrue { (null as List<String>?).isNullOrEmpty }
    }

    @Test
    fun testNotNullOrEmpty()
    {
        assertTrue { list.notNullOrEmpty }
        assertFalse { emptyList<String>().notNullOrEmpty }
        assertFalse { (null as List<String>?).notNullOrEmpty }
    }

    @Test
    fun testPopFirst()
    {
        val list = this.list.toMutableList()
        val expectedElement = list.first
        val expectedList = list.drop(1)

        val result = list.popFirst()
        assertThat(result, notNull and equalTo(expectedElement))
        assertThat(list, equalTo(expectedList))
    }

    @Test
    fun testPopFirstWhenEmpty()
    {
        val list = mutableListOf<String>()
        val result = list.popFirst()
        assertNull(result)
        assertThat(list, notNull and isEmpty)
    }

    @Test
    fun testPopLast()
    {
        val list = this.list.toMutableList()
        val expectedElement = list.last
        val expectedList = list.dropLast(1)

        val result = list.popLast()
        assertThat(result, equalTo(expectedElement))
        assertThat(list, equalTo(expectedList))
    }

    @Test
    fun testPopLastWhenEmpty()
    {
        val list  = mutableListOf<String>()

        val result = list.popLast()
        assertNull(result)
        assertThat(list, isEmpty and notNull)
    }


}

@RunWith(AlchemyTestRunner::class)
@Repeat(200)
class LinkedListTests
{
    @GenerateList(value = String::class, size = 25)
    private lateinit var list: List<String>

    @Test
    fun testToLinkedList()
    {
        val expected = LinkedList(list)
        val result = list.toLinkedList()
        assertThat(result, equalTo(expected))
        Assert.assertTrue(result is LinkedList)
    }

    @Test
    fun testPopSafeWhenEmpty()
    {
        val emptyList = listOf<String>().toLinkedList()
        val result = emptyList.popSafe()
        assertThat(result, tech.sirwellington.alchemy.test.hamcrest.isNull)
    }

    @Test
    fun testPopSafe()
    {
        val list = this.list.toLinkedList()
        val expected = list.first
        val result = list.popSafe()

        assertThat(result, equalTo(expected))
        assertThat(list, hasSize(this.list.size - 1))
    }

    @Test
    fun testGetNotEmptyWhenEmpty()
    {
        val emptyList = LinkedList<String>()
        Assert.assertFalse(emptyList.notEmpty)
    }

    @Test
    fun testNotEmpty()
    {
        val list = this.list.toLinkedList()
        Assert.assertTrue(list.notEmpty)
    }
}