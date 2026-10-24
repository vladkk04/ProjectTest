package com.klymiuk.scratchcard.navigation.base

import androidx.lifecycle.ViewModel
import com.klymiuk.scratchcard.core.navigation.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppNavigatorViewModel @Inject constructor(
    val navigator: Navigator,
) : ViewModel()
