package com.assessment.deliverybaseproject.utils

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import com.assessment.deliverybaseproject.model.Delivery
import com.assessment.deliverybaseproject.model.Location
import okio.Okio
import java.nio.charset.StandardCharsets

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
object MockDataUtil {
    private var loc = Location("place", 24.000000, 12.999999)

    @SuppressLint("NewApi")
    fun getResponseFromJson(fileName: String): String {
        if (fileName == "errorResponse")
            return ""

        val inputStream = javaClass.classLoader
            .getResourceAsStream("testdata/$fileName.json")
        val source = Okio.buffer(Okio.source(inputStream))
        return source.readString(StandardCharsets.UTF_8)
    }

    fun getDeliveryStub(): Delivery {
        return Delivery(
            1, "image.com", "Deliver to hukum",
            loc
        )
    }
}