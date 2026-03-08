package com.example.ai_sample.ui.middleware.action

import android.util.Log
import com.example.ai_sample.ui.core.Action
import com.example.ai_sample.ui.core.ActionMiddleware

/**
 * Middleware that logs every action.
 */
class LoggingMiddleware<S, A : Action> : ActionMiddleware<S, A> {
    override suspend fun handle(state: S, action: A, next: suspend (A) -> Unit) {
        Log.d("UDF_LOG", "Action: $action, State: $state")
        next(action)
    }
}