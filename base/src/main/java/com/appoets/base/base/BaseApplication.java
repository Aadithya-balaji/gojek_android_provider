package com.appoets.base.base;

import android.app.Application;

import com.appoets.base.di.component.BaseComponent;
import com.appoets.base.di.component.DaggerBaseComponent;
import com.appoets.base.di.modules.NetworkModule;

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
