package com.klymiuk.scratchcard.domain.mapper

import com.klymiuk.scratchcard.data.model.ScratchCardEntity
import com.klymiuk.scratchcard.data.remote.RemoteException
import com.klymiuk.scratchcard.domain.model.ScratchCard
import com.klymiuk.scratchcard.domain.model.ScratchCardException

internal fun ScratchCardEntity.toDomain(): ScratchCard = when (this) {
    ScratchCardEntity.Unscratched -> ScratchCard.Unscratched
    is ScratchCardEntity.Scratched -> ScratchCard.Scratched(code)
    is ScratchCardEntity.Activated -> ScratchCard.Activated(code)
}

internal fun ScratchCard.toEntity(): ScratchCardEntity = when (this) {
    ScratchCard.Unscratched -> ScratchCardEntity.Unscratched
    is ScratchCard.Scratched -> ScratchCardEntity.Scratched(code)
    is ScratchCard.Activated -> ScratchCardEntity.Activated(code)
}

internal fun RemoteException.toDomain(): ScratchCardException = when (this) {
    is RemoteException.Network -> ScratchCardException.Network(this)
    is RemoteException.InvalidResponse -> ScratchCardException.InvalidResponse(message.orEmpty(), this)
}
