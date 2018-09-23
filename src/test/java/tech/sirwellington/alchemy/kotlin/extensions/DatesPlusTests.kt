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
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateDate
import tech.sirwellington.alchemy.test.junit.runners.GenerateDate.Type.FUTURE
import tech.sirwellington.alchemy.test.junit.runners.GenerateDate.Type.PAST
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.time.LocalDate
import java.util.Date
import kotlin.math.absoluteValue
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AlchemyTestRunner::class)
@Repeat
class DatesPlusTests
{

    @GenerateDate(FUTURE)
    private lateinit var futureDate: Date

    @GenerateDate(PAST)
    private lateinit var pastDate: Date

    @Before
    fun setUp()
    {

    }

    @Test
    fun testIsInThePast()
    {
        assertTrue { pastDate.isInThePast() }
        assertFalse { futureDate.isInThePast() }
    }

    @Test
    fun testIsInTheFuture()
    {
        assertTrue { futureDate.isInTheFuture() }
        assertFalse { pastDate.isInTheFuture() }
    }

    @Test
    fun testUtilDateYearsAgo()
    {
        val result = pastDate.yearsAgo

        val pastLocalDate = java.sql.Date(pastDate.time).toLocalDate()
        val expected = pastLocalDate.until(LocalDate.now()).years.absoluteValue

        assertThat(result, equalTo(expected))
    }

    @Test
    fun testSqlDateYearsAgo()
    {
        val expected = pastDate.yearsAgo
        val result = pastDate.toSqlDate().yearsAgo

        assertThat(result, equalTo(expected))
    }

    @Test
    fun testSqlDateToUtilDate()
    {
        val sqlDate = java.sql.Date(futureDate.time)
        val result = sqlDate.toJavaUtilDate()
        assertThat(result, equalTo(futureDate))
    }

    @Test
    fun testUtilDateToSqlDate()
    {
        val sqlDate = java.sql.Date(pastDate.time)
        val result = pastDate.toSqlDate()
        assertThat(result, equalTo(sqlDate))
    }

}