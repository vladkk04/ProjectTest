package com.klymiuk.scratchcard.domain.model

sealed interface ScratchCard {

    data object Unscratched : ScratchCard

    data class Scratched(val code: String) : ScratchCard

    data class Activated(val code: String) : ScratchCard
}
