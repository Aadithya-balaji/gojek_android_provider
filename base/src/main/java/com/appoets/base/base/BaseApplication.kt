package com.appoets.base.base

import android.app.Application
import com.appoets.base.di.BaseComponent
import com.appoets.base.di.DaggerBaseComponent
import com.appoets.base.di.WebServiceModule

open class BaseApplication : Application() {

    val baseComponent: BaseComponent = DaggerBaseComponent.builder()
            .webServiceModule(WebServiceModule())
            .build()
}