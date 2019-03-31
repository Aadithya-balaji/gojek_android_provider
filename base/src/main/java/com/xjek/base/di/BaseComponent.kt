package com.xjek.base.di

import com.xjek.base.repository.BaseRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [WebServiceModule::class])
interface BaseComponent {
    fun inject(baseRepository: BaseRepository)
}