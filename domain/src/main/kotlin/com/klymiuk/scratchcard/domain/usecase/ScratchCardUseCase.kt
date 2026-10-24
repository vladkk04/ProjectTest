package com.klymiuk.scratchcard.domain.usecase

import com.klymiuk.scratchcard.core.common.result.runSuspendCatching
import com.klymiuk.scratchcard.data.generator.ScratchCodeGenerator
import com.klymiuk.scratchcard.data.repository.ScratchCardRepository
import com.klymiuk.scratchcard.domain.mapper.toDomain
import com.klymiuk.scratchcard.domain.mapper.toEntity
import com.klymiuk.scratchcard.domain.model.ScratchCard
import com.klymiuk.scratchcard.domain.model.ScratchCardException
import kotlinx.coroutines.flow.first

fun interface ScratchCardUseCase : suspend () -> Result<ScratchCard.Scratched>

fun ScratchCardUseCase(
    scratchCardRepository: ScratchCardRepository,
    scratchCodeGenerator: ScratchCodeGenerator,
): ScratchCardUseCase = ScratchCardUseCase {
    runSuspendCatching {
        val card = scratchCardRepository.scratchCard.first().toDomain()
        if (card !is ScratchCard.Unscratched) throw ScratchCardException.IllegalCardState(card)

        val scratched = ScratchCard.Scratched(code = scratchCodeGenerator.generate())

        if (!scratchCardRepository.compareAndSet(expected = card.toEntity(), new = scratched.toEntity())) {
            throw ScratchCardException.IllegalCardState(scratchCardRepository.scratchCard.first().toDomain())
        }
        scratched
    }
}
