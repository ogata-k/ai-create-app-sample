package com.example.ai_sample.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base ViewModel implementing the refined UDF pattern.
 */
abstract class BaseViewModel<S, I : Intent, A : Action, M : Mutation, E : Effect>(
    initialState: S,
    private val reducer: Reducer<S, M>,
    private val intentMiddlewares: List<IntentMiddleware<S, I>> = emptyList(),
    private val actionMiddlewares: List<ActionMiddleware<S, A>> = emptyList()
) : ViewModel(), Store<S, I, E> {

    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<E>()
    override val effects: SharedFlow<E> = _effects.asSharedFlow()

    override fun dispatch(intent: I) {
        viewModelScope.launch {
            executeIntentPipeline(intent)
        }
    }

    private suspend fun executeIntentPipeline(intent: I) {
        val chain = intentMiddlewares.foldRight<IntentMiddleware<S, I>, suspend (I) -> Unit>(
            { currentIntent ->
                val action = mapIntentToAction(currentIntent)
                executeActionPipeline(action)
            }
        ) { middleware, next ->
            { currentIntent -> middleware.handle(state.value, currentIntent, next) }
        }
        chain(intent)
    }

    private suspend fun executeActionPipeline(action: A) {
        val chain = actionMiddlewares.foldRight<ActionMiddleware<S, A>, suspend (A) -> Unit>(
            { currentAction ->
                handleAction(currentAction)
            }
        ) { middleware, next ->
            { currentAction -> middleware.handle(state.value, currentAction, next) }
        }
        chain(action)
    }

    protected abstract fun mapIntentToAction(intent: I): A
    protected abstract suspend fun handleAction(action: A)

    protected fun emitMutation(mutation: M) {
        _state.value = reducer.reduce(_state.value, mutation)
    }

    protected suspend fun emitEffect(effect: E) {
        _effects.emit(effect)
    }
}
