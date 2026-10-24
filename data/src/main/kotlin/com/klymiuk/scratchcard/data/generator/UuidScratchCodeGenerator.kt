package com.klymiuk.scratchcard.data.generator

import com.klymiuk.scratchcard.core.common.coroutines.AppDispatcher
import com.klymiuk.scratchcard.core.common.coroutines.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class UuidScratchCodeGenerator @Inject constructor(
    @param:Dispatcher(AppDispatcher.Default) private val defaultDispatcher: CoroutineDispatcher,
) : ScratchCodeGenerator {

    override suspend fun generate(): String = withContext(defaultDispatcher) {
        delay(DURATION)
        UUID.randomUUID().toString()
    }

    private companion object {
        val DURATION = 2.seconds
    }
}
