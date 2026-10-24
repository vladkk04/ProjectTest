package com.klymiuk.scratchcard.domain.usecase

import com.klymiuk.scratchcard.core.testing.coroutines.testApplicationScope
import com.klymiuk.scratchcard.core.testing.repository.FakeActivationRepository
import com.klymiuk.scratchcard.core.testing.repository.FakeScratchCardRepository
import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import com.klymiuk.scratchcard.data.remote.RemoteException
import com.klymiuk.scratchcard.domain.model.ScratchCard
import com.klymiuk.scratchcard.domain.model.ScratchCardException
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

class ActivateScratchCardUseCaseTest {

    private val scratched = ScratchCardEntity.Scratched(CODE)
    private val cardRepository = FakeScratchCardRepository(initial = scratched)
    private val activationRepository = FakeActivationRepository()

    private fun TestScope.activateScratchCard() = ActivateScratchCardUseCase(
        scratchCardRepository = cardRepository,
        activationRepository = activationRepository,
        applicationScope = testApplicationScope(),
    )

    @Test
    fun `activates card when android version is greater than threshold`() = runTest {
        activationRepository.androidVersion = 287_028L

        val result = activateScratchCard().invoke()

        assertEquals(Result.success(ScratchCard.Activated(CODE)), result)
        assertEquals(ScratchCardEntity.Activated(CODE), cardRepository.current)
        assertEquals(listOf(CODE), activationRepository.requestedCodes)
    }

    @Test
    fun `activates card when android version is one above threshold`() = runTest {
        activationRepository.androidVersion = 277_029L

        val result = activateScratchCard().invoke()

        assertTrue(result.isSuccess)
        assertEquals(ScratchCardEntity.Activated(CODE), cardRepository.current)
    }

    @Test
    fun `rejects activation when android version equals threshold`() = runTest {
        activationRepository.androidVersion = 277_028L

        val error = activateScratchCard().invoke().exceptionOrNull()

        assertTrue(error is ScratchCardException.ActivationRejected)
        assertEquals(277_028L, (error as ScratchCardException.ActivationRejected).androidVersion)
        assertEquals(scratched, cardRepository.current)
    }

    @Test
    fun `rejects activation when android version is lower than threshold`() = runTest {
        activationRepository.androidVersion = 1L

        val error = activateScratchCard().invoke().exceptionOrNull()

        assertTrue(error is ScratchCardException.ActivationRejected)
        assertEquals(scratched, cardRepository.current)
    }

    @Test
    fun `fails without calling service when card is not scratched`() = runTest {
        cardRepository.set(ScratchCardEntity.Unscratched)

        val error = activateScratchCard().invoke().exceptionOrNull()

        assertTrue(error is ScratchCardException.IllegalCardState)
        assertTrue(activationRepository.requestedCodes.isEmpty())
        assertEquals(ScratchCardEntity.Unscratched, cardRepository.current)
    }

    @Test
    fun `fails without calling service when card is already activated`() = runTest {
        cardRepository.set(ScratchCardEntity.Activated(CODE))

        val error = activateScratchCard().invoke().exceptionOrNull()

        assertTrue(error is ScratchCardException.IllegalCardState)
        assertTrue(activationRepository.requestedCodes.isEmpty())
    }

    @Test
    fun `maps network failure and keeps card scratched`() = runTest {
        val networkError = RemoteException.Network(RuntimeException("offline"))
        activationRepository.error = networkError

        val error = activateScratchCard().invoke().exceptionOrNull()

        assertTrue(error is ScratchCardException.Network)
        assertTrue(error?.cause === networkError)
        assertEquals(scratched, cardRepository.current)
    }

    @Test
    fun `maps invalid response and keeps card scratched`() = runTest {
        activationRepository.error = RemoteException.InvalidResponse("bad body")

        val error = activateScratchCard().invoke().exceptionOrNull()

        assertTrue(error is ScratchCardException.InvalidResponse)
        assertEquals(scratched, cardRepository.current)
    }

    @Test
    fun `activation completes even when caller is cancelled`() = runTest {
        activationRepository.duration = 1.seconds
        val activate = activateScratchCard()

        val caller = launch { activate() }
        advanceTimeBy(500L)
        caller.cancel()
        advanceUntilIdle()

        assertTrue(caller.isCancelled)
        assertEquals(ScratchCardEntity.Activated(CODE), cardRepository.current)
    }

    private companion object {
        const val CODE = "6f1c2e9a-0d1b-4a52-8f7e-2b9d4c3a1e55"
    }
}
