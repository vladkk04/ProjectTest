package com.klymiuk.scratchcard.feature.scratch

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.klymiuk.scratchcard.core.navigation.AppScreen
import com.klymiuk.scratchcard.core.testing.navigation.FakeNavigator
import com.klymiuk.scratchcard.core.testing.repository.FakeScratchCardRepository
import com.klymiuk.scratchcard.core.testing.repository.FakeScratchCodeGenerator
import com.klymiuk.scratchcard.core.testing.rule.MainDispatcherRule
import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import com.klymiuk.scratchcard.domain.model.ScratchCard
import com.klymiuk.scratchcard.domain.usecase.GetScratchCardUseCase
import com.klymiuk.scratchcard.domain.usecase.ScratchCardUseCase
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
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ScratchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeScratchCardRepository()
    private val generator = FakeScratchCodeGenerator()
    private val navigator = FakeNavigator().apply { navigate(AppScreen.Scratch) }

    private val viewModelStore = ViewModelStore()

    private val viewModel: ScratchViewModel by lazy {
        ViewModelProvider.create(
        store = viewModelStore,
        factory = viewModelFactory {
            initializer {
                ScratchViewModel(
                    observeScratchCard = GetScratchCardUseCase(repository),
                    scratchCard = ScratchCardUseCase(repository, generator),
                    navigator = navigator,
                )
            }
        },
        )[ScratchViewModel::class]
    }

    private fun TestScope.collectUiState() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
        runCurrent()
    }

    @Test
    fun `scratch reveals code after two seconds`() = runTest {
        collectUiState()

        viewModel.onAction(ScratchScreenAction.Scratch)
        runCurrent()
        assertEquals(ScratchUiState(ScratchCard.Unscratched, isScratching = true), viewModel.uiState.value)
        assertFalse(viewModel.uiState.value.canScratch)

        advanceTimeBy(2_001L)

        val code = FakeScratchCodeGenerator.CODE
        assertEquals(ScratchUiState(ScratchCard.Scratched(code), isScratching = false), viewModel.uiState.value)
        assertEquals(ScratchCardEntity.Scratched(code), repository.current)
    }

    @Test
    fun `closing the screen before completion cancels scratching`() = runTest {
        collectUiState()

        viewModel.onAction(ScratchScreenAction.Scratch)
        advanceTimeBy(1_000L)
        viewModelStore.clear()
        advanceUntilIdle()

        assertEquals(ScratchCardEntity.Unscratched, repository.current)
        assertEquals(0, generator.completions)
    }

    @Test
    fun `repeated clicks start a single scratch`() = runTest {
        viewModel.onAction(ScratchScreenAction.Scratch)
        viewModel.onAction(ScratchScreenAction.Scratch)
        advanceUntilIdle()

        assertEquals(1, generator.invocations)
    }

    @Test
    fun `scratch is not possible for already scratched card`() = runTest {
        repository.set(ScratchCardEntity.Scratched("existing"))
        collectUiState()

        assertFalse(viewModel.uiState.value.canScratch)

        viewModel.onAction(ScratchScreenAction.Scratch)
        advanceUntilIdle()

        assertEquals(0, generator.invocations)
        assertEquals(ScratchCardEntity.Scratched("existing"), repository.current)
    }

    @Test
    fun `back action navigates back`() {
        viewModel.onAction(ScratchScreenAction.Back)

        assertEquals(listOf(AppScreen.Home), navigator.backStack)
    }

    @Test
    fun `scratch is possible for unscratched card`() = runTest {
        collectUiState()

        assertTrue(viewModel.uiState.value.canScratch)
    }
}
