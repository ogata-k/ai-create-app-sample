package com.example.ai_sample.core

import android.util.Log

/**
 * Middleware that logs when an action reaches it.
 */
class TimingMiddleware<S, A : Action> : ActionMiddleware<S, A> {
    override suspend fun handle(state: S, action: A, next: suspend (A) -> Unit) {
        Log.d("UDF_LOG", "Action $action reached TimingMiddleware.")
        next(action)
    }
}
