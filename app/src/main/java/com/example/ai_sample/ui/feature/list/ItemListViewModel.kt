package com.example.ai_sample.ui.feature.list

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.ai_sample.core.AppEvent
import com.example.ai_sample.core.AppEventBus
import com.example.ai_sample.core.BaseViewModel
import com.example.ai_sample.core.LoggingMiddleware
import com.example.ai_sample.core.TimingMiddleware
import com.example.ai_sample.data.model.Item
import com.example.ai_sample.data.paging_source.ItemPagingSource
import com.example.ai_sample.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ItemListViewModel @Inject constructor(
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

    val pagedItems: Flow<PagingData<Item>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { ItemPagingSource(repository) }
    )
        .flow
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
        // Note: For non-paged initial load or other purposes
        val result = repository.getItems(page = 1, limit = 100)
        result.fold(
            onSuccess = { items ->
                emitMutation(ItemListMutation.ItemsLoaded(items))
            },
            onFailure = { e ->
                val errorMessage = e.message ?: "Failed to load items"
                emitMutation(ItemListMutation.Error(errorMessage))
                AppEventBus.tryEmit(AppEvent.ShowSnackbar(errorMessage))
            }
        )
    }
}
