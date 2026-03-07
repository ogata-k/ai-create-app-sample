package com.example.ai_sample.ui.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.example.ai_sample.core.AppEvent
import com.example.ai_sample.core.AppEventBus
import com.example.ai_sample.core.BaseViewModel
import com.example.ai_sample.core.LoggingMiddleware
import com.example.ai_sample.core.TimingMiddleware
import com.example.ai_sample.data.repository.ItemRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    private val repository: ItemRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ItemDetailState, ItemDetailIntent, ItemDetailAction, ItemDetailMutation, ItemDetailEffect>(
    initialState = ItemDetailState(),
    reducer = ItemDetailReducer(),
    intentMiddlewares = emptyList(),
    actionMiddlewares = listOf(
        LoggingMiddleware(),
        TimingMiddleware()
    )
) {
    private val detailRoute = savedStateHandle.toRoute<DetailRoute>()
    private val itemId = detailRoute.itemId

    init {
        dispatch(ItemDetailIntent.LoadItemRequested(itemId))
    }

    override fun mapIntentToAction(intent: ItemDetailIntent): ItemDetailAction {
        return when (intent) {
            is ItemDetailIntent.LoadItemRequested -> ItemDetailAction.FetchItem(intent.itemId)
            is ItemDetailIntent.BackClicked -> ItemDetailAction.HandleBack
        }
    }

    override suspend fun handleAction(action: ItemDetailAction) {
        when (action) {
            is ItemDetailAction.FetchItem -> fetchItem(action.itemId)
            is ItemDetailAction.HandleBack -> emitEffect(ItemDetailEffect.NavigateBack)
        }
    }

    private suspend fun fetchItem(id: Int) {
        emitMutation(ItemDetailMutation.Loading)
        repository.getItem(id)
            .onSuccess { item ->
                emitMutation(ItemDetailMutation.ItemLoaded(item))
            }
            .onFailure { error ->
                val errorMessage = error.message ?: "Failed to load item"
                emitMutation(ItemDetailMutation.Error(errorMessage))
                AppEventBus.emit(AppEvent.ShowSnackbar(errorMessage))
            }
    }
}
