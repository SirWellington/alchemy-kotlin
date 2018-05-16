package tech.sirwellington.alchemy.kotlin.extensions

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.BinaryGenerators
import tech.sirwellington.alchemy.test.hamcrest.nonEmptyString
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import tech.sirwellington.alchemy.test.junit.runners.GenerateString.Type.ALPHABETIC
import tech.sirwellington.alchemy.test.junit.runners.GenerateURL
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.net.URL
import javax.xml.bind.DatatypeConverter
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(AlchemyTestRunner::class)
@Repeat(150)
class StringsPlusKtTest
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
    fun testAsURL()
    {
        val expected = url
        val string = url.toString()
        val result = string.asURL
        assertThat(result, equalTo(expected))
    }

}