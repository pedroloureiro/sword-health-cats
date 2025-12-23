package com.sword.cats.data.api.common

import com.sword.cats.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

class ApiServiceFactory(client: OkHttpClient) {
    private val builder: Retrofit.Builder = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())

    fun <S : Any> getServiceClient(serviceClass: KClass<S>): S = builder.build().create(serviceClass.java)
}