package com.klymiuk.scratchcard.feature.scratch

import androidx.compose.runtime.Immutable
import com.klymiuk.scratchcard.domain.model.ScratchCard

@Immutable
data class ScratchUiState(
    val card: ScratchCard = ScratchCard.Unscratched,
    val isScratching: Boolean = false,
) {
    val canScratch: Boolean get() = card is ScratchCard.Unscratched && !isScratching
}
