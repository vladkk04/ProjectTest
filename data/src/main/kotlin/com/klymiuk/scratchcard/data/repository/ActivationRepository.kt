package com.klymiuk.scratchcard.data.repository

interface ActivationRepository {

    suspend fun getAndroidVersion(code: String): Long
}
