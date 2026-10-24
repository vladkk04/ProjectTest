package com.klymiuk.scratchcard.core.common.result

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.coroutines.cancellation.CancellationException

class SuspendRunCatchingTest {

    @Test
    fun `wraps value in success`() {
        assertEquals(Result.success(42), runSuspendCatching { 42 })
    }

    @Test
    fun `wraps exception in failure`() {
        val error = IllegalStateException("boom")

        val result = runSuspendCatching { throw error }

        assertTrue(result.exceptionOrNull() === error)
    }

    @Test(expected = CancellationException::class)
    fun `rethrows cancellation`() {
        runSuspendCatching { throw CancellationException("cancelled") }
    }
}
