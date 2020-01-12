package com.assessment.deliverybaseproject.viewmodel

import com.assessment.deliverybaseproject.base.BaseTest
import com.assessment.deliverybaseproject.ui.deliveryDetail.DeliveryDetailViewModel
import com.assessment.deliverybaseproject.utils.DeliveryStub
import com.jraska.livedata.test
import org.junit.Before
import org.junit.Test

class DeliveryDetailViewModelTest : BaseTest() {

    private lateinit var viewModel: DeliveryDetailViewModel

    @Before
    fun setUp() {
        viewModel = DeliveryDetailViewModel()
    }

    @Test
    fun testSelectedDelivery_setLiveData() {
        viewModel.delivery.test().assertNoValue().assertHistorySize(0)
        viewModel.loc.test().assertNoValue().assertHistorySize(0)

        viewModel.setSelectedDeliveryDetailInfo(DeliveryStub.getValidStub())

        viewModel.delivery.test().assertHasValue().assertHistorySize(1)
            .run { assertValue { it == DeliveryStub.getValidStub() } }
        viewModel.loc.test().assertHasValue().assertHistorySize(1)
            .run { assertValue { it == DeliveryStub.getValidStub().location } }
    }
}