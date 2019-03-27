package com.appoets.gojek.provider.views.onboard;

import com.appoets.base.base.BaseViewModel;

public class OnBoardViewModel extends BaseViewModel<OnBoardNavigator> {

    public void openSignIn() {
        getNavigator().goToSignIn();
    }

    public void openSignUp() {
        getNavigator().goToSignUp();
    }

}
