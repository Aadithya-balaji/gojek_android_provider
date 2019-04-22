package com.xjek.base.base

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.facebook.stetho.Stetho
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.di.BaseComponent
import com.xjek.base.di.DaggerBaseComponent
import com.xjek.base.di.WebServiceModule
import com.xjek.base.utils.LocaleUtils

open class BaseApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleUtils.setLocale(base!!))
    }

    var baseComponent = DaggerBaseComponent.builder()
            .webServiceModule(WebServiceModule())
            .build()

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        LocaleUtils.setLocale(this)
    }

    override fun onCreate() {
        super.onCreate()
        baseApplication = this
        appController = baseComponent
        Stetho.initializeWithDefaults(this)
        PreferencesHelper.setDefaultPreferences(this)
    }

    companion object {
        private lateinit var baseApplication: Context
        var appController: BaseComponent? = null
        val getBaseApplicationContext: Context
            get() = baseApplication
    }
}