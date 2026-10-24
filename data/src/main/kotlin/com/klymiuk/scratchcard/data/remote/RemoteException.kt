package com.klymiuk.scratchcard.data.remote

sealed class RemoteException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {

    class Network(cause: Throwable) :
        RemoteException("Service is unavailable", cause)

    class InvalidResponse(message: String, cause: Throwable? = null) :
        RemoteException(message, cause)
}
