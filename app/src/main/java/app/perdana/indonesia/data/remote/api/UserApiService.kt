package app.perdana.indonesia.data.remote.api

import app.perdana.indonesia.data.remote.model.LoginRequest
import app.perdana.indonesia.data.remote.model.LoginResponse
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by ebysofyan on 12/2/19.
 */
interface UserApiService {
    @POST("/api/v1/user/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @Multipart
    @POST("/api/v1/user/register")
    suspend fun register(@PartMap body: HashMap<String, RequestBody>, @Part photos: MutableList<MultipartBody.Part>): Response<JsonElement>
}