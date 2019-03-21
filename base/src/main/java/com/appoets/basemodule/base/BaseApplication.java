package com.appoets.basemodule.base;

import android.app.Application;

import com.appoets.basemodule.di.component.BaseComponent;
import com.appoets.basemodule.di.component.DaggerBaseComponent;
import com.appoets.basemodule.di.modules.NetworkModule;

public class BaseApplication extends Application {

    private static BaseComponent baseComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        baseComponent = DaggerBaseComponent.builder()
                .networkModule(new NetworkModule())
                .build();
    }

    public static BaseComponent getBaseComponent() {
        return baseComponent;
    }
}
