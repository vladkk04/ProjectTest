package com.klymiuk.scratchcard.data.remote

import com.klymiuk.scratchcard.data.remote.model.VersionResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class O2ApiService @Inject constructor(
    private val httpClient: HttpClient,
) {
    suspend fun getVersion(code: String): VersionResponse =
        httpClient.get(VERSION_URL) {
            parameter(PARAM_CODE, code)
        }.body()

    companion object {
        const val VERSION_URL = "https://api.o2.sk/version"
        const val PARAM_CODE = "code"
    }
}
