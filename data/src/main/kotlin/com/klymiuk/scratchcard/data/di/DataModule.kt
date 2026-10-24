package com.klymiuk.scratchcard.data.di

import com.klymiuk.scratchcard.data.generator.ScratchCodeGenerator
import com.klymiuk.scratchcard.data.generator.UuidScratchCodeGenerator
import com.klymiuk.scratchcard.data.repository.ActivationRepository
import com.klymiuk.scratchcard.data.repository.InMemoryScratchCardRepository
import com.klymiuk.scratchcard.data.repository.RemoteActivationRepository
import com.klymiuk.scratchcard.data.repository.ScratchCardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindScratchCardRepository(impl: InMemoryScratchCardRepository): ScratchCardRepository

    @Binds
    fun bindActivationRepository(impl: RemoteActivationRepository): ActivationRepository

    @Binds
    fun bindScratchCodeGenerator(impl: UuidScratchCodeGenerator): ScratchCodeGenerator
}
