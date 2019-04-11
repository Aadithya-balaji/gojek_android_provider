package com.xjek.base.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xjek.base.views.CustomDialog;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public abstract class BaseFragment<T extends ViewDataBinding> extends Fragment {

    private FragmentActivity mActivity;
    private T mViewDataBinding;
    private MutableLiveData loadingLiveData = new MutableLiveData<Boolean>();
    private CustomDialog customDialog;


    @LayoutRes
    public abstract int getLayoutId();

    protected abstract void initView(View mRootView, ViewDataBinding mViewDataBinding);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(mViewDataBinding.getRoot(), mViewDataBinding);
        customDialog = new CustomDialog(mActivity);
        loadingLiveData.observe(getActivity(), new Observer() {
            @Override
            public void onChanged(Object o) {
                /*if(loadingLiveData.getValue()==true){
                    showLoading();
                }else{
                    hideLoading();
                }*/
            }
        });

    }

    protected void setBindingVariable(int variableId, @Nullable Object object) {
        mViewDataBinding.setVariable(variableId, object);
        mViewDataBinding.executePendingBindings();
    }

    protected void launchNewActivity(Class<?> cls, boolean shouldCloseActivity) {
        startActivity(new Intent(mActivity.getApplicationContext(), cls));
        if (shouldCloseActivity)
            mActivity.finish();
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    protected void showLoading() {
        if (customDialog.getWindow() != null) {
            customDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            customDialog.show();
        }
    }

    protected void hideLoading() {
        customDialog.cancel();
    }


}