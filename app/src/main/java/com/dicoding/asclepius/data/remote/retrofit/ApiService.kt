package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.News
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token " + BuildConfig.API_KEY)
    @GET("everything")
    suspend fun getAllNews(
        @Query("q") query: String = "cancer"
    ): Call<News>
}