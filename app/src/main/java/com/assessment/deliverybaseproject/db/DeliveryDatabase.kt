package com.assessment.deliverybaseproject.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.assessment.deliverybaseproject.db.dao.DeliveryDao
import com.assessment.deliverybaseproject.model.Delivery

@Database(entities = [Delivery::class], version = 1, exportSchema = false)
abstract class DeliveryDatabase : RoomDatabase() {
    abstract fun deliveryDao(): DeliveryDao
}