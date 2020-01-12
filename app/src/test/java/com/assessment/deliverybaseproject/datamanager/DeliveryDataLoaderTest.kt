package com.assessment.deliverybaseproject.datamanager

import androidx.paging.DataSource
import com.assessment.deliverybaseproject.base.BaseTest
import com.assessment.deliverybaseproject.db.cache.DeliveryLocalCache
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import com.assessment.deliverybaseproject.utils.DeliveryStub
import com.assessment.deliverybaseproject.utils.MockDataSource
import com.jraska.livedata.test
import com.nhaarman.mockito_kotlin.given
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
class DeliveryDataLoaderTest : BaseTest() {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    private lateinit var deliveryDataLoader: DeliveryDataLoader
    @Mock
    private lateinit var deliveryLocalCache: DeliveryLocalCache
    @Mock
    private lateinit var deliveryApi: DeliveryApi

    private lateinit var dataSource: DataSource.Factory<Int, Delivery>

    @Before
    fun setUp() {
        deliveryDataLoader = DeliveryDataLoader(deliveryApi, deliveryLocalCache)
        dataSource = MockDataSource(
            DeliveryStub.getValidListStub()
        )
        given(deliveryLocalCache.deliveries).willReturn(dataSource)
    }

    @Test(expected = NullPointerException::class)
    fun testGetDeliveries_loading() {
        val deliveryResult = deliveryDataLoader.deliveries

        Assert.assertNull(deliveryResult.networkErrors.value?.error)
        deliveryResult.isRequestInProgress.test().assertValue(false)
        Assert.assertNotNull(deliveryResult.data)
        deliveryResult.data.test().run {
            assertHasValue()
            assertValue { it.size == 3 }
            assertValue { it[0]?.id == 1 }
        }
    }

    @Test(expected = NullPointerException::class)
    fun testGetDeliveries_refreshing() {
        deliveryDataLoader.refresh = true

        val deliveryResult = deliveryDataLoader.deliveries

        verify(deliveryLocalCache).clear()
        Assert.assertNull(deliveryResult.networkErrors.value?.error)
        deliveryResult.isRequestInProgress.test().assertValue(false)
        Assert.assertNotNull(deliveryResult.data)
        deliveryResult.data.test().run {
            assertHasValue()
            assertValue { it.size == 3 }
            assertValue { it[0]?.id == 1 }
        }
    }
}