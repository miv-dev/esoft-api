package com.miv.di

import com.miv.handlers.DemandHandler
import com.miv.handlers.OfferHandler
import com.miv.handlers.EstateHandler
import com.miv.handlers.impl.EstateHandlerImpl
import com.miv.handlers.UserHandler
import com.miv.handlers.impl.DemandHandlerImpl
import com.miv.handlers.impl.OfferHandlerImpl
import com.miv.handlers.impl.UserHandlerImpl
import dagger.Binds
import dagger.Module
import di.ApplicationScope


@Module
interface HandlersModule {
    @[ApplicationScope Binds]
    fun bindUserHandler(userHandler: UserHandlerImpl): UserHandler

    @[ApplicationScope Binds]
    fun bindEstateHandler(estateHandler: EstateHandlerImpl): EstateHandler

    @[ApplicationScope Binds]
    fun bindsOfferHandler(offerHandler: OfferHandlerImpl): OfferHandler

    @[ApplicationScope Binds]
    fun bindsDemandHandler(demandHandler: DemandHandlerImpl): DemandHandler

}