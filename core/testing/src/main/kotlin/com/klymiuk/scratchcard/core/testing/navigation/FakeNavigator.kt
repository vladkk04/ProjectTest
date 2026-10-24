package com.klymiuk.scratchcard.core.testing.navigation

import com.klymiuk.scratchcard.core.navigation.AppScreen
import com.klymiuk.scratchcard.core.navigation.Navigator

class FakeNavigator(
    startScreen: AppScreen = AppScreen.Home,
) : Navigator {

    private val screens = mutableListOf(startScreen)

    override val backStack: List<AppScreen> get() = screens.toList()

    override fun navigate(screen: AppScreen) {
        screens += screen
    }

    override fun goBack() {
        screens.removeLastOrNull()
    }
}
