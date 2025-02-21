package com.miv.di

import com.miv.handlers.*
import com.miv.handlers.impl.*
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


    @[ApplicationScope Binds]
    fun bindsDealHandler(dealHandler: DealHandlerImpl): DealHandler

    @[ApplicationScope Binds]
    fun bindsEventHandler(eventHandler: EventHandlerImpl): EventHandler
}