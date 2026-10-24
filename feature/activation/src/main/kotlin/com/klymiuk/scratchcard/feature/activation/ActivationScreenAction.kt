package com.klymiuk.scratchcard.feature.activation

sealed interface ActivationScreenAction {
    data object Activate : ActivationScreenAction
    data object DismissError : ActivationScreenAction
    data object Back : ActivationScreenAction
}
