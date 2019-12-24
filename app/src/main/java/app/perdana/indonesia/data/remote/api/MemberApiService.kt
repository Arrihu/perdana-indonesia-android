package app.perdana.indonesia.data.remote.api

import com.google.gson.JsonElement
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

/**
 * Created by ebysofyan on 12/2/19.
 */
interface MemberApiService {
    @GET("/api/v1/user/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<JsonElement>
}