package com.klymiuk.scratchcard.feature.home

import androidx.compose.runtime.Immutable
import com.klymiuk.scratchcard.domain.model.ScratchCard

@Immutable
data class HomeUiState(
    val card: ScratchCard = ScratchCard.Unscratched,
)
