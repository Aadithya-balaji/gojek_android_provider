package com.appoets.base.di

import com.appoets.base.repository.BaseRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [WebServiceModule::class])
interface BaseComponent {
    fun inject(baseRepository: BaseRepository)
}