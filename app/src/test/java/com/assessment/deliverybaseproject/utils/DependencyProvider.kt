package com.assessment.deliverybaseproject.utils

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import com.assessment.deliverybaseproject.repo.web.DeliveryApi
import okhttp3.mockwebserver.MockWebServer
import okio.Okio
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.nio.charset.StandardCharsets

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
object DependencyProvider {

    @SuppressLint("NewApi")
    fun getResponseFromJson(fileName: String): String {
        if (fileName == "errorResponse")
            return ""

        val inputStream = javaClass.classLoader
            .getResourceAsStream("testdata/$fileName.json")
        val source = Okio.buffer(Okio.source(inputStream))
        return source.readString(StandardCharsets.UTF_8)
    }

    fun provideRetrofit(mockWebServer: MockWebServer): DeliveryApi {
        return Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(DeliveryApi::class.java)
    }
}