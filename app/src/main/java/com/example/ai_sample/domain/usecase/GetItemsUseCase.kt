package com.example.ai_sample.domain.usecase

import com.example.ai_sample.data.model.Item
import com.example.ai_sample.data.repository.ItemRepository

class GetItemsUseCase(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(page: Int = 1, limit: Int = 20): Result<List<Item>> {
        return repository.getItems(page, limit)
    }
}
