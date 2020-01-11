package app.perdana.indonesia.data.repository

import app.perdana.indonesia.core.utils.NetworkConfig
import app.perdana.indonesia.data.remote.api.PracticeApiService
import app.perdana.indonesia.data.remote.model.PracticeContainer
import app.perdana.indonesia.data.remote.model.PracticeContainerSeries
import retrofit2.Response

/**
 * Created by ebysofyan on 12/2/19.
 */

class PracticeApiRepository {
    companion object {
        private var instance: PracticeApiRepository? = null
        fun getInstance(): PracticeApiRepository? {
            if (instance == null) {
                synchronized(PracticeApiRepository::class) {
                    instance = PracticeApiRepository()
                }
            }
            return instance
        }
    }

    suspend fun fetchPracticesContainer(
        token: String,
        archerId: String
    ): Response<MutableList<PracticeContainer>> {
        val service = NetworkConfig.client.create(PracticeApiService::class.java)
        return service.fetchPracticesContainer(token, archerId)
    }

    suspend fun getPracticesContainer(
        token: String,
        containerId: String,
        archerId: String
    ): Response<PracticeContainerSeries> {
        val service = NetworkConfig.client.create(PracticeApiService::class.java)
        return service.getPracticesContainer(token, containerId, archerId)
    }

    suspend fun addNewPracticeContainer(
        token: String,
        archerId: String,
        practiceContainer: PracticeContainer
    ): Response<PracticeContainer> {
        val service = NetworkConfig.client.create(PracticeApiService::class.java)
        return service.addNewPracticeContainer(token, archerId, practiceContainer)
    }
}