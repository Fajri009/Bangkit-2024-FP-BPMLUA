package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.News
import retrofit2.http.*

interface ApiService {
    @Headers("Authorization: token " + BuildConfig.API_KEY)
    @GET("everything")
    suspend fun getAllNews(
        @Query("q") query: String = "cancer",
        @Query("sortBy") sortBy: String = "publishedAt",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): News
}