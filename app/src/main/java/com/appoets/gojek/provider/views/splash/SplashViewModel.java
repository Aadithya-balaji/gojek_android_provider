package com.appoets.gojek.provider.views.splash;

import com.appoets.base.base.BaseViewModel;

class SplashViewModel extends BaseViewModel<SplashNavigator> {

    void moveToNextScreen() {
        getNavigator().moveToHome();
    }
}