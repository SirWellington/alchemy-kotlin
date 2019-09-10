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

import java.net.URL
import java.net.URLEncoder
import java.security.SecureRandom
import java.util.Base64
import java.util.Random
import java.util.UUID


/**
 *
 * @author SirWellington
 */

fun String.titleCased(): String
{
    if (isEmpty()) return this

    val firstCharacter = this[0].toString()
    val firstCharacterCapitalized = firstCharacter.toUpperCase()

    if (length == 1) return firstCharacterCapitalized

    val otherCharacters = substring(1 until length)
    val otherCharactersLowerCased = otherCharacters.toLowerCase()

    return firstCharacterCapitalized + otherCharactersLowerCased
}

fun String.doesNotContain(substring: CharSequence, ignoringCase: Boolean = false): Boolean
{
    val contains = this.contains(substring, ignoreCase = ignoringCase)
    return !contains
}

/**
 * Variable version of [String.isEmpty]
 */
val String.isEmpty get() = isEmpty()

/**
 * Convenient variable to check whether the given
 * string is empty or null.
 */
val String?.isEmptyOrNull: Boolean
    get()
    {
        if (this == null) return true

        return isEmpty()
    }

/**
 * Returns `true`, unless the String is empty or null.
 * This is a useful check to see whether a string is good to
 * use or not.
 */
val String?.notEmptyOrNull: Boolean get() = !this.isEmptyOrNull

/**
 * Returns `true` if the string is either null or blank. Returns `false` otherwise.
 */
val String?.notNullOrBlank: Boolean get() = !this.isNullOrBlank()

/**
 * Gets the first letter of the given string,
 * unless the string is empty, in which case the first letter will be null.
 *
 * @return The first letter of this string, or `null` if this string is empty.
 */
val String.firstLetter: String?
    get()
    {
        if (isEmpty) return null

        val firstCharacter = this[0]
        return firstCharacter.toString()
    }


/**
 * Call this on a Base64-encoded String to get the underlying binary
 *
 * @return The Binary representation of this Base64 encoded string.
 */
val String.base64Decoded: ByteArray?
get()
{
    return tryOrNull()
    {
        Base64Decoder._parseBase64Binary(this)
    }
}

/**
 * Call this on a [ByteArray] to convert in into a Base64-Encoded [String].
 * @return A Base64-encoded version of this [ByteArray].
 */
val ByteArray.base64Encoded: String?
get()
{
    val decoded = Base64.getEncoder().encode(this)
    return decoded.toString(Charsets.UTF_8)
}

/**
 * Call this on a string to attempt to load it as a URL.
 * If the operation fails, `null` will be returned.
 *
 * @return A [URL]-representation of this string
 */
val String.asURL: URL?
get()
{
    return tryOrNull { URL(this) }
}

/**
 * URL-Encodes this string for use with URI paths.
 */
val String.urlEncoded: String
    get() = URLEncoder.encode(this, Charsets.UTF_8.name())

/**
 * @return The last String character in this String.
 */
val String.lastCharacter: String?
    get() = if (isEmpty) null else takeLast(1)


/**
 * Allows for the use of the `+=` operation with [StringBuilder].
 */
operator fun StringBuilder.plusAssign(string: String)
{
    append(string)
}

/**
 * Creates a random Hexadecimal.
 *
 * @param length The desired length of the Hexadecimal.
 * @param secureRandom Whether the hexadecimal generated should be cryptographically secure or not.
 * @param uppercase Whether the string should be uppercased or not.
 */
fun String.Companion.hexadecimal(length: Int = Int.random(15, 100),
                                 secureRandom: Boolean = false,
                                 uppercase: Boolean = true): String
{
    if (length <= 0) return ""

    val buffer = StringBuilder()
    val random = if (secureRandom) SecureRandom() else Random()

    while (buffer.length < length)
    {
        val hex = Integer.toHexString(random.nextInt())
        buffer += hex
    }

    val string = buffer.substring(0, length)

    return if (uppercase) string.toUpperCase() else string.toLowerCase()
}

/**
 * Creates a random [UUID] string using the
 *
 * @param includeHyphens Whether to include the hyphens that are part of the UUID standard, or omit them.
 */
