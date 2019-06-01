package com.gox.base.base

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.lifecycle.MutableLiveData
import com.facebook.stetho.Stetho
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesHelper
import com.gox.base.di.BaseComponent
import com.gox.base.di.DaggerBaseComponent
import com.gox.base.di.WebServiceModule
import com.gox.base.utils.LocaleUtils
import com.gox.monitorinternet.InternetConnectivityListener
import com.gox.monitorinternet.MonitorInternet
import com.testfairy.TestFairy

open class BaseApplication : Application(), InternetConnectivityListener {

    private var mMonitorInternet: MonitorInternet? = null

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleUtils.setLocale(base!!))
    }

    var baseComponent: BaseComponent = DaggerBaseComponent.builder()
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
        MonitorInternet.init(this)
        mMonitorInternet = MonitorInternet.instance!!
        mMonitorInternet!!.addInternetConnectivityListener(this)
        Stetho.initializeWithDefaults(this)
        PreferencesHelper.setDefaultPreferences(this)
        preferences = getSharedPreferences(Constants.CUSTOM_PREFERENCE, Context.MODE_PRIVATE)
        TestFairy.begin(this, "SDK-OHDYC1Nx")
    }

    companion object {
        private lateinit var baseApplication: Context
        private lateinit var preferences: SharedPreferences
        val getCustomPreference: SharedPreferences? get() = preferences
        var appController: BaseComponent? = null
        val getBaseApplicationContext: Context get() = baseApplication
        private val monitorInternetLiveData = MutableLiveData<Boolean>()
        val getInternetMonitorLiveData: MutableLiveData<Boolean> get() = monitorInternetLiveData
    }


    override fun onLowMemory() {
        super.onLowMemory()
        MonitorInternet.instance!!.removeAllInternetConnectivityChangeListeners()
    }

    override fun onInternetConnectivityChanged(isConnected: Boolean) {
        println("isConnected = $isConnected")
        getInternetMonitorLiveData.value = isConnected
    }
}