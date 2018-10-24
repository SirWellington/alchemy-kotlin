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
import com.natpryce.hamkrest.greaterThanOrEqualTo
import com.natpryce.hamkrest.lessThanOrEqualTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.TimeGenerators
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.GenerateDate
import tech.sirwellington.alchemy.test.junit.runners.GenerateDate.Type.FUTURE
import tech.sirwellington.alchemy.test.junit.runners.GenerateDate.Type.PAST
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.time.Instant
import java.time.Instant.now
import java.time.temporal.ChronoUnit.*
import java.util.Date
import kotlin.math.absoluteValue
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@RunWith(AlchemyTestRunner::class)
@Repeat
class InstantsPlusTests
{

    @GenerateDate(PAST)
    private lateinit var pastDate: Date

    private val pastInstant: Instant get() = pastDate.toInstant()

    @GenerateDate(FUTURE)
    private lateinit var futureDate: Date
    private val futureInstant: Instant get() = futureDate.toInstant()

    @Before
    fun setUp()
    {
    }

    @Test
    fun testIsInThePast()
    {
        assertTrue { pastInstant.isInThePast() }
        assertFalse { futureInstant.isInThePast() }
    }

    @Test
    fun testIsInTheFuture()
    {
        assertTrue { futureInstant.isInTheFuture() }
        assertFalse { pastInstant.isInTheFuture() }
    }

    @Test
    fun testAsDate()
    {
        val result = pastInstant.asDate()
        assertThat(result, equalTo(pastDate))
    }

    @Test
    fun testUntilNowWhenPast()
    {
        val time = TimeGenerators.pastInstants().get()
        val unit = listOf(MILLIS, SECONDS, MINUTES, HOURS, NANOS).anyElement!!
        val expected = time.until(now(), unit).absoluteValue
        val result = time.timeAgo(unit)
        val ε = 5
        assertThat(result, greaterThanOrEqualTo(expected - ε))
        assertThat(result, lessThanOrEqualTo(expected + ε))
    }

}