package app.perdana.indonesia.core.utils

import app.perdana.indonesia.BuildConfig.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by @ebysofyan on 6/13/19
 */
object NetworkConfig {

    private val createOkHttpClient: OkHttpClient = OkHttpClient().newBuilder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    val client: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}