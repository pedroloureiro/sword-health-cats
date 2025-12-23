package com.sword.cats.extensions

import retrofit2.Response

fun <T> Response<T>.mapToResult(): Result<T> {
    val dataModel = body()
    return if (isSuccessful && dataModel != null) {
        Result.success(dataModel)
    } else {
        Result.failure(Exception())
    }
}