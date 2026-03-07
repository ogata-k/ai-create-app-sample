package com.example.ai_sample.core

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * A simple event bus for global events.
 */
object AppEventBus {
    private val _events = MutableSharedFlow<AppEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    suspend fun emit(event: AppEvent) {
        _events.emit(event)
    }

    fun tryEmit(event: AppEvent) {
        _events.tryEmit(event)
    }
}

sealed interface AppEvent {
    data class ShowSnackbar(val message: String) : AppEvent
    data object Unauthorized : AppEvent
    // Add more global events here
}