fun String.Companion.uuid(includeHyphens: Boolean = true): String
{
    val uuid =  UUID.randomUUID().toString()

    return if (includeHyphens) uuid else
    {
        uuid.replace("-", "")
    }
}


//===========================================
// BASE 64 Decoder
//===========================================

/**
 * Private object responsible for decoding Base64 strings.
 *
 * @author SirWellington
 */
private object Base64Decoder
{
    private val decodeMap = initDecodeMap()
    private const val PADDING: Byte = 127

    fun _parseBase64Binary(text: String): ByteArray
    {
        val buflen = guessLength(text)
        val out = ByteArray(buflen)
        var o = 0

        val len = text.length
        var i: Int

        val quadruplet = ByteArray(4)
        var q = 0

        // convert each quadruplet to three bytes.
        i = 0
        while (i < len)
        {
            val ch = text[i]
            val v = decodeMap[ch.toInt()]

            if (v.toInt() != -1)
            {
                quadruplet[q++] = v
            }

            if (q == 4)
            {
                // quadruplet is now filled.
                out[o++] = (quadruplet[0].toInt() shl 2 or (quadruplet[1].toInt() shr 4)).toByte()
                if (quadruplet[2] != PADDING)
                {
                    out[o++] = (quadruplet[1].toInt() shl 4 or (quadruplet[2].toInt() shr 2)).toByte()
                }
                if (quadruplet[3] != PADDING)
                {
                    out[o++] = (quadruplet[2].toInt() shl 6 or quadruplet[3].toInt()).toByte()
                }
                q = 0
            }
            i++
        }

        if (buflen == o)
        // speculation worked out to be OK
        {
            return out
        }

        // we overestimated, so need to create a new buffer
        val nb = ByteArray(o)
        System.arraycopy(out, 0, nb, 0, o)
        return nb
    }

    /**
     * computes the length of binary data speculatively.
     *
     *
     *
     * Our requirement is to create byte[] of the exact length to store the binary data.
     * If we do this in a straight-forward way, it takes two passes over the data.
     * Experiments show that this is a non-trivial overhead (35% or so is spent on
     * the first pass in calculating the length.)
     *
     *
     *
     * So the approach here is that we compute the length speculatively, without looking
     * at the whole contents. The obtained speculative value is never less than the
     * actual length of the binary data, but it may be bigger. So if the speculation
     * goes wrong, we'll pay the cost of reallocation and buffer copying.
     *
     *
     *
     * If the base64 text is tightly packed with no indentation nor illegal char
     * (like what most web services produce), then the speculation of this method
     * will be correct, so we get the performance benefit.
     */
    private fun guessLength(text: String): Int
    {
        val len = text.length

        // compute the tail '=' chars
        var j = len - 1
        while (j >= 0)
        {
            val code = decodeMap[text[j].toInt()]
            if (code == PADDING)
            {
                j--
                continue
            }
            if (code.toInt() == -1)
            // most likely this base64 text is indented. go with the upper bound
            {
                return text.length / 4 * 3
            }
            j--
            break
        }

        j++    // text.charAt(j) is now at some base64 char, so +1 to make it the size
        val padSize = len - j
        return if (padSize > 2)
        {
            text.length / 4 * 3
        }
        else text.length / 4 * 3 - padSize

        // so far this base64 looks like it's unindented tightly packed base64.
        // take a chance and create an array with the expected size
    }

    private fun initDecodeMap(): ByteArray
    {
        val map = ByteArray(128)
        var i = 0
        while (i < 128)
        {
            map[i] = -1
            i++
        }

        i = 'A'.toInt()
        while (i <= 'Z'.toInt())
        {
            map[i] = (i - 'A'.toInt()).toByte()
            i++
        }

        i = 'a'.toInt()
        while (i <= 'z'.toInt())
        {
            map[i] = (i - 'a'.toInt() + 26).toByte()
            i++
        }

        i = '0'.toInt()
        while (i <= '9'.toInt())
        {
            map[i] = (i - '0'.toInt() + 52).toByte()
            i++
        }

        map['+'.toInt()] = 62
        map['/'.toInt()] = 63
        map['='.toInt()] = PADDING

        return map
    }
}