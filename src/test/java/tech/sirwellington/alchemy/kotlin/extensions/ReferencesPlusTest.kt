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
import com.natpryce.hamkrest.sameInstance
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.generator.one
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner

@RunWith(AlchemyTestRunner::class)
class ReferencesPlusTest
{

    private lateinit var originalReference: String

    @Before
    fun setUp()
    {
        originalReference = one(StringGenerators.alphabeticStrings())
    }

    @Test
    fun testAsWeak()
    {
        val weakReference = originalReference.asWeak()

        assertTrue(weakReference.notNull)
        assertTrue(weakReference.get().notNull)
        assertThat(weakReference.get(), equalTo(originalReference))
    }

    @Test
    fun testAsWeakWhenCollected()
    {
        val weakReference = originalReference.asWeak()
        originalReference = one(StringGenerators.alphabeticStrings())

        System.gc()

        assertTrue(weakReference.notNull)
        assertTrue(weakReference.isNull)

    }

    @Test
    fun testIsTheSameReferenceAs()
    {
        val first = originalReference.asWeak()
        val second = originalReference.asWeak()
        assertThat(first, !sameInstance(second))
    }

    @Test
    fun testAlreadyContainsWeakRef()
    {
        val first = originalReference.asWeak()
        val list = mutableListOf(first)
        val second = originalReference.asWeak()

        assertThat(first, !equalTo(second))
        assertTrue(list.alreadyContainsWeakRef(second))
    }

    @Test
    fun testAlreadyContainsWeakRefWhenItDoesNot()
    {
        val firstRef = originalReference.asWeak()
        val secondValue = one(StringGenerators.alphabeticStrings())
        val secondRef = secondValue.asWeak()

        val list = mutableListOf(firstRef)

        assertTrue(list.alreadyContainsWeakRef(originalReference.asWeak()))
        assertFalse(list.alreadyContainsWeakRef(secondRef))

        list.add(secondRef)

        assertThat(list.size, equalTo(2))
    }


    @Test
    fun testIsNull()
    {
        val nullRef: Any? = null
        assertTrue(nullRef.isNull)

        val nonNullRef = originalReference
        assertFalse(nonNullRef.isNull)
    }

    @Test
    fun testNotNull()
    {
        val nonNullRef = originalReference
        assertTrue(nonNullRef.notNull)

        val nullRef: String? = null
        assertFalse(nullRef.notNull)
    }

}