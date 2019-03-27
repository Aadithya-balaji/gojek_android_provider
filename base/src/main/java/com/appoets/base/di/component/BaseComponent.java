package com.appoets.base.di.component;

import com.appoets.base.base.BaseRespositary;
import com.appoets.base.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface BaseComponent {
    void inject(BaseRespositary respositary);
}
