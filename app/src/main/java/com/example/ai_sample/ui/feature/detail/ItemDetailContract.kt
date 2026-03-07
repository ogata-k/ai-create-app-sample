package com.example.ai_sample.ui.feature.detail

import com.example.ai_sample.core.Action
import com.example.ai_sample.core.Effect
import com.example.ai_sample.core.Intent
import com.example.ai_sample.core.Mutation
import com.example.ai_sample.data.model.Item

data class ItemDetailState(
    val item: Item? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ItemDetailIntent : Intent {
    data class LoadItemRequested(val itemId: Int) : ItemDetailIntent
    data object BackClicked : ItemDetailIntent
}

sealed interface ItemDetailAction : Action {
    data class FetchItem(val itemId: Int) : ItemDetailAction
    data object HandleBack : ItemDetailAction
}

sealed interface ItemDetailMutation : Mutation {
    data object Loading : ItemDetailMutation
    data class ItemLoaded(val item: Item) : ItemDetailMutation
    data class Error(val message: String) : ItemDetailMutation
}

sealed interface ItemDetailEffect : Effect {
    data object NavigateBack : ItemDetailEffect
}
