package com.xjek.base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.xjek.base.utils.NetworkUtils;
import com.xjek.base.views.CustomDialog;

import java.util.Objects;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    private T mViewDataBinding;
    private CustomDialog customDialog;


    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(ViewDataBinding mViewDataBinding);
    protected MutableLiveData<Boolean> baseLiveDataLoading = new MutableLiveData<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initView(mViewDataBinding);
        customDialog = new CustomDialog(this);


        baseLiveDataLoading.observe(this, showLoading -> {
            if (showLoading)
                showLoading();
            else
                hideLoading();
        });
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
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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

    protected void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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


    protected void replaceFragment(@IdRes int id, Fragment fragmentName, String fragmentTag,
                                   boolean addToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(id, fragmentName, fragmentTag);
        if (addToBackStack)
            transaction.addToBackStack(fragmentTag);
        transaction.commit();
    }
}
