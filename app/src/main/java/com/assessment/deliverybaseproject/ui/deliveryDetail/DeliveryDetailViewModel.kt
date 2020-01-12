package com.assessment.deliverybaseproject.ui.deliveryDetail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.model.Location

class DeliveryDetailViewModel : ViewModel() {

    val loc = MutableLiveData<Location>()
    val delivery = MutableLiveData<Delivery>()

    fun setSelectedDeliveryDetailInfo(delivery: Delivery) {
        this.delivery.postValue(delivery)
        loc.postValue(delivery.location)
    }
}