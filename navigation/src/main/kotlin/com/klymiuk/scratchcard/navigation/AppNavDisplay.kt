package com.klymiuk.scratchcard.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.klymiuk.scratchcard.core.navigation.AppScreen
import com.klymiuk.scratchcard.feature.activation.ActivationScreen
import com.klymiuk.scratchcard.feature.home.HomeScreen
import com.klymiuk.scratchcard.feature.scratch.ScratchScreen
import com.klymiuk.scratchcard.navigation.base.AppNavigatorViewModel

@Composable
fun AppNavDisplay(
    modifier: Modifier = Modifier,
) {
    val viewModel = hiltViewModel<AppNavigatorViewModel>()
    val navigator = viewModel.navigator

    NavDisplay(
        backStack = navigator.backStack,
        onBack = navigator::goBack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = { forwardTransition() },
        popTransitionSpec = { backTransition() },
        predictivePopTransitionSpec = { backTransition() },
        entryProvider = entryProvider {
            entry<AppScreen.Home> { HomeScreen() }
            entry<AppScreen.Scratch> { ScratchScreen() }
            entry<AppScreen.Activation> { ActivationScreen() }
        },
        modifier = modifier,
    )
}
