package com.klymiuk.scratchcard.core.testing.repository

import com.klymiuk.scratchcard.data.repository.ActivationRepository
import kotlinx.coroutines.delay
import kotlin.time.Duration

class FakeActivationRepository(
    var androidVersion: Long = 287_028L,
    var error: Throwable? = null,
    var duration: Duration = Duration.ZERO,
) : ActivationRepository {

    val requestedCodes = mutableListOf<String>()

    override suspend fun getAndroidVersion(code: String): Long {
        requestedCodes += code
        delay(duration)
        error?.let { throw it }
        return androidVersion
    }
}
