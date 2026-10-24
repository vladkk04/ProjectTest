package com.klymiuk.scratchcard.feature.home

import app.cash.turbine.test
import com.klymiuk.scratchcard.core.navigation.AppScreen
import com.klymiuk.scratchcard.core.testing.navigation.FakeNavigator
import com.klymiuk.scratchcard.core.testing.repository.FakeScratchCardRepository
import com.klymiuk.scratchcard.core.testing.rule.MainDispatcherRule
import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import com.klymiuk.scratchcard.domain.model.ScratchCard
import com.klymiuk.scratchcard.domain.usecase.GetScratchCardUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeScratchCardRepository()
    private val navigator = FakeNavigator()
    private val viewModel by lazy { HomeViewModel(GetScratchCardUseCase(repository), navigator) }

    @Test
    fun `always reflects current card state`() = runTest {
        viewModel.uiState.test {
            assertEquals(HomeUiState(ScratchCard.Unscratched), awaitItem())

            repository.set(ScratchCardEntity.Scratched(CODE))
            assertEquals(HomeUiState(ScratchCard.Scratched(CODE)), awaitItem())

            repository.set(ScratchCardEntity.Activated(CODE))
            assertEquals(HomeUiState(ScratchCard.Activated(CODE)), awaitItem())
        }
    }

    @Test
    fun `scratch action navigates to scratch screen`() {
        viewModel.onAction(HomeScreenAction.NavigateToScratch)

        assertEquals(listOf(AppScreen.Home, AppScreen.Scratch), navigator.backStack)
    }

    @Test
    fun `activation action navigates to activation screen`() {
        viewModel.onAction(HomeScreenAction.NavigateToActivation)

        assertEquals(listOf(AppScreen.Home, AppScreen.Activation), navigator.backStack)
    }

    private companion object {
        const val CODE = "code"
    }
}
