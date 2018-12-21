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

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.isEmptyString
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.BinaryGenerators
import tech.sirwellington.alchemy.test.hamcrest.hasSize
import tech.sirwellington.alchemy.test.hamcrest.isNull
import tech.sirwellington.alchemy.test.hamcrest.nonEmptyString
import tech.sirwellington.alchemy.test.hamcrest.notNull
import tech.sirwellington.alchemy.test.junit.runners.*
import tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.ALPHABETIC
import java.net.URL
import java.util.UUID
import javax.xml.bind.DatatypeConverter
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(AlchemyTestRunner::class)
@Repeat(150)
class StringsPlusTests
{
    @GenerateString
    private lateinit var string: String

    @GenerateString(ALPHABETIC)
    private lateinit var alphabetic: String

    @GenerateURL
    private lateinit var url: URL

    @Test
    fun testTitleCased()
    {
    }

    @Test
    fun testIsEmpty()
    {
        assertTrue { "".isEmpty }
        assertFalse { string.isEmpty }
    }

    @Test
    fun testIsEmptyOrNull()
    {
        assertTrue { "".isEmptyOrNull }
        assertTrue { (null as String?).isEmptyOrNull }
        assertFalse { string.isEmptyOrNull }
        assertFalse { alphabetic.isEmptyOrNull }
    }

    @Test
    fun testIsNotEmptyOrNull()
    {
        assertFalse { "".notEmptyOrNull }
        assertFalse { (null as String?).notEmptyOrNull }
        assertTrue { string.notEmptyOrNull }
        assertTrue { alphabetic.notEmptyOrNull }
    }

    @Test
    fun testGetFirstLetter()
    {
        val expected = string[0].toString()
        val result = string.firstLetter
        assertNotNull(result)
        assertThat(result!!, nonEmptyString)
        assertThat(result, equalTo(expected))
    }

    @Test
    fun testBase64Decode()
    {
        val binary = BinaryGenerators.binary(1024).get()
        val base64 = DatatypeConverter.printBase64Binary(binary)
        val result = base64.base64Decoded
        assertNotNull(result)
        Assert.assertArrayEquals(result, binary)
    }

    @Test
    fun testBase64Encoded()
    {
        val binary = BinaryGenerators.binary(2048).get()
        val expected = DatatypeConverter.printBase64Binary(binary)
        val result = binary.base64Encoded
        assertThat(result, notNull and equalTo(expected))
    }

    @Test
    fun testAsURL()
    {
        val expected = url
        val string = url.toString()
        val result = string.asURL
        assertThat(result, equalTo(expected))
    }

    @Test
    fun testAsURLWhenNotURL()
    {
        val result = string.asURL
        assertNull(result)
    }

    @Test
    fun testLastCharacter()
    {
        val expected = string.last().toString()
        val result = string.lastCharacter
        assertThat(result, equalTo(expected))
    }

    @DontRepeat
    @Test
    fun testLastCharWhenEmpty()
    {
        val string = ""
        val result = string.lastCharacter
        assertThat(result, isNull)
    }

    @Test
    fun testStringBuilderPlusAssign()
    {
        val builder = StringBuilder()
        builder += string
        val result = builder.toString()
        assertThat(result, equalTo(string))

    }

    @Test
    fun testHexadecimalStringWhenBadArg()
    {
        val length = Int.random(-100, 1)
        val result = String.hexadecimal(length = length)
        assertThat(result, notNull and isEmptyString)
    }

    @Test
    fun testHexadecimalString()
    {
        val length = Int.random(1, 100)
        val secure = Booleans.any
        val result = String.hexadecimal(length = length, secureRandom = secure)
        assertThat(result, nonEmptyString)
    }

    @DontRepeat
    @Test
    fun testHexadecimalStringUniqueness()
    {
        val results = mutableSetOf<String>()
        val repetitions = Int.random(100, 5000)
        val length = Int.random(100, 1000)

        repetitions.repeat()
        {
            val result = String.hexadecimal(length)
            results.add(result)
        }

        assertThat(results, hasSize(repetitions))
    }

    @Test
    fun testUUID()
    {
        val result = String.uuid()
        assertThat(result, nonEmptyString)

        val uuid = UUID.fromString(result)
        assertThat(uuid.toString(), equalTo(result))
    }

    @Test
    fun testUUIDWithoutHyphens()
    {
        val result = String.uuid(includeHyphens = false)
        assertThat(result, nonEmptyString)
    }

    @DontRepeat
    @Test
    fun testUUIDUniqueness()
    {
        val repetitions = Int.random(100, 1000)
        val length = Int.random(50, 200)
        val results = mutableSetOf<String>()

        repetitions.repeat()
        {
            val result = String.uuid()
            results.add(result)
        }

        assertThat(results, hasSize(repetitions))
    }

}