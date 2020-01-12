package com.assessment.deliverybaseproject.datamanager

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.assessment.deliverybaseproject.BuildConfig.DATABASE_PAGE_SIZE
import com.assessment.deliverybaseproject.BuildConfig.PREFETCH_DISTANCE_NUM
import com.assessment.deliverybaseproject.db.cache.DeliveryLocalCache
import com.assessment.deliverybaseproject.model.DeliveryResult
import com.assessment.deliverybaseproject.repo.paging.DeliveryPagingCallback
import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class is responsible for loading data and providing the online/offline data to ui
 */
@Singleton
class DeliveryDataLoader @Inject
constructor(
    private val deliveryService: DeliveryApi, private val localCache: DeliveryLocalCache
) {
    var refresh = false

    val deliveries: DeliveryResult
        get() {
            if (refresh) {
                localCache.clear()
            }
            val deliveries = localCache.deliveries
            val pagingCallback = DeliveryPagingCallback(deliveryService, localCache)
            val networkErrors = pagingCallback.networkErrors
            val isRequestInProgress = pagingCallback.isRequestInProgress
            val pagedConfig = PagedList.Config.Builder()
                .setPageSize(DATABASE_PAGE_SIZE)
                .setPrefetchDistance(PREFETCH_DISTANCE_NUM)
                .setEnablePlaceholders(true)
                .build()
            val data = LivePagedListBuilder(deliveries, pagedConfig)
                .setBoundaryCallback(pagingCallback)
                .build()

            return DeliveryResult(data, networkErrors, isRequestInProgress)
        }
}
