package com.klymiuk.scratchcard.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppScreen : NavKey {

    @Serializable
    data object Home : AppScreen

    @Serializable
    data object Scratch : AppScreen

    @Serializable
    data object Activation : AppScreen
}
