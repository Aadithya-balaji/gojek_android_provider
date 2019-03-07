package com.appoets.gojek.provider.views.splash;

import android.os.Handler;

import com.appoets.basemodule.base.BaseActivity;
import com.appoets.gojek.provider.R;
import com.appoets.gojek.provider.databinding.ActivitySplashBinding;
import com.appoets.gojek.provider.views.dashboard.DashBoardActivity;
import com.appoets.xjek.ui.onboard.OnBoardActivityK;
import com.appoets.xjek.ui.signin.SignInActivity;
import com.google.android.gms.maps.model.Dash;

import androidx.databinding.ViewDataBinding;
//import com.appoets.xjek.ui.onboard.OnBoardActivityK;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> implements SplashNavigator {

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(ViewDataBinding mViewDataBinding) {
        SplashViewModel splashViewModel = new SplashViewModel();
        splashViewModel.setNavigator(this);
        splashViewModel.moveToNextScreen();
    }

    @Override
    public void moveToHome() {
        new Handler().postDelayed(() -> openNewActivity(SplashActivity.this, DashBoardActivity.class, true), 3000);
    }
}
