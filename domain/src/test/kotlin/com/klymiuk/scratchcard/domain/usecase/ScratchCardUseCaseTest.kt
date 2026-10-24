package com.klymiuk.scratchcard.domain.usecase

import com.klymiuk.scratchcard.core.testing.repository.FakeScratchCardRepository
import com.klymiuk.scratchcard.core.testing.repository.FakeScratchCodeGenerator
import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import com.klymiuk.scratchcard.domain.model.ScratchCard
import com.klymiuk.scratchcard.domain.model.ScratchCardException
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ScratchCardUseCaseTest {

    private val repository = FakeScratchCardRepository()
    private val generator = FakeScratchCodeGenerator()
    private val scratchCard = ScratchCardUseCase(repository, generator)

    @Test
    fun `reveals code and stores scratched card`() = runTest {
        val result = scratchCard()

        assertEquals(Result.success(ScratchCard.Scratched(FakeScratchCodeGenerator.CODE)), result)
        assertEquals(ScratchCardEntity.Scratched(FakeScratchCodeGenerator.CODE), repository.current)
        assertEquals(2_000L, currentTime)
    }

    @Test
    fun `card stays unscratched when cancelled before code is revealed`() = runTest {
        val job = launch { scratchCard() }

        advanceTimeBy(1_500L)
        job.cancel()
        advanceUntilIdle()

        assertTrue(job.isCancelled)
        assertEquals(ScratchCardEntity.Unscratched, repository.current)
        assertEquals(1, generator.invocations)
        assertEquals(0, generator.completions)
    }

    @Test
    fun `fails without generating code when card is already scratched`() = runTest {
        repository.set(ScratchCardEntity.Scratched("existing"))

        val error = scratchCard().exceptionOrNull()

        assertTrue(error is ScratchCardException.IllegalCardState)
        assertEquals(ScratchCard.Scratched("existing"), (error as ScratchCardException.IllegalCardState).card)
        assertEquals(0, generator.invocations)
        assertEquals(ScratchCardEntity.Scratched("existing"), repository.current)
    }

    @Test
    fun `fails when card is already activated`() = runTest {
        repository.set(ScratchCardEntity.Activated("existing"))

        val error = scratchCard().exceptionOrNull()

        assertTrue(error is ScratchCardException.IllegalCardState)
        assertEquals(ScratchCardEntity.Activated("existing"), repository.current)
    }
}
