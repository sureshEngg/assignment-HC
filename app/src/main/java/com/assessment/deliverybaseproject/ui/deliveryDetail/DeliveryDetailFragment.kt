package com.assessment.deliverybaseproject.ui.deliveryDetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.assessment.deliverybaseproject.databinding.FragmentDeliveryDetailBinding
import com.assessment.deliverybaseproject.ui.main.DeliveryActivity
import com.google.android.gms.maps.MapView

class DeliveryDetailFragment : androidx.fragment.app.Fragment() {

    private lateinit var mMapView: MapView
    private lateinit var viewModel: DeliveryDetailViewModel
    lateinit var dataBinding: FragmentDeliveryDetailBinding
    private lateinit var parentActivity: DeliveryActivity

    companion object {
        fun newInstance(): DeliveryDetailFragment {
            return DeliveryDetailFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentDeliveryDetailBinding.inflate(parentActivity.layoutInflater)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this.parentActivity)
            .get(DeliveryDetailViewModel::class.java)
        dataBinding.viewModel = viewModel
        dataBinding.lifecycleOwner = this
        mMapView = dataBinding.map
        mMapView.run {
            onCreate(savedInstanceState)
            onResume()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        parentActivity = activity as DeliveryActivity
    }

    override fun onResume() {
        super.onResume()
        parentActivity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mMapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView.onLowMemory()
    }
}