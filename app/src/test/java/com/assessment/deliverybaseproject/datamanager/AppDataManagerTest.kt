package com.assessment.deliverybaseproject.datamanager

import android.content.Context
import androidx.room.Room
import com.assessment.deliverybaseproject.base.BaseTest
import com.assessment.deliverybaseproject.db.DeliveryDatabase
import com.assessment.deliverybaseproject.db.cache.DeliveryLocalCache
import com.assessment.deliverybaseproject.db.dao.DeliveryDao
import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import com.assessment.deliverybaseproject.utils.DeliveryStub
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import java.util.concurrent.Executors

class AppDataManagerTest : BaseTest() {

    private lateinit var appDataManager: AppDataManager
    private lateinit var deliveryLocalCache: DeliveryLocalCache
    private lateinit var deliveryDataLoader: DeliveryDataLoader
    private var deliveryApi = Mockito.mock(DeliveryApi::class.java)
    private var deliveryDao = Mockito.mock(DeliveryDao::class.java)
    private lateinit var deliveryDb: DeliveryDatabase
    private val context = Mockito.mock(Context::class.java)

    @Before
    fun setUp() {
        deliveryDb = Room.inMemoryDatabaseBuilder(
            context,
            DeliveryDatabase::class.java
        ).build()
        deliveryDao.insertAll(DeliveryStub.getValidListStub())
        val deliveryDummyData = deliveryDb.deliveryDao().deliveries
        Mockito.`when`(deliveryDao.deliveries).thenReturn(deliveryDummyData)

        deliveryLocalCache = DeliveryLocalCache(deliveryDao, Executors.newSingleThreadExecutor())
        deliveryDataLoader = DeliveryDataLoader(deliveryApi, deliveryLocalCache)
        appDataManager = AppDataManager(deliveryDataLoader)
    }

    @Test
    fun testDoLoad() {
        appDataManager.doLoad()

        Assert.assertFalse(deliveryDataLoader.refresh)
    }

    @Test
    fun testDoRefresh() {
        appDataManager.doRefresh()

        Assert.assertTrue(deliveryDataLoader.refresh)
    }
}