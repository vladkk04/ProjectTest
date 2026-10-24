package com.klymiuk.scratchcard.navigation.di

import com.klymiuk.scratchcard.navigation.base.AppNavigator
import com.klymiuk.scratchcard.core.navigation.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface NavigatorModule {

    @Binds
    fun bindNavigator(impl: AppNavigator): Navigator
}
