package com.xjek.provider.views.splash;

import android.os.Handler;

import com.xjek.base.base.BaseActivity;
import com.xjek.provider.R;
import com.xjek.provider.databinding.ActivitySplashBinding;
import com.xjek.provider.views.onboard.OnBoardActivityK;
import androidx.databinding.ViewDataBinding;
//import com.xjek.gojek.provider.views.onboard.OnBoardActivityK;

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
