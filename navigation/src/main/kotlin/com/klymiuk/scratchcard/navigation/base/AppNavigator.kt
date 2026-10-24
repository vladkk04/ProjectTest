package com.klymiuk.scratchcard.navigation.base

import androidx.navigation3.runtime.NavBackStack
import com.klymiuk.scratchcard.core.navigation.AppScreen
import com.klymiuk.scratchcard.core.navigation.Navigator
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppNavigator @Inject constructor() : Navigator {

    private val _backStack = NavBackStack<AppScreen>(AppScreen.Home)
    override val backStack: List<AppScreen> = _backStack

    override fun navigate(screen: AppScreen) {
        if (_backStack.lastOrNull() != screen) _backStack.add(screen)
    }

    override fun goBack() {
        if (_backStack.size > 1) _backStack.removeLastOrNull()
    }
}
