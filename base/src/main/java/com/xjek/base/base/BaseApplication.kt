package com.xjek.base.base

import android.app.Application
import com.xjek.base.di.BaseComponent
import com.xjek.base.di.DaggerBaseComponent
import com.xjek.base.di.WebServiceModule

open class BaseApplication : Application() {

    val baseComponent: BaseComponent = DaggerBaseComponent.builder()
            .webServiceModule(WebServiceModule())
            .build()
}