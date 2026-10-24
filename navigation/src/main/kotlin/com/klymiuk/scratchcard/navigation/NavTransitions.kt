package com.klymiuk.scratchcard.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith

private const val TRANSITION_DURATION_MILLIS = 350

private fun <T> slideSpec() = tween<T>(TRANSITION_DURATION_MILLIS, easing = FastOutSlowInEasing)

internal fun forwardTransition(): ContentTransform =
    slideInHorizontally(slideSpec()) { fullWidth -> fullWidth } togetherWith
        slideOutHorizontally(slideSpec()) { fullWidth -> -fullWidth }

internal fun backTransition(): ContentTransform =
    slideInHorizontally(slideSpec()) { fullWidth -> -fullWidth } togetherWith
        slideOutHorizontally(slideSpec()) { fullWidth -> fullWidth }
