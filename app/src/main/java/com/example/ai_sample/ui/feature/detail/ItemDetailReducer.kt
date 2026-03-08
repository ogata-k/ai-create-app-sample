package com.example.ai_sample.ui.feature.detail

import com.example.ai_sample.ui.core.Reducer

class ItemDetailReducer : Reducer<ItemDetailState, ItemDetailMutation> {
    override fun reduce(
        currentState: ItemDetailState,
        mutation: ItemDetailMutation
    ): ItemDetailState {
        return when (mutation) {
            is ItemDetailMutation.Loading -> currentState.copy(
                isLoading = true,
                error = null
            )

            is ItemDetailMutation.ItemLoaded -> currentState.copy(
                isLoading = false,
                item = mutation.item,
                error = null
            )

            is ItemDetailMutation.Error -> currentState.copy(
                isLoading = false,
                error = mutation.message
            )
        }
    }
}
