package com.assessment.deliverybaseproject.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Delivery(
    @field:PrimaryKey
    val id: Int,
    val imageUrl: String,
    val description: String,
    @Embedded var location: Location
)