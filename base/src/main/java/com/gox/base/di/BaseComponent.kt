package com.gox.base.di

import com.gox.base.repository.BaseRepository
import com.gox.base.utils.RunTimePermission
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [WebServiceModule::class, AppContainerModule::class])
interface BaseComponent {
    fun inject(baseRepository: BaseRepository)

    fun inject(runtimePermission: RunTimePermission)
}