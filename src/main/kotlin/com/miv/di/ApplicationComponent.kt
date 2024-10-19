package com.miv.di

import com.miv.db.DatabaseConfig
import com.miv.db.DatabaseManager
import com.miv.plugins.AppRouting
import dagger.BindsInstance
import dagger.Component
import dagger.Provides
import di.ApplicationScope

@ApplicationScope
@Component(
    modules = [HandlersModule::class, ServiceModule::class]
)
interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
        ): ApplicationComponent
    }

    fun routingFactory(): AppRouting.Factory
}