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

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tech.sirwellington.alchemy.generator.BinaryGenerators
import tech.sirwellington.alchemy.generator.StringGenerators
import tech.sirwellington.alchemy.test.hamcrest.notNull
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.io.File

@RunWith(AlchemyTestRunner::class)
@Repeat(25)
class FilesPlusTest
{

    private lateinit var file: File

    @Before
    fun setup()
    {
    }

    @After
    fun done()
    {
        file.delete()
    }

    @Test
    fun testMakeTempFile()
    {
        val extension = ".jpg" eitherOr ".txt"
        val name = StringGenerators.alphabeticStrings().get()
        file = FilesPlus.makeTempFile(name, extension)
        assertThat(file, notNull)

        val binary = BinaryGenerators.binary(1000).get()
        file.writeBytes(binary)
    }

    @Test
    fun testWriteStream()
    {
        val binary = BinaryGenerators.binary(1000).get()
        val stream = binary.inputStream()
        file = FilesPlus.makeTempFile()
        val bytes = file.writeStream(stream)
        assertThat(bytes, equalTo(binary.size.toLong()))

        val result = file.readBytes()
        Assert.assertArrayEquals(result, binary)
    }

}