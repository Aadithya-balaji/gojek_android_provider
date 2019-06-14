package com.gox.base.di

import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class AppContainerModule {
    @Provides
    @Singleton
    internal fun providesStringArrayList(): ArrayList<String> {
        return ArrayList()
    }
}