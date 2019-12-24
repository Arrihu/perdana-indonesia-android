package app.perdana.indonesia.data.repository

import app.perdana.indonesia.core.utils.NetworkConfig
import app.perdana.indonesia.data.remote.api.GeneralApiService
import app.perdana.indonesia.data.remote.model.*
import retrofit2.Response

/**
 * Created by ebysofyan on 12/2/19.
 */

class GeneralApiRepository {
    companion object {
        private var instance: GeneralApiRepository? = null
        fun getInstance(): GeneralApiRepository? {
            if (instance == null) {
                synchronized(GeneralApiRepository::class) {
                    instance = GeneralApiRepository()
                }
            }
            return instance
        }
    }

    suspend fun fetchRegionals(): Response<List<Regional>> {
        val service = NetworkConfig.client.create(GeneralApiService::class.java)
        return service.fetchOpenRegionals()
    }

    suspend fun fetchProvinces(regionalId: String): Response<List<Province>> {
        val service = NetworkConfig.client.create(GeneralApiService::class.java)
        return service.fetchOpenProvinces(regionalId)
    }

    suspend fun fetchBranchs(provinceId: String): Response<List<Branch>> {
        val service = NetworkConfig.client.create(GeneralApiService::class.java)
        return service.fetchOpenBranch(provinceId)
    }

    suspend fun fetchClubs(branchId: String): Response<List<Club>> {
        val service = NetworkConfig.client.create(GeneralApiService::class.java)
        return service.fetchOpenClub(branchId)
    }

    suspend fun fetchUnits(branchId: String): Response<List<Satuan>> {
        val service = NetworkConfig.client.create(GeneralApiService::class.java)
        return service.fetchOpenUnit(branchId)
    }
}