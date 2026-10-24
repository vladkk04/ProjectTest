package com.klymiuk.scratchcard.data.repository

import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import kotlinx.coroutines.flow.Flow

interface ScratchCardRepository {

    val scratchCard: Flow<ScratchCardEntity>

    suspend fun compareAndSet(expected: ScratchCardEntity, new: ScratchCardEntity): Boolean
}
