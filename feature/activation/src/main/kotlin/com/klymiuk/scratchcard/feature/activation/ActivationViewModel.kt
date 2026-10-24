package com.klymiuk.scratchcard.feature.activation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.klymiuk.scratchcard.core.navigation.Navigator
import com.klymiuk.scratchcard.domain.model.ScratchCardException
import com.klymiuk.scratchcard.domain.usecase.ActivateScratchCardUseCase
import com.klymiuk.scratchcard.domain.usecase.GetScratchCardUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivationViewModel @Inject constructor(
    observeScratchCard: GetScratchCardUseCase,
    private val activateScratchCard: ActivateScratchCardUseCase,
    private val navigator: Navigator,
) : ViewModel() {

    private val requestState = MutableStateFlow(RequestState())

    val uiState: StateFlow<ActivationUiState> =
        combine(observeScratchCard(), requestState) { card, request ->
            ActivationUiState(card = card, isActivating = request.isActivating, error = request.error)
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ActivationUiState())

    fun onAction(action: ActivationScreenAction) {
        when (action) {
            ActivationScreenAction.Activate -> activate()
            ActivationScreenAction.DismissError -> dismissError()
            ActivationScreenAction.Back -> navigator.goBack()
        }
    }

    private fun activate() {
        if (requestState.value.isActivating) return

        viewModelScope.launch {
            requestState.update { it.copy(isActivating = true, error = null) }
            val error = activateScratchCard().exceptionOrNull()?.toActivationError()
            requestState.update { RequestState(isActivating = false, error = error) }
        }
    }

    private fun dismissError() {
        requestState.update { it.copy(error = null) }
    }

    private fun Throwable.toActivationError(): ActivationError = when (this) {
        is ScratchCardException.ActivationRejected -> ActivationError.Rejected(androidVersion)
        is ScratchCardException.IllegalCardState -> ActivationError.InvalidState
        is ScratchCardException.InvalidResponse -> ActivationError.InvalidResponse
        else -> ActivationError.Network
    }

    private data class RequestState(
        val isActivating: Boolean = false,
        val error: ActivationError? = null,
    )
}
