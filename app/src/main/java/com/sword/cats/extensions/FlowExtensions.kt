package com.sword.cats.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

suspend fun <T> Flow<T>.mapToResult(): Result<T> {
    return try {
        Result.success(this.first())
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}