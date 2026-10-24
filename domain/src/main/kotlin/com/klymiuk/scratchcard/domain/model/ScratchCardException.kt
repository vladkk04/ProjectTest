package com.klymiuk.scratchcard.domain.model

sealed class ScratchCardException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {

    class IllegalCardState(val card: ScratchCard) :
        ScratchCardException("Operation is not allowed for card in state $card")

    class ActivationRejected(val androidVersion: Long) :
        ScratchCardException("Activation rejected, android version $androidVersion")

    class Network(cause: Throwable) :
        ScratchCardException("Activation service is unavailable", cause)

    class InvalidResponse(message: String, cause: Throwable? = null) :
        ScratchCardException(message, cause)
}
