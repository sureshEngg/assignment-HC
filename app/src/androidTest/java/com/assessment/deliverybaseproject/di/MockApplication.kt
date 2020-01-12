package com.assessment.deliverybaseproject.di

import android.annotation.SuppressLint
import com.assessment.deliverybaseproject.base.BaseApplication

@SuppressLint("Registered")
class MockApplication : BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        DaggerMockComponent.builder()
            .application(this)
            .build()
            .inject(this)
    }
}
