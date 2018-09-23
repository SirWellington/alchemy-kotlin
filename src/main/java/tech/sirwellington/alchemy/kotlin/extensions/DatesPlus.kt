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

import java.time.LocalDate
import java.time.Period
import java.util.Date


/**
 * @return `true` if this Date is in the past, `false` otherwise.
 * @author SirWellington
 */
fun Date.isInThePast(): Boolean = this.time < Date().time

/**
 * @return `true` if this date is in the future, `false` otherwise.
 * @author SirWellington
 */
fun Date.isInTheFuture(): Boolean = this.time > Date().time


/**
 * @return Returns how many years ago the date was.
 * @author SirWellington
 */
val java.sql.Date.yearsAgo: Int
    get()
    {
        val period = Period.between(this.toLocalDate(), LocalDate.now())
        return period.years
    }

/**
 * @return Returns how many years ago the date was.
 * @author SirWellington
 */
val java.util.Date.yearsAgo get() = this.toSqlDate().yearsAgo

/**
 * @return a [java.util.Date] version of this [java.sql.Date] object.
 */
fun java.sql.Date.toJavaUtilDate() = java.util.Date(this.time)

/**
 * @return a [java.sql.Date] version of this [java.util.Date] object.
 */
fun java.util.Date.toSqlDate() = java.sql.Date(this.time)