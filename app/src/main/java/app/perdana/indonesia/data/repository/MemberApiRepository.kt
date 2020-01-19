package app.perdana.indonesia.data.repository

import app.perdana.indonesia.core.utils.NetworkConfig
import app.perdana.indonesia.data.remote.api.MemberApiService
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import com.google.gson.JsonElement
import retrofit2.Response

/**
 * Created by ebysofyan on 12/2/19.
 */

class MemberApiRepository {
    companion object {
        private var instance: MemberApiRepository? = null
        fun getInstance(): MemberApiRepository? {
            if (instance == null) {
                synchronized(MemberApiRepository::class) {
                    instance = MemberApiRepository()
                }
            }
            return instance
        }
    }

    suspend fun getProfile(token: String): Response<JsonElement> {
        val service = NetworkConfig.client.create(MemberApiService::class.java)
        return service.getProfile(token)
    }

    suspend fun fetchMembers(token: String): Response<List<ArcherMemberResponse>> {
        val service = NetworkConfig.client.create(MemberApiService::class.java)
        return service.fetchMembers(token)
    }

    suspend fun fetchMemberApplicants(token: String): Response<List<ArcherMemberResponse>> {
        val service = NetworkConfig.client.create(MemberApiService::class.java)
        return service.fetchMemberApplicants(token)
    }

    suspend fun getMemberApplicant(token: String, id: String): Response<ArcherMemberResponse> {
        val service = NetworkConfig.client.create(MemberApiService::class.java)
        return service.getMemberApplicant(token, id)
    }

    suspend fun approveApplicantMember(
        token: String,
        id: String,
        registerNumber: String
    ): Response<ArcherMemberResponse> {
        val service = NetworkConfig.client.create(MemberApiService::class.java)
        return service.approveApplicantMember(token, id, registerNumber)
    }
}