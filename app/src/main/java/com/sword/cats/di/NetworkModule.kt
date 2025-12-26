package com.sword.cats.di

import android.content.Context
import com.sword.cats.data.api.factories.ApiServiceFactory
import com.sword.cats.data.api.factories.NetworkClientFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    fun providesNetworkClient(context: Context): OkHttpClient {
        return NetworkClientFactory.getClient(context)
    }

    @Provides
    @Singleton
    fun providesApiServiceFactory(client: OkHttpClient): ApiServiceFactory {
        return ApiServiceFactory(client)
    }
}