package com.assessment.deliverybaseproject.repo.web

import com.assessment.deliverybaseproject.base.BaseTest
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.utils.DependencyProvider
import com.assessment.deliverybaseproject.utils.MOCK_API_PORT
import io.reactivex.Single
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.core.IsNull
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class DeliveryApiTest : BaseTest() {
    private lateinit var deliveryApi: DeliveryApi
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun init() {
        mockWebServer = MockWebServer()
        mockWebServer.start(MOCK_API_PORT)
        deliveryApi = DependencyProvider.provideRetrofit(mockWebServer)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testGetDeliveries_success() {
        queueResponse {
            setResponseCode(200)
            setBody(DependencyProvider.getResponseFromJson("deliveries"))
        }

        val deliveries = deliveryApi.getDeliveries(1, 10)

        Assert.assertThat<Single<List<Delivery>>>(deliveries, IsNull.notNullValue())
        deliveries.test().run {
            assertNoErrors()
            assertNoTimeout()
            assertComplete()
            Assert.assertEquals(values()[0].size, 18)
            Assert.assertEquals(values()[0][0].id, 1)
        }
    }

    @Test
    fun testGetDeliveries_failed() {
        queueResponse {
            setResponseCode(500)
            setBody(DependencyProvider.getResponseFromJson("errorResponse"))
        }

        deliveryApi
            .getDeliveries(1, 10)
            .test()
            .run {
                assertValueCount(0)
                Assert.assertEquals(values().size, 0)
            }
    }

    private fun queueResponse(block: MockResponse.() -> Unit) {
        mockWebServer.enqueue(MockResponse().apply(block))
    }

    @After
    fun tierDown() {
        mockWebServer.shutdown()
    }

}