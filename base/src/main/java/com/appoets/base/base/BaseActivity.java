package com.appoets.base.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.appoets.base.utils.NetworkUtils;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public abstract class BaseActivity<T extends ViewDataBinding> extends AppCompatActivity {

    private T mViewDataBinding;

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView(ViewDataBinding mViewDataBinding);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initView(mViewDataBinding);
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
