package com.appoets.basemodule.base;

import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    private BaseActivity mActivity;
    private T mViewDataBinding;

    @LayoutRes
    public abstract int getLayoutId();

    protected abstract void initView(View mRootView,ViewDataBinding mViewDataBinding);


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            this.mActivity = (BaseActivity) context;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(mViewDataBinding.getRoot(), mViewDataBinding);
    }

    public boolean isNetworkConnected() {
        return mActivity != null && mActivity.isNetworkConnected();
    }

     public void showKeyboard(){
         if (mActivity != null) {
             mActivity.showKeyboard();
         }
    }

    public void hideKeyboard(){
        if (mActivity != null) {
            mActivity.hideKeyboard();
        }
    }


    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    protected void setBindingVariable(int variableId,@Nullable Object object){
        mViewDataBinding.setVariable(variableId,object);
        mViewDataBinding.executePendingBindings();
    }

}
