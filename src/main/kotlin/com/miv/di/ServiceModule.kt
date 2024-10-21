package com.miv.di

import com.miv.services.ImportService
import com.miv.services.ProfileService
import com.miv.services.SearchService
import com.miv.services.UserService
import com.miv.services.impl.ImportServiceImpl
import com.miv.services.impl.ProfileServiceImpl
import com.miv.services.impl.SearchServiceImpl
import com.miv.services.impl.UserServiceImpl
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
}