package com.appoets.base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.appoets.base.utils.NetworkUtils;
import com.xjek.basemodule.ui.CustomDialog;

import java.util.Objects;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

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

    }

    protected void showLoading() {
        if (customDialog != null) {
            Objects.requireNonNull(customDialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
            customDialog.show();
        }
    }

    protected void hideLoading() {
        if (customDialog != null)
            customDialog.cancel();
    }

    protected void showKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    protected void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected void openNewActivity(Activity activity, Class<?> cls, boolean finishCurrent) {
        Intent intent = new Intent(activity, cls);
        startActivity(intent);
        if (finishCurrent) activity.finish();
    }

    protected boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    protected void setBindingVariable(int variableId, @Nullable Object object) {
        mViewDataBinding.setVariable(variableId, object);
        mViewDataBinding.executePendingBindings();
    }

}
