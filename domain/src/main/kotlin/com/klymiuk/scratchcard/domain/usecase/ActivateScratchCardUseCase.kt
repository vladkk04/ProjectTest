package com.klymiuk.scratchcard.domain.usecase

import com.klymiuk.scratchcard.core.common.result.runSuspendCatching
import com.klymiuk.scratchcard.data.remote.RemoteException
import com.klymiuk.scratchcard.data.repository.ActivationRepository
import com.klymiuk.scratchcard.data.repository.ScratchCardRepository
import com.klymiuk.scratchcard.domain.mapper.toDomain
import com.klymiuk.scratchcard.domain.mapper.toEntity
import com.klymiuk.scratchcard.domain.model.ScratchCard
import com.klymiuk.scratchcard.domain.model.ScratchCardException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first

fun interface ActivateScratchCardUseCase : suspend () -> Result<ScratchCard.Activated> {

    companion object {
        const val MIN_ANDROID_VERSION_EXCLUSIVE = 277_028L
    }
}

fun ActivateScratchCardUseCase(
    scratchCardRepository: ScratchCardRepository,
    activationRepository: ActivationRepository,
    applicationScope: CoroutineScope,
): ActivateScratchCardUseCase = ActivateScratchCardUseCase {
    applicationScope.async {
        runSuspendCatching {
            val card = scratchCardRepository.scratchCard.first().toDomain()
            if (card !is ScratchCard.Scratched) throw ScratchCardException.IllegalCardState(card)

            val androidVersion = try {
                activationRepository.getAndroidVersion(card.code)
            } catch (e: RemoteException) {
                throw e.toDomain()
            }
            if (androidVersion <= ActivateScratchCardUseCase.MIN_ANDROID_VERSION_EXCLUSIVE) {
                throw ScratchCardException.ActivationRejected(androidVersion)
            }

            val activated = ScratchCard.Activated(code = card.code)
            if (!scratchCardRepository.compareAndSet(expected = card.toEntity(), new = activated.toEntity())) {
                throw ScratchCardException.IllegalCardState(scratchCardRepository.scratchCard.first().toDomain())
            }
            activated
        }
    }.await()
}
