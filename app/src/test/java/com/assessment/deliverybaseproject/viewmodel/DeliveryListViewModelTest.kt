package com.assessment.deliverybaseproject.viewmodel

import android.app.Application
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.assessment.deliverybaseproject.base.BaseTest
import com.assessment.deliverybaseproject.datamanager.AppDataManager
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.model.DeliveryResult
import com.assessment.deliverybaseproject.repo.paging.DeliveryPagingCallback
import com.assessment.deliverybaseproject.ui.deliveryList.DeliveryListViewModel
import com.assessment.deliverybaseproject.utils.DeliveryStub
import com.assessment.deliverybaseproject.utils.MockDataSource
import com.jraska.livedata.test
import com.nhaarman.mockito_kotlin.spy
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.junit.MockitoRule

@RunWith(MockitoJUnitRunner.Silent::class)
class DeliveryListViewModelTest : BaseTest() {

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var dataManager: AppDataManager
    private lateinit var viewModel: DeliveryListViewModel

    private var context = mock(Application::class.java)

    @Before
    fun setUp() {
        viewModel = DeliveryListViewModel(dataManager)
    }

    @Test
    fun testInit_startLoading() {
        viewModel.isLoading.test().assertValue(true)
        verify(dataManager, times(1)).doLoad()
    }

    @Test
    fun testLoadResult_success() {
        Assert.assertNull(viewModel.deliveryResult.value)
        given(dataManager.doLoad()).willReturn(mockDeliveryList())

        viewModel.deliveryResult.test().assertHasValue()
    }

    @Test
    fun testDataLoad_fail() {
        viewModel = DeliveryListViewModel(dataManager)
        given(dataManager.doLoad()).willReturn(mockErroneousData())

        viewModel.deliveryResult.value?.networkErrors?.test()?.run {
            assertValue { it.error == Throwable("Failed to connect") }
        }
        viewModel.deliveryResult.value?.data?.test()?.run {
            assertValue { it.size == 0 }
        }
    }

    @Test
    fun testLoadDeliveries_success() {
        `when`(dataManager.doLoad()).thenReturn(mockDeliveryList())
        val spy = spy(viewModel)

        spy.loadDeliveries()

        spy.deliveries.test().run {
            assertValue { it[0]?.id == 1 }
            assertValue { it.size == 3 }
        }
    }

    @Test
    fun testRefreshList_loadResultSuccess() {
        viewModel.isLoading.value = false
        Assert.assertNull(viewModel.deliveryResult.value)
        given(dataManager.doRefresh()).willReturn(mockDeliveryList())

        viewModel.onRefresh()

        viewModel.isLoading.test().assertValue(true)
        verify(dataManager, times(1)).doRefresh()
        viewModel.deliveryResult.test().assertHasValue()
    }

    @Test
    fun testRefreshList_loadDeliveriesSuccess() {
        given(dataManager.doRefresh()).willReturn(mockDeliveryList())
        viewModel.isLoading.value = false

        val spy = spy(viewModel)
        spy.onRefresh()

        spy.deliveries.test().run {
            assertHasValue()
            assertValue { it[0]?.id == 1 }
            assertValue { it.size == 3 }
        }
    }

    @Test
    fun onSelectedDeliveryTest_selected() {
        viewModel.selectedDelivery.test().assertNoValue()?.assertHistorySize(0)

        viewModel.onDeliverySelected(DeliveryStub.getValidStub())

        viewModel.selectedDelivery.test().assertHasValue()?.assertHistorySize(1)
        viewModel.selectedDelivery.test().run {
            assertValue { it == DeliveryStub.getValidStub() }
        }
    }

    @Test
    fun onRetryClick() {
        viewModel.errorClick.onClick(View(context))

        viewModel.isLoading.test().assertValue { it == true }
        verify(dataManager, times(2)).doLoad()
    }

    private fun mockDeliveryList(): DeliveryResult? {
        val pagedConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setPrefetchDistance(2)
            .setEnablePlaceholders(true)
            .build()
        val data = LivePagedListBuilder(
            MockDataSource<Delivery>(
                DeliveryStub.getValidListStub()
            ), pagedConfig
        )
            .build()
        return DeliveryResult(data, MutableLiveData<DeliveryPagingCallback.NetworkError>(), MutableLiveData<Boolean>())
    }

    private fun mockErroneousData(): DeliveryResult? {
        val pagedConfig = PagedList.Config.Builder()
            .setPageSize(10)
            .setPrefetchDistance(2)
            .setEnablePlaceholders(true)
            .build()
        val data = LivePagedListBuilder(
            MockDataSource(
                ArrayList<Delivery>()
            ), pagedConfig
        ).build()
        val error = MutableLiveData<DeliveryPagingCallback.NetworkError>()
        error.value?.error = Throwable("Failed to connect")
        return DeliveryResult(data, error, MutableLiveData<Boolean>())
    }
}