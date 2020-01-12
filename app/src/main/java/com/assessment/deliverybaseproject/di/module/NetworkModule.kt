package com.assessment.deliverybaseproject.di.module

import com.assessment.deliverybaseproject.BuildConfig
import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * This module provides the dependencies related to web API
 */
@Module
@Suppress("unused")
class NetworkModule {

    @Provides
    @Reusable
    internal fun provideDeliveryApi(retrofit: Retrofit): DeliveryApi {
        return retrofit.create(DeliveryApi::class.java)
    }

    @Provides
    @Reusable
    internal fun provideRetrofitInterface(interceptor: HttpLoggingInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
        if (BuildConfig.DEBUG)
            client.addInterceptor(interceptor)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client.build())
            .build()
    }

    @Provides
    @Reusable
    internal fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}