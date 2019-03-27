package com.appoets.base.base;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;

@Singleton
public class BaseRespositary {
    @Inject
    public Retrofit retrofit;

    public BaseRespositary(){
        BaseApplication.getBaseComponent().inject(this);
    }

    protected <T> T  getApiClient(Class<T> service){
        return retrofit.create(service);
    }

}
