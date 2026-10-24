package com.klymiuk.scratchcard.domain.di

import com.klymiuk.scratchcard.core.common.coroutines.ApplicationScope
import com.klymiuk.scratchcard.data.generator.ScratchCodeGenerator
import com.klymiuk.scratchcard.data.repository.ActivationRepository
import com.klymiuk.scratchcard.data.repository.ScratchCardRepository
import com.klymiuk.scratchcard.domain.usecase.ActivateScratchCardUseCase
import com.klymiuk.scratchcard.domain.usecase.GetScratchCardUseCase
import com.klymiuk.scratchcard.domain.usecase.ScratchCardUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetScratchCardUseCase(
        scratchCardRepository: ScratchCardRepository,
    ): GetScratchCardUseCase = GetScratchCardUseCase(scratchCardRepository)

    @Provides
    fun provideScratchCardUseCase(
        scratchCardRepository: ScratchCardRepository,
        scratchCodeGenerator: ScratchCodeGenerator,
    ): ScratchCardUseCase = ScratchCardUseCase(scratchCardRepository, scratchCodeGenerator)

    @Provides
    fun provideActivateScratchCardUseCase(
        scratchCardRepository: ScratchCardRepository,
        activationRepository: ActivationRepository,
        @ApplicationScope applicationScope: CoroutineScope,
    ): ActivateScratchCardUseCase = ActivateScratchCardUseCase(
        scratchCardRepository = scratchCardRepository,
        activationRepository = activationRepository,
        applicationScope = applicationScope,
    )
}
