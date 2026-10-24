package com.klymiuk.scratchcard.feature.activation

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klymiuk.scratchcard.core.navigation.AppScreen
import com.klymiuk.scratchcard.core.testing.coroutines.testApplicationScope
import com.klymiuk.scratchcard.core.testing.navigation.FakeNavigator
import com.klymiuk.scratchcard.core.testing.repository.FakeActivationRepository
import com.klymiuk.scratchcard.core.testing.repository.FakeScratchCardRepository
import com.klymiuk.scratchcard.core.testing.rule.MainDispatcherRule
import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import com.klymiuk.scratchcard.data.remote.RemoteException
import com.klymiuk.scratchcard.domain.model.ScratchCard
import com.klymiuk.scratchcard.domain.usecase.ActivateScratchCardUseCase
import com.klymiuk.scratchcard.domain.usecase.GetScratchCardUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
class ActivationViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scratched = ScratchCard.Scratched(CODE)
    private val cardRepository = FakeScratchCardRepository(initial = ScratchCardEntity.Scratched(CODE))
    private val activationRepository = FakeActivationRepository(duration = 1.seconds)
    private val navigator = FakeNavigator().apply { navigate(AppScreen.Activation) }

    private val viewModelStore = ViewModelStore()

    private fun TestScope.createViewModel(): ActivationViewModel = ViewModelProvider.create(
        store = viewModelStore,
        factory = viewModelFactory {
            initializer {
                ActivationViewModel(
                    observeScratchCard = GetScratchCardUseCase(cardRepository),
                    activateScratchCard = ActivateScratchCardUseCase(
                        scratchCardRepository = cardRepository,
                        activationRepository = activationRepository,
                        applicationScope = testApplicationScope(),
                    ),
                    navigator = navigator,
                )
            }
        },
    )[ActivationViewModel::class]

    private fun TestScope.collectUiState(viewModel: ActivationViewModel) {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        runCurrent()
    }

    @Test
    fun `successful activation activates the card`() = runTest {
        val viewModel = createViewModel()
        collectUiState(viewModel)

        viewModel.onAction(ActivationScreenAction.Activate)
        runCurrent()
        assertTrue(viewModel.uiState.value.isActivating)
        assertFalse(viewModel.uiState.value.canActivate)

        advanceUntilIdle()

        assertEquals(ActivationUiState(card = ScratchCard.Activated(CODE)), viewModel.uiState.value)
        assertEquals(listOf(CODE), activationRepository.requestedCodes)
    }

    @Test
    fun `rejected activation shows error and keeps card scratched`() = runTest {
        activationRepository.androidVersion = 277_028L
        val viewModel = createViewModel()
        collectUiState(viewModel)

        viewModel.onAction(ActivationScreenAction.Activate)
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(ActivationError.Rejected(277_028L), state.error)
        assertEquals(scratched, state.card)
        assertFalse(state.isActivating)
        assertTrue(state.canActivate)
    }

    @Test
    fun `dismissing error hides it`() = runTest {
        activationRepository.androidVersion = 0L
        val viewModel = createViewModel()
        collectUiState(viewModel)
        viewModel.onAction(ActivationScreenAction.Activate)
        advanceUntilIdle()

        viewModel.onAction(ActivationScreenAction.DismissError)
        runCurrent()

        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `network failure shows network error`() = runTest {
        activationRepository.error = RemoteException.Network(IOException("offline"))
        val viewModel = createViewModel()
        collectUiState(viewModel)

        viewModel.onAction(ActivationScreenAction.Activate)
        advanceUntilIdle()

        assertEquals(ActivationError.Network, viewModel.uiState.value.error)
        assertEquals(ScratchCardEntity.Scratched(CODE), cardRepository.current)
    }

    @Test
    fun `invalid response shows invalid response error`() = runTest {
        activationRepository.error = RemoteException.InvalidResponse("bad body")
        val viewModel = createViewModel()
        collectUiState(viewModel)

        viewModel.onAction(ActivationScreenAction.Activate)
        advanceUntilIdle()

        assertEquals(ActivationError.InvalidResponse, viewModel.uiState.value.error)
    }

    @Test
    fun `closing the screen does not cancel activation`() = runTest {
        val viewModel = createViewModel()
        collectUiState(viewModel)

        viewModel.onAction(ActivationScreenAction.Activate)
        advanceTimeBy(500L)
        viewModelStore.clear()
        advanceUntilIdle()

        assertEquals(ScratchCardEntity.Activated(CODE), cardRepository.current)
    }

    @Test
    fun `back during activation navigates back and activation still completes`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(ActivationScreenAction.Activate)
        runCurrent()
        viewModel.onAction(ActivationScreenAction.Back)
        viewModelStore.clear()
        advanceUntilIdle()

        assertEquals(listOf(AppScreen.Home), navigator.backStack)
        assertEquals(ScratchCardEntity.Activated(CODE), cardRepository.current)
    }

    @Test
    fun `activation is not possible for unscratched card`() = runTest {
        cardRepository.set(ScratchCardEntity.Unscratched)
        val viewModel = createViewModel()
        collectUiState(viewModel)

        assertFalse(viewModel.uiState.value.canActivate)
    }

    @Test
    fun `repeated clicks send a single request`() = runTest {
        val viewModel = createViewModel()

        viewModel.onAction(ActivationScreenAction.Activate)
        runCurrent()
        viewModel.onAction(ActivationScreenAction.Activate)
        advanceUntilIdle()

        assertEquals(1, activationRepository.requestedCodes.size)
    }

    private companion object {
        const val CODE = "6f1c2e9a-0d1b-4a52-8f7e-2b9d4c3a1e55"
    }
}
