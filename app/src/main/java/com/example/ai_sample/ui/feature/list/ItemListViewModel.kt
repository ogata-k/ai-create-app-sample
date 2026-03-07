package com.example.ai_sample.ui.feature.list

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ai_sample.core.AppEvent
import com.example.ai_sample.core.AppEventBus
import com.example.ai_sample.core.BaseViewModel
import com.example.ai_sample.core.LoggingMiddleware
import com.example.ai_sample.core.TimingMiddleware
import com.example.ai_sample.data.model.Item
import com.example.ai_sample.data.repository.ItemRepository
import kotlinx.coroutines.flow.Flow

class ItemListViewModel(
    private val repository: ItemRepository
) : BaseViewModel<ItemListState, ItemListIntent, ItemListAction, ItemListMutation, ItemListEffect>(
    initialState = ItemListState(),
    reducer = ItemListReducer(),
    intentMiddlewares = emptyList(),
    actionMiddlewares = listOf(
        LoggingMiddleware(),
        TimingMiddleware()
    )
) {

    val pagedItems: Flow<PagingData<Item>> = repository.getItemPager()
        .cachedIn(viewModelScope)

    init {
        dispatch(ItemListIntent.LoadItemsRequested)
    }

    override fun mapIntentToAction(intent: ItemListIntent): ItemListAction {
        return when (intent) {
            is ItemListIntent.LoadItemsRequested -> ItemListAction.FetchItems
            is ItemListIntent.ItemClicked -> ItemListAction.HandleItemClick(intent.itemId)
        }
    }

    override suspend fun handleAction(action: ItemListAction) {
        when (action) {
            is ItemListAction.FetchItems -> fetchItems()
            is ItemListAction.HandleItemClick -> {
                emitEffect(ItemListEffect.NavigateToDetail(action.itemId))
            }
        }
    }

    private suspend fun fetchItems() {
        emitMutation(ItemListMutation.Loading)
        repository.getItems()
            .onSuccess { items ->
                emitMutation(ItemListMutation.ItemsLoaded(items))
            }
            .onFailure { error ->
                val errorMessage = error.message ?: "Failed to load items"
                emitMutation(ItemListMutation.Error(errorMessage))
                AppEventBus.tryEmit(AppEvent.ShowSnackbar(errorMessage))
            }
    }
}
