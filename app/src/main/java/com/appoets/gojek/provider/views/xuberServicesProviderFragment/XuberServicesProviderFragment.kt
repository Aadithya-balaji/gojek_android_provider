package com.appoets.gojek.provider.views.xuberServicesProviderFragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.XuberserviceProviderFragmentBinding
import com.appoets.gojek.provider.views.adapters.XuberServicesProviderAdapter

class XuberServicesProviderFragment : BaseFragment<XuberserviceProviderFragmentBinding>() {
    lateinit var mViewDataBinding: XuberserviceProviderFragmentBinding
    override fun getLayoutId(): Int = R.layout.xuberservice_provider_fragment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as XuberserviceProviderFragmentBinding
        mViewDataBinding.xuberServicesProviderAdapter = XuberServicesProviderAdapter(activity)


    }

}