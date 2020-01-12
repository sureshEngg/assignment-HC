package com.assessment.deliverybaseproject.utils

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.assessment.deliverybaseproject.BuildConfig
import com.assessment.deliverybaseproject.model.Location
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions

/**
 * This is to provide contract between View and ViewModel. Change in data needs to be reflected on xml view.
 */
@BindingAdapter("adapter")
fun setAdapter(
    view: androidx.recyclerview.widget.RecyclerView?,
    adapter: androidx.recyclerview.widget.RecyclerView.Adapter<*>?
) {
    view?.adapter = adapter
}

@SuppressLint("CheckResult")
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    val context = imageView.context
    Glide.with(context).load(url)
        .into(imageView)
}

@BindingAdapter("initMap")
fun initMap(mapView: MapView?, loc: Location) {
    if (mapView != null) {
        mapView.onCreate(Bundle())
        mapView.getMapAsync { googleMap ->
            mapView.contentDescription = loc.address
            val marker = MarkerOptions().position(loc.latLong).title(loc.address)
            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
            // adding marker
            googleMap.addMarker(marker)
            val cameraPosition = CameraPosition.Builder()
                .target(loc.latLong).zoom(BuildConfig.ZOOM_LEVEL).build()
            googleMap.animateCamera(
                CameraUpdateFactory
                    .newCameraPosition(cameraPosition)
            )
        }
    }
}