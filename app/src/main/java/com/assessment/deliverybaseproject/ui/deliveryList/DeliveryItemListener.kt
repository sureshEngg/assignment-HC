package com.assessment.deliverybaseproject.ui.deliveryList

import com.assessment.deliverybaseproject.model.Delivery

interface DeliveryItemListener {
    fun onDeliverySelected(delivery: Delivery)
}
