package com.xjek.provider.views.splash;

import com.xjek.base.base.BaseViewModel;

class SplashViewModel extends BaseViewModel<SplashNavigator> {

    void moveToNextScreen() {
        getNavigator().moveToHome();
    }
}