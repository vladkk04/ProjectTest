package com.klymiuk.scratchcard.domain.usecase

import com.klymiuk.scratchcard.data.repository.ScratchCardRepository
import com.klymiuk.scratchcard.domain.mapper.toDomain
import com.klymiuk.scratchcard.domain.model.ScratchCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun interface GetScratchCardUseCase : () -> Flow<ScratchCard>

fun GetScratchCardUseCase(
    scratchCardRepository: ScratchCardRepository,
): GetScratchCardUseCase = GetScratchCardUseCase {
    scratchCardRepository.scratchCard.map { it.toDomain() }
}
