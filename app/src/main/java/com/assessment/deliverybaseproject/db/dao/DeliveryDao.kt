package com.assessment.deliverybaseproject.db.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.assessment.deliverybaseproject.model.Delivery

@Dao
interface DeliveryDao {
    @get:Query("SELECT * FROM delivery")
    val deliveries: DataSource.Factory<Int, Delivery>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(delivery: List<Delivery>)

    @Query("DELETE FROM delivery")
    fun clear()
}