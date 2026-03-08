package com.example.ai_sample.ui.feature.list

import com.example.ai_sample.ui.core.Reducer

class ItemListReducer : Reducer<ItemListState, ItemListMutation> {
    override fun reduce(currentState: ItemListState, mutation: ItemListMutation): ItemListState {
        return when (mutation) {
            is ItemListMutation.Loading -> currentState.copy(
                isLoading = true,
                error = null
            )

            is ItemListMutation.ItemsLoaded -> currentState.copy(
                isLoading = false,
                items = mutation.items,
                error = null
            )

            is ItemListMutation.Error -> currentState.copy(
                isLoading = false,
                error = mutation.message
            )
        }
    }
}
