package com.klymiuk.scratchcard.feature.scratch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klymiuk.scratchcard.core.navigation.Navigator
import com.klymiuk.scratchcard.domain.usecase.GetScratchCardUseCase
import com.klymiuk.scratchcard.domain.usecase.ScratchCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScratchViewModel @Inject constructor(
    observeScratchCard: GetScratchCardUseCase,
    private val scratchCard: ScratchCardUseCase,
    private val navigator: Navigator,
) : ViewModel() {

    private val isScratching = MutableStateFlow(false)

    val uiState: StateFlow<ScratchUiState> =
        combine(observeScratchCard(), isScratching, ::ScratchUiState)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ScratchUiState())

    private var scratchJob: Job? = null

    fun onAction(action: ScratchScreenAction) {
        when (action) {
            ScratchScreenAction.Scratch -> scratch()
            ScratchScreenAction.Back -> navigator.goBack()
        }
    }

    private fun scratch() {
        if (scratchJob?.isActive == true) return

        scratchJob = viewModelScope.launch {
            isScratching.value = true
            try {
                scratchCard()
            } finally {
                isScratching.value = false
            }
        }
    }
}
