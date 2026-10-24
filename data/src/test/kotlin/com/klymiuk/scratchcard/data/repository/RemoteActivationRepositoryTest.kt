package com.klymiuk.scratchcard.data.repository

import com.klymiuk.scratchcard.core.network.HttpClientFactory
import com.klymiuk.scratchcard.core.network.NetworkJson
import com.klymiuk.scratchcard.data.remote.O2ApiService
import com.klymiuk.scratchcard.data.remote.RemoteException
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class RemoteActivationRepositoryTest {

    private val requests = mutableListOf<HttpRequestData>()

    private fun TestScope.repository(handler: MockRequestHandler): RemoteActivationRepository {
        val engine = MockEngine { request ->
            requests += request
            handler(request)
        }
        val client = HttpClientFactory.create(engine = engine, json = NetworkJson)
        return RemoteActivationRepository(
            apiService = O2ApiService(client),
            ioDispatcher = StandardTestDispatcher(testScheduler),
        )
    }

    private fun json(body: String, status: HttpStatusCode = HttpStatusCode.OK): MockRequestHandler = {
        respond(body, status, headersOf(HttpHeaders.ContentType, "application/json"))
    }

    @Test
    fun `sends code as query parameter of GET version request`() = runTest {
        repository(json("""{ "android": "287028" }""")).getAndroidVersion(CODE)

        val request = requests.single()
        assertEquals(HttpMethod.Get, request.method)
        assertEquals("https://api.o2.sk/version?code=$CODE", request.url.toString())
    }

    @Test
    fun `parses android version and ignores other fields`() = runTest {
        val body = """{ "ios": "6.52.2", "android": "287028", "androidTM": "271780" }"""

        val version = repository(json(body)).getAndroidVersion(CODE)

        assertEquals(287_028L, version)
    }

    @Test
    fun `maps http error to network exception`() = runTest {
        val error = runCatching {
            repository(json("{}", HttpStatusCode.InternalServerError)).getAndroidVersion(CODE)
        }.exceptionOrNull()

        assertTrue(error is RemoteException.Network)
    }

    @Test
    fun `maps connection failure to network exception`() = runTest {
        val error = runCatching {
            repository { throw IOException("no connection") }.getAndroidVersion(CODE)
        }.exceptionOrNull()

        assertTrue(error is RemoteException.Network)
    }

    @Test
    fun `maps non numeric android version to invalid response`() = runTest {
        val error = runCatching {
            repository(json("""{ "android": "6.52.2" }""")).getAndroidVersion(CODE)
        }.exceptionOrNull()

        assertTrue(error is RemoteException.InvalidResponse)
    }

    @Test
    fun `maps missing android field to invalid response`() = runTest {
        val error = runCatching {
            repository(json("""{ "ios": "6.52.2" }""")).getAndroidVersion(CODE)
        }.exceptionOrNull()

        assertTrue(error is RemoteException.InvalidResponse)
    }

    private companion object {
        const val CODE = "6f1c2e9a-0d1b-4a52-8f7e-2b9d4c3a1e55"
    }
}
