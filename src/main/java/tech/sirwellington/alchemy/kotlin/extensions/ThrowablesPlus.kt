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


/**
 * Returns the result in [block], or if an [Exception] is thrown,
 * returns `null` instead.
 *
 * @author SirWellington
 */
inline fun <T> tryOrNull(block: () -> (T)): T?
{
    return try
    {
        block()
    }
    catch (ex: Exception)
    {
        null
    }
}

inline fun <T, reified E : Throwable> tryOrNull(exceptionType: Class<E> = E::class.java, block: () -> T): T?
{
    return try
    {
        block()
    }
    catch (ex: Throwable)
    {
        if (ex is E)
        {
            null
        }
        else
        {
            throw ex
        }
    }
}