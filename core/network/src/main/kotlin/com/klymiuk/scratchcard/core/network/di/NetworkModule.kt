package com.klymiuk.scratchcard.core.network.di

import com.klymiuk.scratchcard.core.network.BuildConfig
import com.klymiuk.scratchcard.core.network.HttpClientFactory
import com.klymiuk.scratchcard.core.network.NetworkJson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.Logger
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = NetworkJson

    @Provides
    @Singleton
    fun provideHttpClient(json: Json): HttpClient = HttpClientFactory.create(
        engine = OkHttp.create(),
        json = json,
        logger = if (BuildConfig.DEBUG) Logger.ANDROID else null,
    )
}
