package com.xjek.base.di

import com.xjek.base.repository.BaseRepository
import com.xjek.base.utils.RunTimePermission
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [WebServiceModule::class,AppContainerModule::class])
interface BaseComponent {
    fun inject(baseRepository: BaseRepository)

    fun inject(runtimePermission:RunTimePermission)
}