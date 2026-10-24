package com.klymiuk.scratchcard.data.model

sealed interface ScratchCardEntity {

    data object Unscratched : ScratchCardEntity

    data class Scratched(val code: String) : ScratchCardEntity

    data class Activated(val code: String) : ScratchCardEntity
}
