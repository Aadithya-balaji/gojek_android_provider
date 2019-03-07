package com.appoets.basemodule.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appoets.basemodule.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;

public abstract class BaseBottomSheet<T extends ViewDataBinding> extends BottomSheetDialogFragment {

    protected  abstract  void initView(View View);
    private  View rootView;
    private  T mViewDataBinding;
    private  View view;

    public  abstract  int getLayout();
    public  abstract  void initView(ViewDataBinding mViewDataBinding);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater,getLayout(), container, false);
        initView(mViewDataBinding);
        view=mViewDataBinding.getRoot();
        return view;
    }
}
