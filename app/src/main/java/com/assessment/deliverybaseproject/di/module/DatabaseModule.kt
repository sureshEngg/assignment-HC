package com.assessment.deliverybaseproject.di.module

import android.content.Context
import androidx.room.Room
import com.assessment.deliverybaseproject.BuildConfig
import com.assessment.deliverybaseproject.datamanager.AppDataManager
import com.assessment.deliverybaseproject.datamanager.DataManager
import com.assessment.deliverybaseproject.datamanager.DeliveryDataLoader
import com.assessment.deliverybaseproject.db.DeliveryDatabase
import com.assessment.deliverybaseproject.db.cache.DeliveryLocalCache
import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    internal fun provideDataManager(appDataManager: AppDataManager): DataManager {
        return appDataManager
    }

    @Provides
    @Singleton
    internal fun provideDeliveryDataLoader(
        deliveryApi: DeliveryApi,
        deliveryLocalCache: DeliveryLocalCache
    ): DeliveryDataLoader {
        return DeliveryDataLoader(deliveryApi, deliveryLocalCache)
    }

    @Provides
    fun provideDeliveryLocalCache(db: DeliveryDatabase): DeliveryLocalCache {
        return DeliveryLocalCache(
            db.deliveryDao(),
            Executors.newSingleThreadExecutor()
        )
    }

    @Provides
    fun provideDeliveryDatabase(context: Context): DeliveryDatabase {
        return Room.databaseBuilder(
            context,
            DeliveryDatabase::class.java, BuildConfig.DB_NAME
        ).build()
    }
}