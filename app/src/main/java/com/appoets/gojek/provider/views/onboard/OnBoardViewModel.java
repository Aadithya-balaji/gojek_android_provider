package com.appoets.gojek.provider.views.onboard;

import com.appoets.basemodule.base.BaseViewModel;
import com.appoets.xjek.data.remote.repositary.GoJekRepositary;

public class OnBoardViewModel extends BaseViewModel<OnBoardNavigator> {

    public void openSignIn() {
        getNavigator().goToSignIn();
    }

    public void openSignUp() {
        getNavigator().goToSignUp();
    }

    public void getEmployess() {
        GoJekRepositary.getINSTANCE().loadEmployeeData();
    }
}
