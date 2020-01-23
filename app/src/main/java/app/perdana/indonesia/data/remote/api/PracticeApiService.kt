package app.perdana.indonesia.data.remote.api

import app.perdana.indonesia.data.remote.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by ebysofyan on 12/2/19.
 */
interface PracticeApiService {
    @GET("/api/v1/practices/list")
    suspend fun fetchPracticesContainer(@Header("Authorization") token: String, @Query("archer_id") archerId: String): Response<MutableList<PracticeContainer>>

    @PUT("/api/v1/practices/series/{id}/")
    suspend fun updateSeriesScore(
        @Header("Authorization") token: String, @Path("id") id: String, @Body body: PracticeSeriesScore
    ): Response<PracticeSeries>

    @GET("/api/v1/practices/list/{id}")
    suspend fun getPracticesContainer(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("archer_id") archerId: String
    ): Response<PracticeContainerSeries>


    @POST("/api/v1/practices/list/")
    suspend fun addNewPracticeContainer(@Header("Authorization") token: String, @Query("archer_id") archerId: String, @Body practicesContainer: PracticeContainer): Response<PracticeContainer>
}