package com.appoets.gojek.traximodule.views.views.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract  class BaseBottomSheet : BottomSheetDialogFragment(){
    abstract fun getLayoutId(): Int
    abstract fun initView(view: View)
    private  var rootView:View?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (rootView == null)
        {
            rootView = inflater.inflate(getLayoutId(), container, false)
            initView(rootView!!)
        }

        return rootView
    }


}
