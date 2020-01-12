package com.assessment.deliverybaseproject.di.component

import android.app.Application
import com.assessment.deliverybaseproject.base.BaseApplication
import com.assessment.deliverybaseproject.di.builders.FragmentBuilder
import com.assessment.deliverybaseproject.di.module.ConnectivityModule
import com.assessment.deliverybaseproject.di.module.DatabaseModule
import com.assessment.deliverybaseproject.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, FragmentBuilder::class,
        ConnectivityModule::class, DatabaseModule::class, NetworkModule::class]
)
interface AppComponent {

    fun inject(app: BaseApplication)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}