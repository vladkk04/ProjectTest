package com.klymiuk.scratchcard.data.repository

import com.klymiuk.scratchcard.core.common.coroutines.AppDispatcher
import com.klymiuk.scratchcard.core.common.coroutines.Dispatcher
import com.klymiuk.scratchcard.data.remote.O2ApiService
import com.klymiuk.scratchcard.data.remote.RemoteException
import io.ktor.serialization.ContentConvertException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class RemoteActivationRepository @Inject constructor(
    private val apiService: O2ApiService,
    @param:Dispatcher(AppDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ActivationRepository {

    override suspend fun getAndroidVersion(code: String): Long = withContext(ioDispatcher) {
        val response = try {
            apiService.getVersion(code)
        } catch (e: CancellationException) {
            throw e
        } catch (e: ContentConvertException) {
            throw RemoteException.InvalidResponse("Malformed activation response", e)
        } catch (e: SerializationException) {
            throw RemoteException.InvalidResponse("Malformed activation response", e)
        } catch (e: Exception) {
            throw RemoteException.Network(e)
        }

        response.android.trim().toLongOrNull()
            ?: throw RemoteException.InvalidResponse("Unexpected android version '${response.android}'")
    }
}
