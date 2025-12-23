package com.sword.cats.data.api.common

import android.content.Context
import com.sword.cats.BuildConfig
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

enum class NetworkClientType { STANDARD, AUTHENTICATED }

object NetworkClientFactory {
    private const val READ_TIMEOUT_SECONDS = 30L
    private const val WRITE_TIMEOUT_SECONDS = 30L
    private const val CONNECTION_TIMEOUT_SECONDS = 10L
    private const val CACHE_SIZE_BYTES = 10 * 1024 * 1024L // 10 MB
    private const val NETWORK_LOG_TAG = "OkHttp"
    private const val X_API_KEY = "x-api-key"
    private const val HTTP_CACHE = "HttpCache"

    @JvmStatic
    fun getClient(
        context: Context,
        networkClientType: NetworkClientType,
    ): OkHttpClient {
        val builder = OkHttpClient().newBuilder()

        builder.connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        builder.readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        builder.writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        builder.cache(getCache(context))
        if (networkClientType == NetworkClientType.AUTHENTICATED) {
            builder.addInterceptor(buildAuthInterceptor())
        }
        builder.addInterceptor(buildExceptionInterceptor())
        builder.addNetworkInterceptor(buildLoggingInterceptor())

        return builder.build()
    }

    private fun buildExceptionInterceptor(): Interceptor = Interceptor { chain ->
        try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            Response.Builder()
                .request(chain.request())
                .code(599)
                .protocol(Protocol.HTTP_2)
                .message(e.localizedMessage ?: "Unknown error")
                .body("{}".toResponseBody("application/json".toMediaType()))
                .build()
        }
    }

    private fun buildLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor { msg -> android.util.Log.d(NETWORK_LOG_TAG, msg) }
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
        return interceptor
    }

    private fun buildAuthInterceptor(): Interceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .header(X_API_KEY, BuildConfig.API_KEY)
            .build()
        chain.proceed(newRequest)
    }

    private fun getCache(context: Context): Cache {
        val httpCacheDirectory = File(context.cacheDir.absolutePath, HTTP_CACHE)
        return Cache(httpCacheDirectory, CACHE_SIZE_BYTES)
    }
}