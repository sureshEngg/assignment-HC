package com.assessment.deliverybaseproject.datamanager

import com.assessment.deliverybaseproject.model.DeliveryResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataManager @Inject
constructor(private val deliveryDataLoader: DeliveryDataLoader) : DataManager {
    override fun doLoad(): DeliveryResult {
        deliveryDataLoader.refresh = false
        return deliveryDataLoader.deliveries
    }

    override fun doRefresh(): DeliveryResult {
        deliveryDataLoader.refresh = true
        return deliveryDataLoader.deliveries
    }
}