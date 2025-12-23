package com.sword.cats.di

import android.content.Context
import com.sword.cats.BuildConfig
import com.sword.cats.data.api.common.ApiServiceFactory
import com.sword.cats.data.api.common.NetworkClientFactory
import com.sword.cats.data.api.common.NetworkClientType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Singleton
    @Provides
    @Named(DIConstants.API_STANDARD)
    fun providesStandardNetworkClient(context: Context): OkHttpClient {
        return NetworkClientFactory.getClient(context, NetworkClientType.STANDARD)
    }

    @Singleton
    @Provides
    @Named(DIConstants.API_AUTHENTICATED)
    fun providesAuthenticatedNetworkClient(context: Context): OkHttpClient {
        return NetworkClientFactory.getClient(context, NetworkClientType.AUTHENTICATED)
    }

    @Provides
    @Singleton
    @Named(DIConstants.API_AUTHENTICATED)
    fun providesApiAuthServiceFactory(@Named(DIConstants.API_AUTHENTICATED) client: OkHttpClient): ApiServiceFactory {
        return ApiServiceFactory(client)
    }

    @Provides
    @Singleton
    @Named(DIConstants.API_STANDARD)
    fun providesApiStandardServiceFactory(@Named(DIConstants.API_STANDARD) client: OkHttpClient): ApiServiceFactory {
        return ApiServiceFactory(client)
    }
}