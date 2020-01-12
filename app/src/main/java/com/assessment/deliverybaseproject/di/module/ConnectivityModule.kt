package com.assessment.deliverybaseproject.di.module

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ConnectivityModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    fun provideConnectivity(context: Context): ConnectivityManager {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return connectivityManager as ConnectivityManager
    }
}