package com.miv.di

import com.miv.services.*
import com.miv.services.impl.*
import dagger.Binds
import dagger.Module
import di.ApplicationScope


@Module
interface ServiceModule {
    @[ApplicationScope Binds]
    fun bindsUserService(impl: UserServiceImpl): UserService

    @[ApplicationScope Binds]
    fun bindsImportService(impl: ImportServiceImpl): ImportService

    @[ApplicationScope Binds]
    fun bindsProfileService(impl: ProfileServiceImpl): ProfileService

    @[ApplicationScope Binds]
    fun bindsSearchService(impl: SearchServiceImpl): SearchService

    @[ApplicationScope Binds]
    fun bindsDistrictService(impl: DistrictServiceImpl): DistrictService

    @[ApplicationScope Binds]
    fun bindsEstateService(impl: EstateServiceImpl): EstateService

    @[ApplicationScope Binds]
    fun bindsOfferService(impl: OfferServiceImpl): OfferService

    @[ApplicationScope Binds]
    fun bindsDemandService(impl: DemandServiceImpl): DemandService
}