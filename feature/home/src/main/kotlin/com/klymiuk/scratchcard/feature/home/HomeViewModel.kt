package com.klymiuk.scratchcard.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klymiuk.scratchcard.core.navigation.AppScreen
import com.klymiuk.scratchcard.core.navigation.Navigator
import com.klymiuk.scratchcard.domain.usecase.GetScratchCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    observeScratchCard: GetScratchCardUseCase,
    private val navigator: Navigator,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = observeScratchCard()
        .map(::HomeUiState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun onAction(action: HomeScreenAction) {
        when (action) {
            HomeScreenAction.NavigateToScratch -> navigator.navigate(AppScreen.Scratch)
            HomeScreenAction.NavigateToActivation -> navigator.navigate(AppScreen.Activation)
        }
    }
}
