package com.assessment.deliverybaseproject.db.cache

import androidx.paging.DataSource
import com.assessment.deliverybaseproject.db.dao.DeliveryDao
import com.assessment.deliverybaseproject.model.Delivery
import java.util.concurrent.Executor

class DeliveryLocalCache(private val deliveryDao: DeliveryDao, private val ioExecutor: Executor) {

    val deliveries: DataSource.Factory<Int, Delivery>
        get() = deliveryDao.deliveries

    fun insert(deliveries: List<Delivery>) {
        ioExecutor.execute {
            deliveryDao.insertAll(deliveries)
        }
    }

    fun clear() {
        ioExecutor.execute {
            deliveryDao.clear()
        }
    }
}

