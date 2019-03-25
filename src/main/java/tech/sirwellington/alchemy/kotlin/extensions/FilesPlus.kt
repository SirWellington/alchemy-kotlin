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

import java.io.File
import java.io.InputStream


object FilesPlus
{

    /**
     * Creates a temporary file
     * @author SirWellington
     */
    @JvmStatic
    fun makeTempFile(name: String = "${Int.random(0, Int.MAX_VALUE)}", extension: String? = null): File
    {
        return File.createTempFile(name, extension)
    }

}

/**
 * Copies the content of the [istream] into this file.
 * @return The number of total bytes copied.
 *
 * @author SirWellington
 */
fun File.writeStream(istream: InputStream): Long
{
    this.outputStream().use()
    { ostream ->

        istream.use()
        { istream ->

            return istream.copyTo(ostream)
        }
    }
}