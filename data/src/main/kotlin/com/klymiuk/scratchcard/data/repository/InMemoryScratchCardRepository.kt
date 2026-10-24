package com.klymiuk.scratchcard.data.repository

import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryScratchCardRepository @Inject constructor() : ScratchCardRepository {

    private val card = MutableStateFlow<ScratchCardEntity>(ScratchCardEntity.Unscratched)

    override val scratchCard: StateFlow<ScratchCardEntity> = card.asStateFlow()

    override suspend fun compareAndSet(expected: ScratchCardEntity, new: ScratchCardEntity): Boolean =
        card.compareAndSet(expected, new)
}
