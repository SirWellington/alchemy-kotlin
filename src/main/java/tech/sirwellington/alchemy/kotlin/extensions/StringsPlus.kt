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

import java.net.URL
import javax.xml.bind.DatatypeConverter


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
 * Returns true, unless the String is empty or null.
 * This is a useful check to see whether a string is good to
 * use or not.
 */
val String?.notEmptyOrNull: Boolean get() = !this.isEmptyOrNull

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
    return tryOrNull { DatatypeConverter.parseBase64Binary(this) }
}

/**
 * Call this on a [ByteArray] to convert in into a Base64-Encoded [String].
 * @return A Base64-encoded version of this [ByteArray].
 */
val ByteArray.base64Encoded: String?
get()
{
    return tryOrNull { DatatypeConverter.printBase64Binary(this) }
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