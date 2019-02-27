package com.appoets.basemodule.base;

import androidx.lifecycle.ViewModel;


import java.lang.ref.WeakReference;

import javax.inject.Inject;


public abstract class BaseViewModel<N>  extends ViewModel {
    private WeakReference<N> mNavigator;

    @Inject
    BaseRespositary respositary;

    protected N getNavigator() {
        return mNavigator.get();
    }

    public void setNavigator(N navigator) {
        this.mNavigator = new WeakReference<>(navigator);
    }




}
