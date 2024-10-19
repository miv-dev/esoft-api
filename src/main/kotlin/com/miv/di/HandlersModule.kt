package com.miv.di

import com.miv.db.DatabaseConfig
import com.miv.db.DatabaseManager
import com.miv.handlers.UserHandler
import com.miv.handlers.impl.UserHandlerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import di.ApplicationScope


@Module
interface HandlersModule {
    @[ApplicationScope Binds]
    fun bindUserHandler(userHandler: UserHandlerImpl): UserHandler


}