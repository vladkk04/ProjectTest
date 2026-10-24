package com.klymiuk.scratchcard.data.generator

import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class UuidScratchCodeGeneratorTest {

    @Test
    fun `generates random uuid after two seconds`() = runTest {
        val generator = UuidScratchCodeGenerator(StandardTestDispatcher(testScheduler))

        val first = generator.generate()

        assertEquals(2_000L, currentTime)
        assertEquals(first, UUID.fromString(first).toString())
        assertTrue(first != generator.generate())
    }

    @Test
    fun `generation is cancellable before it completes`() = runTest {
        val generator = UuidScratchCodeGenerator(StandardTestDispatcher(testScheduler))

        val code = async { generator.generate() }
        advanceTimeBy(1_999L)
        runCurrent()
        assertFalse(code.isCompleted)

        code.cancel()
        runCurrent()
        assertTrue(code.isCancelled)
    }
}
