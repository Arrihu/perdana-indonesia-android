package app.perdana.indonesia.data.repository

import app.perdana.indonesia.core.utils.NetworkConfig
import app.perdana.indonesia.data.remote.api.UserApiService
import app.perdana.indonesia.data.remote.model.LoginRequest
import app.perdana.indonesia.data.remote.model.LoginResponse
import retrofit2.Response

/**
 * Created by ebysofyan on 12/2/19.
 */

class UserApiRepository {
    companion object {
        private var instance: UserApiRepository? = null
        fun getInstance(): UserApiRepository? {
            if (instance == null) {
                synchronized(UserApiRepository::class) {
                    instance = UserApiRepository()
                }
            }
            return instance
        }
    }

    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        val service = NetworkConfig.client.create(UserApiService::class.java)
        return service.login(loginRequest)
    }
}