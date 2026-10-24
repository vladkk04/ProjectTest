package com.klymiuk.scratchcard.core.testing.repository

import com.klymiuk.scratchcard.data.generator.ScratchCodeGenerator
import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class FakeScratchCodeGenerator(
    private val code: String = CODE,
    private val duration: Duration = 2.seconds,
) : ScratchCodeGenerator {

    var invocations = 0
        private set

    var completions = 0
        private set

    override suspend fun generate(): String {
        invocations++
        delay(duration)
        completions++
        return code
    }

    companion object {
        const val CODE = "0b4c1b1e-6a7d-4f0e-9c1a-3c1f2f6d9e01"
    }
}
