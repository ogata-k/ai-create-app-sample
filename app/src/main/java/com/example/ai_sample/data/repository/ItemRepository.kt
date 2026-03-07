package com.example.ai_sample.data.repository

import com.example.ai_sample.data.api.ApiService
import com.example.ai_sample.data.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(
    private val apiService: ApiService
) {

    suspend fun getItems(page: Int, limit: Int): List<Item> = withContext(Dispatchers.IO) {
        apiService.getItems(page = page, limit = limit)
    }

    suspend fun getItem(id: Int): Result<Item> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getItem(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
