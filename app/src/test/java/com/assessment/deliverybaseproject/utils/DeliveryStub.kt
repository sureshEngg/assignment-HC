package com.assessment.deliverybaseproject.utils

import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.model.Location

class DeliveryStub {
    companion object {
        private var loc = Location("place", 24.000000, 12.999999)
        fun getValidStub(): Delivery {
            return Delivery(
                1, "image.com", "Deliver to hukum",
                loc
            )
        }

        fun getValidListStub(): List<Delivery> {
            return listOf(
                Delivery(
                    1, "image.com", "Deliver to hukum",
                    loc
                ),
                Delivery(
                    2, "image1.com", "Deliver to hukum1",
                    loc
                ),
                Delivery(
                    3, "image1.com", "Deliver to hukum1",
                    loc
                )
            )
        }
    }
}