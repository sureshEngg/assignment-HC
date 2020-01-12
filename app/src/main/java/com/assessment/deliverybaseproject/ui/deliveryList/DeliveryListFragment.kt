package com.assessment.deliverybaseproject.ui.deliveryList

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.assessment.deliverybaseproject.R
import com.assessment.deliverybaseproject.databinding.FragmentDeliveryListBinding
import com.assessment.deliverybaseproject.di.ViewModelProviderFactory
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.repo.paging.DeliveryPagingCallback
import com.assessment.deliverybaseproject.ui.deliveryDetail.DeliveryDetailFragment
import com.assessment.deliverybaseproject.ui.deliveryDetail.DeliveryDetailViewModel
import com.assessment.deliverybaseproject.ui.main.DeliveryActivity
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import java.net.UnknownHostException
import javax.inject.Inject

class DeliveryListFragment : androidx.fragment.app.Fragment() {

    @Inject
    lateinit var factory: ViewModelProviderFactory
    private lateinit var parentActivity: DeliveryActivity
    private lateinit var dataBinding: FragmentDeliveryListBinding
    private lateinit var viewModel: DeliveryListViewModel
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)
        viewModel = ViewModelProviders.of(this, factory)
            .get(DeliveryListViewModel::class.java)
        dataBinding = FragmentDeliveryListBinding.inflate(parentActivity.layoutInflater)
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this
        setListView()
        observeLiveData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return dataBinding.root
    }

    private fun observeLiveData() {
        viewModel.networkErrors.observe(parentActivity, Observer<DeliveryPagingCallback.NetworkError> {
            hideProgressBar()
            if (it != null) onNetworkError(it) else hideError()
        })

        viewModel.deliveries.observe(parentActivity, Observer {
            if (it != null && it.size > 0) {
                hideProgressBar()
                viewModel.deliveryListAdapter?.submitList(it)
            }
        })

        viewModel.isRequestInProgress.observe(parentActivity, Observer {
            viewModel.deliveryListAdapter?.setNetworkProgress(it)
        })

        viewModel.selectedDelivery.observe(parentActivity, Observer {
            onSelectedDelivery(it)
        })
    }

    override fun onResume() {
        super.onResume()
        parentActivity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        parentActivity = activity as DeliveryActivity
    }

    @SuppressLint("WrongConstant")
    private fun setListView() {
        dataBinding.rvDeliveryList.addItemDecoration(
            androidx.recyclerview.widget.DividerItemDecoration(
                parentActivity,
                androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
            )
        )
        dataBinding.rvDeliveryList.layoutManager = LinearLayoutManager(
            parentActivity, LinearLayoutManager.VERTICAL, false
        )
    }

    private fun onSelectedDelivery(delivery: Delivery) {
        val detailsViewModel = ViewModelProviders.of(parentActivity)
            .get<DeliveryDetailViewModel>(DeliveryDetailViewModel::class.java)
        detailsViewModel.setSelectedDeliveryDetailInfo(delivery)
        navigateToDeliveryDetail()
    }

    private fun navigateToDeliveryDetail() {
        hideError()
        parentActivity.supportFragmentManager.beginTransaction().replace(
            R.id.fl_container,
            DeliveryDetailFragment.newInstance()
        ).addToBackStack(null).commit()
    }

    private fun hideProgressBar() {
        viewModel.isLoading.value = false
    }

    private fun displayNetworkError(error: String) {
        snackBar = Snackbar.make(dataBinding.root, error, Snackbar.LENGTH_INDEFINITE)
        snackBar?.setAction(R.string.retry, viewModel.errorClick)
        snackBar?.show()
    }

    private fun hideError() {
        snackBar?.dismiss()
    }

    private fun onNetworkError(networkError: DeliveryPagingCallback.NetworkError) {
        if (networkError.error is UnknownHostException) {
            displayNetworkError(getString(R.string.error_no_network))
        } else {
            displayLoadingError(networkError)
        }
    }

    private fun displayLoadingError(networkError: DeliveryPagingCallback.NetworkError) {
        snackBar = Snackbar.make(
            dataBinding.root, parentActivity.getString(R.string.error_loading_failed),
            Snackbar.LENGTH_INDEFINITE
        )
        snackBar?.setAction(R.string.retry) {
            if (viewModel.deliveries.value?.size == 0) {
                viewModel.isLoading.value = true
            }
            networkError.retry()
        }
        snackBar?.show()
    }

    companion object {
        fun newInstance(): DeliveryListFragment {
            return DeliveryListFragment()
        }
    }
}