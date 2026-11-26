package com.faiz.bkksmkn6.data

import com.faiz.bkksmkn6.BuildConfig
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BkkApi{
    @GET("jobs")
    suspend fun jobs(
        @Query("page")page: Int=1,
        @Query("per_page")perPage: Int=10,
        @Query("q")q: String? = null,
        @Query("type")type: String? = null
    ): ListResponse<Lowongan>

    @GET("jobs/{id}")
    suspend fun job (@Path("id") id: Int): DetailResponse<Lowongan>

    @GET("companies")
    suspend fun companies(
        @Query("page")page: Int =1,
        @Query("per_page")perpage: Int =10,
        @Query("q")q: String? = null
    ): ListResponse<Perusahaan>

    @GET("companies/{id}")
    suspend fun company(@Path("id") id: Int): DetailResponse<Perusahaan>

    @GET("news")
    suspend fun news(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 10,
        @Query("q") q: String? = null
    ): ListResponse<Berita>

    @GET("news/{id}")
    suspend fun newsDetail(@Path("id") id: Int): DetailResponse<Berita>
}

object ApiClient {

    private val http = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .header("X-API-Key", BuildConfig.BKK_API_KEY)
                    .build()
            )

        }
        .build()
    val api: BkkApi= Retrofit.Builder()
        .baseUrl("https://bkk.smkn6solo.sch.id/api/v1/")
        .client(http)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BkkApi::class.java)
    }