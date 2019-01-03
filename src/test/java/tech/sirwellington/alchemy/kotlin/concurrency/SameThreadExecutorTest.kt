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

package tech.sirwellington.alchemy.kotlin.concurrency

import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import tech.sirwellington.alchemy.test.junit.runners.AlchemyTestRunner
import tech.sirwellington.alchemy.test.junit.runners.Repeat
import java.util.concurrent.Executor

@RunWith(AlchemyTestRunner::class)
@Repeat(25)
class SameThreadExecutorTest
{

    private lateinit var instance: Executor

    @Mock
    private lateinit var task: Runnable

    @Before
    fun setup()
    {
        instance = SameThreadExecutor()
    }

    @Test
    fun testExecuteWithTask()
    {
        instance.execute(task)
        verify(task).run()
    }

    @Test
    fun testExecuteWithNull()
    {
        instance.execute(null)
    }

}