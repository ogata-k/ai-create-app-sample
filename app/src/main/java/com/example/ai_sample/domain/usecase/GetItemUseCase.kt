package com.example.ai_sample.domain.usecase

import com.example.ai_sample.data.model.Item
import com.example.ai_sample.data.repository.ItemRepository

class GetItemUseCase(
    private val repository: ItemRepository
) {
    suspend operator fun invoke(id: Int): Result<Item> {
        return repository.getItem(id)
    }
}
