/*
 * Copyright 2018 SirWellington Tech.
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
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.test.junit.ThrowableAssertion
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateString
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.sql.SQLException
import kotlin.test.assertTrue

@RunWith(AlchemyTestRunner::class)
@Repeat(50)
class ThrowablesPlusKtTest
{

    @GenerateString
    private lateinit var string: String

    @Test
    fun testTryOrNull()
    {
        val result = tryOrNull { string }
        assertThat(result, equalTo(string))
    }

    @Test
    fun testTryOrNullWhenThrows()
    {
        val result = tryOrNull {
            if (!string.isEmpty)
                throw RuntimeException("failed")
            else
                string
        }

        assertTrue { result.isNull }
    }

    @Test
    fun testTryOrNullWithSpecificException()
    {
        val result = tryOrNull(CustomException::class.java) {
            string
        }

        assertThat(result, equalTo(string))
    }

    @Test
    fun testTryOrNullWithSpecificExceptionWhenExceptionThrown()
    {

        val result = tryOrNull(CustomException::class.java) {
            if (string.notNull)
                throw CustomException()
            else
                string
        }

        assertTrue { result.isNull }

        ThrowableAssertion.assertThrows {

            tryOrNull(CustomException::class.java) {

                if (string.notNull)
                    throw SQLException("")
                else
                    string
            }
        }.isInstanceOf(SQLException::class.java)
    }


    class CustomException : RuntimeException()
}