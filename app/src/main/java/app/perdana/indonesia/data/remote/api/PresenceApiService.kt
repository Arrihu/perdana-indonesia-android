package app.perdana.indonesia.data.remote.api

import app.perdana.indonesia.data.remote.model.PresenceContainerResponse
import app.perdana.indonesia.data.remote.model.PresenceItem
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by ebysofyan on 12/2/19.
 */
interface PresenceApiService {
    @GET("/api/v1/presences/list")
    suspend fun fetchPresenceContainer(@Header("Authorization") token: String): Response<MutableList<PresenceContainerResponse>>

    @GET("/api/v1/presences/list/{id}")
    suspend fun getPresenceContainer(@Header("Authorization") token: String, @Path("id") id: String): Response<PresenceContainerResponse>

    @GET("/api/v1/presences/list/{id}/change-status")
    suspend fun changeItemStatus(
        @Header("Authorization") token: String, @Path("id") id: String, @QueryMap queryMap: HashMap<String, String>
    ): Response<PresenceItem>

    @FormUrlEncoded
    @POST("/api/v1/presences/list/")
    suspend fun addNewPresenceContainer(@Header("Authorization") token: String, @Field("title") title: String): Response<PresenceContainerResponse>
}