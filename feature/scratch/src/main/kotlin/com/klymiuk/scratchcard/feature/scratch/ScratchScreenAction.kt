package com.klymiuk.scratchcard.feature.scratch

sealed interface ScratchScreenAction {
    data object Scratch : ScratchScreenAction
    data object Back : ScratchScreenAction
}
