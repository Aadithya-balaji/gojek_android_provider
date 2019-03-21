package com.appoets.basemodule.di.component;

import com.appoets.basemodule.base.BaseRespositary;
import com.appoets.basemodule.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface BaseComponent {
    void inject(BaseRespositary respositary);
}
