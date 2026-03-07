package com.example.ai_sample.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.ai_sample.data.api.ApiService
import com.example.ai_sample.data.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(
    private val apiService: ApiService
) {

    fun getItemPager(): Flow<PagingData<Item>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ItemPagingSource(apiService) }
        ).flow
    }

    suspend fun getItems(): Result<List<Item>> = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getItems(page = 1, limit = 100)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
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
