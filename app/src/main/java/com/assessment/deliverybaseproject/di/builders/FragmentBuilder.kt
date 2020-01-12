package com.assessment.deliverybaseproject.di.builders

import com.assessment.deliverybaseproject.ui.deliveryList.DeliveryListFragment
import dagger.android.ContributesAndroidInjector

@dagger.Module
abstract class FragmentBuilder {
    @ContributesAndroidInjector
    abstract fun bindDeliveryListFragment(): DeliveryListFragment
}