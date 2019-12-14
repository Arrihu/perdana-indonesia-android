package app.perdana.indonesia.data.remote.api

import app.perdana.indonesia.data.remote.model.LoginRequest
import app.perdana.indonesia.data.remote.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by ebysofyan on 12/2/19.
 */
interface UserApiService {
    @POST("/api/v1/member/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}