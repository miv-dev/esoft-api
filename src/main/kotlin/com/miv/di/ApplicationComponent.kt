package com.miv.di

import com.miv.plugins.AppRouting
import dagger.Component
import di.ApplicationScope

@ApplicationScope
@Component(
    modules = [HandlersModule::class, ServiceModule::class]
)
interface ApplicationComponent{

    @Component.Factory
    interface Factory {
        fun create(
        ): ApplicationComponent
    }

    fun routingFactory(): AppRouting.Factory
}