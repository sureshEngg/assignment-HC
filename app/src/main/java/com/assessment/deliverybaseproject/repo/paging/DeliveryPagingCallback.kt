package com.assessment.deliverybaseproject.repo.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.assessment.deliverybaseproject.BuildConfig.NETWORK_PAGE_SIZE
import com.assessment.deliverybaseproject.db.cache.DeliveryLocalCache
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

/**
 * This class is responsible to detect the list scrolling and load data in chunks (paging)
 */
class DeliveryPagingCallback internal constructor(
    private val deliveryService: DeliveryApi,
    private val localCache: DeliveryLocalCache
) : PagedList.BoundaryCallback<Delivery>() {

    private var lastRequestedPage = 1
    val isRequestInProgress: MutableLiveData<Boolean> = MutableLiveData()
    val networkErrors = MutableLiveData<NetworkError>()
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var isLoadMore = true

    init {
        isRequestInProgress.postValue(false)
    }

    fun requestAndSaveData() {
        if (!isLoadMore || isRequestInProgress.value!!) return

        isRequestInProgress.postValue(true)

        disposable.add(
            deliveryService.getDeliveries(
                lastRequestedPage,
                NETWORK_PAGE_SIZE
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(
                    object : DisposableSingleObserver<List<Delivery>>() {
                        override fun onSuccess(value: List<Delivery>) {
                            onSuccessResponse(value)
                            isLoadMore = value.isNotEmpty()
                        }

                        override fun onError(e: Throwable) {
                            onErrorResponse(e)
                        }
                    })
        )
    }

    override fun onZeroItemsLoaded() {
        requestAndSaveData()
    }

    override fun onItemAtEndLoaded(itemAtEnd: Delivery) {
        requestAndSaveData()
    }

    fun onSuccessResponse(items: List<Delivery>) {
        localCache.insert(items)
        lastRequestedPage += items.size
        isRequestInProgress.postValue(false)
    }

    fun onErrorResponse(error: Throwable) {
        networkErrors.postValue(NetworkError(error))
        isRequestInProgress.postValue(false)
    }

    inner class NetworkError(error: Throwable) {
        var error: Throwable? = null

        init {
            this.error = error
        }

        fun retry() {
            requestAndSaveData()
        }
    }
}
