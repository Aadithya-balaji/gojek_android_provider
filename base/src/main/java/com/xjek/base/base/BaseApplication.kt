package com.xjek.base.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.di.BaseComponent
import com.xjek.base.di.DaggerBaseComponent
import com.xjek.base.di.WebServiceModule
import com.xjek.base.utils.LocaleUtils

open class BaseApplication : Application() {

    val baseComponent: BaseComponent = DaggerBaseComponent.builder()
            .webServiceModule(WebServiceModule())
            .build()

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleUtils.setLocale(base!!))
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LocaleUtils.setLocale(this)
    }

    override fun onCreate() {
        super.onCreate()
        baseApplication = this
        PreferencesHelper.setDefaultPreferences(this)
    }

    companion object {
        private lateinit var baseApplication: Context

        val getBaseApplicationContext: Context
            get() = baseApplication
    }
}