package com.xjek.provider.views.xuberServicesProviderFragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.views.adapters.XuberServicesProviderAdapter

class XuberServicesProviderFragment : BaseFragment<XuberserviceProviderFragmentBinding>() {
    lateinit var mViewDataBinding: XuberserviceProviderFragmentBinding
    override fun getLayoutId(): Int = R.layout.xuberservice_provider_fragment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as XuberserviceProviderFragmentBinding
        mViewDataBinding.xuberServicesProviderAdapter = XuberServicesProviderAdapter(activity)


    }

}