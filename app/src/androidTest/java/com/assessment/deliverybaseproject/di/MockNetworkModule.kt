package com.assessment.deliverybaseproject.di

import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * This module provides the dependencies related to web API
 */
@Module
@Suppress("unused")
class MockNetworkModule {

    @Provides
    @Reusable
    internal fun provideDeliveryApi(): DeliveryApi {
        val mockWebServer = MockWebServer()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("http://localhost:8888/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(DeliveryApi::class.java)
    }
}