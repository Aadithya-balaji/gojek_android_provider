package com.appoets.gojek.provider.views.splash;

import com.appoets.basemodule.base.BaseViewModel;

class SplashViewModel extends BaseViewModel<SplashNavigator> {

    void moveToNextScreen() {
        getNavigator().moveToHome();
    }
}