package com.assessment.deliverybaseproject.ui.deliveryList

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.assessment.deliverybaseproject.datamanager.DataManager
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.model.DeliveryResult
import com.assessment.deliverybaseproject.repo.paging.DeliveryPagingCallback

/**
 * This class is created to provide the required business logic and server data to UI
 * ViewModel initiates the connection with Server at the start which is being observed
 * by the corresponding View class
 */
class DeliveryListViewModel(
    private var dataManager: DataManager
) : ViewModel(),
    DeliveryItemListener {

    var deliveryResult: MutableLiveData<DeliveryResult?> = MutableLiveData()

    var deliveries: LiveData<PagedList<Delivery>> = Transformations.switchMap(deliveryResult) { it?.data }

    val networkErrors: LiveData<DeliveryPagingCallback.NetworkError> = Transformations.switchMap(deliveryResult)
    { it?.networkErrors }

    val isRequestInProgress: LiveData<Boolean> = Transformations.switchMap(deliveryResult) { it?.isRequestInProgress }

    var errorClick = View.OnClickListener {
        isLoading.value = true
        loadDeliveries()
    }

    var selectedDelivery: MutableLiveData<Delivery> = MutableLiveData()
    var deliveryListAdapter: DeliveryListAdapter? = DeliveryListAdapter(this)
    val isLoading: MutableLiveData<Boolean> = MutableLiveData()

    init {
        loadDeliveries()
    }

    fun loadDeliveries() {
        startLoading()
        deliveryResult.postValue(dataManager.doLoad())
    }

    override fun onDeliverySelected(delivery: Delivery) {
        selectedDelivery.postValue(delivery)
    }

    // Pull to refresh
    fun onRefresh() {
        startLoading()
        deliveryResult.postValue(dataManager.doRefresh())
    }

    private fun startLoading() {
        isLoading.postValue(true)
    }
}
