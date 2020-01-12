package com.assessment.deliverybaseproject.model

import com.google.android.gms.maps.model.LatLng

class Location(var address: String, var lat: Double, var lng: Double) {
    val latLong: LatLng
        get() {
            return LatLng(lat, lng)
        }

    override fun toString(): String {
        return "Location{" +
                "address = '" + address + '\''.toString() +
                ",lng = '" + lng + '\''.toString() +
                ",lat = '" + lat + '\''.toString() +
                "}"
    }
}
