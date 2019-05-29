package com.gox.base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseBottomSheet<T : ViewDataBinding> : BottomSheetDialogFragment() {

    private var mViewDataBinding: T? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    abstract fun initView(mViewDataBinding: ViewDataBinding?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        initView(mViewDataBinding)
        return view
    }
}
