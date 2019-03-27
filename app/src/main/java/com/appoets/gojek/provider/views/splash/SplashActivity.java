package com.appoets.gojek.provider.views.splash;

import android.os.Handler;

import com.appoets.base.base.BaseActivity;
import com.appoets.gojek.provider.R;
import com.appoets.gojek.provider.databinding.ActivitySplashBinding;
import com.appoets.xjek.ui.onboard.OnBoardActivityK;

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
        new Handler().postDelayed(() -> openNewActivity(SplashActivity.this, OnBoardActivityK.class, true), 3000);
    }
}
