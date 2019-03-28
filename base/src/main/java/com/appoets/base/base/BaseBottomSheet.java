package com.appoets.base.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseBottomSheet<T extends ViewDataBinding> extends BottomSheetDialogFragment {

    private View rootView;
    private T mViewDataBinding;
    private View view;

    protected abstract void initView(View View);

    public abstract int getLayout();

    public abstract void initView(ViewDataBinding mViewDataBinding);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        initView(mViewDataBinding);
        view = mViewDataBinding.getRoot();
        return view;
    }
}
