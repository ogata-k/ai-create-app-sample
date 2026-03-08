package com.example.ai_sample.ui.feature.list

import com.example.ai_sample.data.model.Item
import com.example.ai_sample.ui.core.Action
import com.example.ai_sample.ui.core.Effect
import com.example.ai_sample.ui.core.Intent
import com.example.ai_sample.ui.core.Mutation

data class ItemListState(
    val items: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed interface ItemListIntent : Intent {
    data object LoadItemsRequested : ItemListIntent
    data class ItemClicked(val itemId: Int) : ItemListIntent
}

sealed interface ItemListAction : Action {
    data object FetchItems : ItemListAction
    data class HandleItemClick(val itemId: Int) : ItemListAction
}

sealed interface ItemListMutation : Mutation {
    data object Loading : ItemListMutation
    data class ItemsLoaded(val items: List<Item>) : ItemListMutation
    data class Error(val message: String) : ItemListMutation
}

sealed interface ItemListEffect : Effect {
    data class NavigateToDetail(val itemId: Int) : ItemListEffect
}
