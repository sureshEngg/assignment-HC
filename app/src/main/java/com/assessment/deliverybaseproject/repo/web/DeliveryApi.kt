package com.assessment.deliverybaseproject.repo.web

import com.assessment.deliverybaseproject.model.Delivery
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * This interface provides the list of deliveries from the server
 */
interface DeliveryApi {
    @GET("/deliveries")
    fun getDeliveries(
        @Query("offset") page: Int,
        @Query("limit") pageSize: Int
    ): Single<List<Delivery>>
}