package com.assessment.deliverybaseproject.repo

import com.assessment.deliverybaseproject.base.BaseTest
import com.assessment.deliverybaseproject.db.cache.DeliveryLocalCache
import com.assessment.deliverybaseproject.repo.paging.DeliveryPagingCallback
import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import com.assessment.deliverybaseproject.utils.DeliveryStub
import com.jraska.livedata.test
import com.nhaarman.mockito_kotlin.spy
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

@RunWith(JUnit4::class)
class DeliveryPagingCallbackTest : BaseTest() {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var deliveryLocalCache: DeliveryLocalCache

    @Mock
    private lateinit var deliveryApi: DeliveryApi
    private lateinit var deliveryPagingCallback: DeliveryPagingCallback

    @Before
    fun setUp() {
        deliveryPagingCallback = DeliveryPagingCallback(deliveryApi, deliveryLocalCache)
    }

    @Test
    fun testDefaultValues() {
        Assert.assertEquals(false, deliveryPagingCallback.isRequestInProgress.value)
        Assert.assertNull(deliveryPagingCallback.networkErrors.value)
    }

    @Test
    fun testFirstPageLoad() {
        given(deliveryApi.getDeliveries(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
            .willReturn(Single.just(DeliveryStub.getValidListStub()))

        val spy = spy(deliveryPagingCallback)
        spy.onZeroItemsLoaded()

        deliveryPagingCallback.isRequestInProgress.test()
            .awaitValue()
            .assertHasValue()
            .assertValue(true)
        verify(spy).requestAndSaveData()
    }

    @Test
    fun testNextPageLoad() {
        given(deliveryApi.getDeliveries(ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt()))
            .willReturn(Single.just(DeliveryStub.getValidListStub()))

        val spy = spy(deliveryPagingCallback)
        spy.onItemAtEndLoaded(DeliveryStub.getValidStub())

        deliveryPagingCallback.isRequestInProgress.test()
            .awaitValue()
            .assertHasValue()
            .assertValue(true)
        verify(spy).requestAndSaveData()
    }
}