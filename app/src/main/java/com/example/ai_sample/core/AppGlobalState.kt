package com.example.ai_sample.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Global state for the application.
 */
object AppGlobalState {
    private val _state = MutableStateFlow(GlobalState())
    val state = _state.asStateFlow()

    fun update(transform: (GlobalState) -> GlobalState) {
        _state.update(transform)
    }

    data class GlobalState(
        val isLoading: Boolean = false,
        val userNickname: String? = null,
        val isDarkMode: Boolean = false
    )
}
