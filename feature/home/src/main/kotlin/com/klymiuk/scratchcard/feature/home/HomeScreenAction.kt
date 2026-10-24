package com.klymiuk.scratchcard.feature.home

sealed interface HomeScreenAction {
    data object NavigateToScratch : HomeScreenAction
    data object NavigateToActivation : HomeScreenAction
}
