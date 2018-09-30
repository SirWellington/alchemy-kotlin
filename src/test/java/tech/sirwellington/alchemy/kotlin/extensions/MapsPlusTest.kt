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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.CollectionGenerators
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.test.hamcrest.isNull
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.Repeat

@RunWith(AlchemyTestRunner::class)
@Repeat
class MapsPlusTest
{

    private lateinit var map: MutableMap<String, String>

    @Before
    fun setUp()
    {
        map = CollectionGenerators.mapOf(StringGenerators.alphabeticStrings(),
                                         StringGenerators.alphanumericStrings())
                .toMutableMap()
    }

    @Test
    fun testRemoveWhereWhenMapIsEmpty()
    {
        map = mutableMapOf()
        val sizeBefore = map.size

        map.removeWhere { true }

        assertThat(map.size, equalTo(sizeBefore))
    }

    @Test
    fun testRemoveWhere()
    {
        val sizeBefore = map.size
        val expectedSize = sizeBefore - 1

        val matchingKey = map.keys.toList().anyElement
        map.removeWhere { it.key == matchingKey }

        assertFalse(map.containsKey(matchingKey))
        assertThat(map.size, equalTo(expectedSize))
    }

    @Test
    fun testRemoveWhereWhenRemovingAll()
    {
        map.removeWhere { true }
        assertTrue(map.isEmpty())
    }

    @Test
    fun testRemoveWhereWhenNothingMatches()
    {
        val sizeBefore = map.size
        val original = map.toMap()

        map.removeWhere { false }

        assertThat(map.size, equalTo(sizeBefore))
        assertThat(map, equalTo(original))
    }

    @Test
    fun testAnyEntry()
    {
        val entry = map.anyEntry!!
        val key = entry.key
        val value = entry.value

        assertTrue(map.containsKey(key))
        assertThat(map[key], equalTo(value))
    }

    @Test
    fun testAnyEntryWhenMapIsEmpty()
    {
        val emptyMap = mutableMapOf<String, String>()
        val emptyMapEntry = emptyMap.anyEntry

        assertThat(emptyMapEntry, isNull)
    }

}
