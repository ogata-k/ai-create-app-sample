package com.example.ai_sample.ui.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ai_sample.data.repository.ItemRepository

class ItemDetailViewModelFactory(
    private val repository: ItemRepository,
    private val itemId: Int
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemDetailViewModel::class.java)) {
            return ItemDetailViewModel(repository, itemId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
