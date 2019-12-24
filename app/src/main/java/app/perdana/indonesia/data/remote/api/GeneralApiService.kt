package app.perdana.indonesia.data.remote.api

import app.perdana.indonesia.data.remote.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by ebysofyan on 12/2/19.
 */
interface GeneralApiService {
    @GET("/api/v1/user/regionals")
    suspend fun fetchOpenRegionals(): Response<List<Regional>>

    @GET("/api/v1/user/provinces")
    suspend fun fetchOpenProvinces(@Query("regional") regionalId: String): Response<List<Province>>

    @GET("/api/v1/user/branchs")
    suspend fun fetchOpenBranch(@Query("province") provinceId: String): Response<List<Branch>>

    @GET("/api/v1/user/clubs")
    suspend fun fetchOpenClub(@Query("branch") branchId: String): Response<List<Club>>

    @GET("/api/v1/user/units")
    suspend fun fetchOpenUnit(@Query("branch") branchId: String): Response<List<Satuan>>
}