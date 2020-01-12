package com.assessment.deliverybaseproject.datamanager

import com.assessment.deliverybaseproject.model.DeliveryResult

interface DataManager {
    fun doLoad(): DeliveryResult
    fun doRefresh(): DeliveryResult
}