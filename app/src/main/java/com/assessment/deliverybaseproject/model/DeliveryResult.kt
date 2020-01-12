package com.assessment.deliverybaseproject.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.assessment.deliverybaseproject.repo.paging.DeliveryPagingCallback

class DeliveryResult(
    var data: LiveData<PagedList<Delivery>>,
    val networkErrors: LiveData<DeliveryPagingCallback.NetworkError>,
    var isRequestInProgress: LiveData<Boolean>
)
