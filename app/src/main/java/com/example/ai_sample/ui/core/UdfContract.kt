package com.example.ai_sample.ui.core

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Marker interface for all intents in the system.
 */
interface Intent

/**
 * Marker interface for all actions in the system.
 */
interface Action

/**
 * Marker interface for all mutations in the system.
 */
interface Mutation

/**
 * Marker interface for all effects in the system.
 */
interface Effect

/**
 * A Reducer is a pure function that takes the current state and a mutation,
 * and returns the new state.
 */
interface Reducer<S, M : Mutation> {
    fun reduce(currentState: S, mutation: M): S
}

/**
 * ActionMiddleware is used for side effects like logging, analytics, and navigation.
 */
interface ActionMiddleware<S, A : Action> {
    suspend fun handle(
        state: S,
        action: A,
        next: suspend (A) -> Unit
    )
}

/**
 * IntentMiddleware is used to intercept or transform intents before they become actions.
 */
interface IntentMiddleware<S, I : Intent> {
    suspend fun handle(
        state: S,
        intent: I,
        next: suspend (I) -> Unit
    )
}

/**
 * Base interface for the Store (ViewModel) that manages state and effects.
 */
interface Store<S, I : Intent, E : Effect> {
    val state: StateFlow<S>
    val effects: SharedFlow<E>
    fun dispatch(intent: I)
}
