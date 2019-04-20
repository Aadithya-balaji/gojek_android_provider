package com.xjek.base.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.DialogFragment;

public abstract class BaseDialogFragment <T extends ViewDataBinding> extends DialogFragment {

    public  abstract  int getLayout();
    public  abstract  void initView(ViewDataBinding viewDataBinding,View view);
    private T mViewDataBinding;
    private  View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater,getLayout(), container, false);
        view=mViewDataBinding.getRoot();
        initView(mViewDataBinding,view);
        return view;

    }
}
