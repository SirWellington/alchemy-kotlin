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

import java.time.Instant
import java.time.Instant.now
import java.time.temporal.ChronoUnit.MILLIS
import java.time.temporal.TemporalUnit
import java.util.Date


fun Instant.isInThePast(): Boolean = this.isBefore(now())

fun Instant.isInTheFuture(): Boolean = this.isAfter(now())

fun Instant.asDate(): Date = Date(this.toEpochMilli())


/**
 * Returns the amount of time that has elapsed since this [Instant]
 * was created.
 */
fun Instant.timeAgo(unit: TemporalUnit = MILLIS): Long = this.until(now(), unit)