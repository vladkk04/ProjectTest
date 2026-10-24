package com.klymiuk.scratchcard.core.testing.repository

import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import com.klymiuk.scratchcard.data.repository.ScratchCardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeScratchCardRepository(
    initial: ScratchCardEntity = ScratchCardEntity.Unscratched,
) : ScratchCardRepository {

    private val card = MutableStateFlow(initial)

    override val scratchCard: StateFlow<ScratchCardEntity> = card

    val current: ScratchCardEntity get() = card.value

    fun set(new: ScratchCardEntity) {
        card.value = new
    }

    override suspend fun compareAndSet(expected: ScratchCardEntity, new: ScratchCardEntity): Boolean =
        card.compareAndSet(expected, new)
}
