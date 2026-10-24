package com.klymiuk.scratchcard.feature.activation

import androidx.compose.runtime.Immutable
import com.klymiuk.scratchcard.domain.model.ScratchCard

@Immutable
data class ActivationUiState(
    val card: ScratchCard = ScratchCard.Unscratched,
    val isActivating: Boolean = false,
    val error: ActivationError? = null,
) {
    val canActivate: Boolean get() = card is ScratchCard.Scratched && !isActivating
}

@Immutable
sealed interface ActivationError {
    data class Rejected(val androidVersion: Long) : ActivationError
    data object Network : ActivationError
    data object InvalidResponse : ActivationError
    data object InvalidState : ActivationError
}
