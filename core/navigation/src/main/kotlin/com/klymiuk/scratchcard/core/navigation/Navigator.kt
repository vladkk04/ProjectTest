package com.klymiuk.scratchcard.core.navigation

interface Navigator {

    val backStack: List<AppScreen>

    fun navigate(screen: AppScreen)

    fun goBack()
}
