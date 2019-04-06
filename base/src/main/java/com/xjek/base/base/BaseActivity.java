package com.xjek.base.base;

import android.content.Intent;
import android.os.Bundle;

import com.xjek.base.utils.NetworkUtils;
import com.xjek.base.views.CustomDialog;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    private MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private T mViewDataBinding;
    private CustomDialog customDialog;

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(ViewDataBinding mViewDataBinding);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initView(mViewDataBinding);
        customDialog = new CustomDialog(this);

        loadingLiveData.observe(this, showLoading -> {
            if (showLoading)
                showLoading();
            else
                hideLoading();
        });
    }

    protected void setBindingVariable(int variableId, @Nullable Object object) {
        mViewDataBinding.setVariable(variableId, object);
        mViewDataBinding.executePendingBindings();
    }

    protected MutableLiveData getLoadingObservable() {
        return loadingLiveData;
    }

    protected void showLoading() {
        if (customDialog != null && customDialog.getWindow() != null) {
            customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            customDialog.show();
        }
    }

    protected void hideLoading() {
        if (customDialog != null)
            customDialog.cancel();
    }

    protected boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    protected void launchNewActivity(Class<?> cls, boolean shouldCloseActivity) {
        startActivity(new Intent(getApplicationContext(), cls));
        if (shouldCloseActivity)
            finish();
    }

    protected void launchNewActivity(Intent intent, boolean shouldCloseActivity) {
        startActivity(intent);
        if (shouldCloseActivity)
            finish();
    }

    protected void replaceExistingFragment(@IdRes int containerViewId, Fragment fragment,
                                           String tag, boolean doRememberTransaction) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment, tag);
        if (doRememberTransaction)
            transaction.addToBackStack(tag);
        transaction.commit();
    }
}
