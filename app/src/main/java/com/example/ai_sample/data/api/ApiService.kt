package com.example.ai_sample.data.api

import com.example.ai_sample.data.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("posts")
    suspend fun getItems(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int
    ): List<Item>

    @GET("posts/{id}")
    suspend fun getItem(@Path("id") id: Int): Item
}
