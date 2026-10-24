package com.klymiuk.scratchcard.navigation.base

import com.klymiuk.scratchcard.core.navigation.AppScreen
import org.junit.Assert.assertEquals
import org.junit.Test

class AppNavigatorTest {

    private val navigator = AppNavigator()

    @Test
    fun `starts on home screen`() {
        assertEquals(listOf(AppScreen.Home), navigator.backStack)
    }

    @Test
    fun `navigate pushes screen`() {
        navigator.navigate(AppScreen.Activation)

        assertEquals(listOf(AppScreen.Home, AppScreen.Activation), navigator.backStack)
    }

    @Test
    fun `navigate ignores screen already on top`() {
        navigator.navigate(AppScreen.Scratch)
        navigator.navigate(AppScreen.Scratch)

        assertEquals(listOf(AppScreen.Home, AppScreen.Scratch), navigator.backStack)
    }

    @Test
    fun `goBack pops top screen`() {
        navigator.navigate(AppScreen.Scratch)

        navigator.goBack()

        assertEquals(listOf(AppScreen.Home), navigator.backStack)
    }

    @Test
    fun `goBack keeps start screen`() {
        navigator.goBack()

        assertEquals(listOf(AppScreen.Home), navigator.backStack)
    }
}
