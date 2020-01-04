package app.perdana.indonesia.data.repository

import app.perdana.indonesia.core.utils.NetworkConfig
import app.perdana.indonesia.data.remote.api.PresenceApiService
import app.perdana.indonesia.data.remote.model.PresenceContainerRequest
import app.perdana.indonesia.data.remote.model.PresenceContainerResponse
import app.perdana.indonesia.data.remote.model.PresenceItem
import retrofit2.Response

/**
 * Created by ebysofyan on 12/2/19.
 */

class PresenceApiRepository {
    companion object {
        private var instance: PresenceApiRepository? = null
        fun getInstance(): PresenceApiRepository? {
            if (instance == null) {
                synchronized(PresenceApiRepository::class) {
                    instance = PresenceApiRepository()
                }
            }
            return instance
        }
    }

    suspend fun fetchPresencesContainer(token: String): Response<MutableList<PresenceContainerResponse>> {
        val service = NetworkConfig.client.create(PresenceApiService::class.java)
        return service.fetchPresenceContainer(token)
    }

    suspend fun getPresencesContainer(
        token: String,
        id: String
    ): Response<PresenceContainerResponse> {
        val service = NetworkConfig.client.create(PresenceApiService::class.java)
        return service.getPresenceContainer(token, id)
    }

    suspend fun changeItemStatus(
        token: String,
        id: String,
        queryMap: HashMap<String, String>
    ): Response<PresenceItem> {
        val service = NetworkConfig.client.create(PresenceApiService::class.java)
        return service.changeItemStatus(token, id, queryMap)
    }

    suspend fun addNewPresenceContainer(
        token: String,
        presenceContainerRequest: PresenceContainerRequest
    ): Response<PresenceContainerResponse> {
        val service = NetworkConfig.client.create(PresenceApiService::class.java)
        return service.addNewPresenceContainer(token, presenceContainerRequest)
    }
}