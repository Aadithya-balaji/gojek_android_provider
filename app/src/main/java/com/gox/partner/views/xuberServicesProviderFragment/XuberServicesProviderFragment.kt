package com.gox.partner.views.xuberServicesProviderFragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseFragment
import com.gox.partner.R
import com.gox.partner.databinding.XuberserviceProviderFragmentBinding
import com.gox.partner.views.adapters.XuberServicesProviderAdapter

class XuberServicesProviderFragment : BaseFragment<XuberserviceProviderFragmentBinding>() {
    lateinit var mViewDataBinding: XuberserviceProviderFragmentBinding
    override fun getLayoutId(): Int = R.layout.xuberservice_provider_fragment

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as XuberserviceProviderFragmentBinding
        mViewDataBinding.xuberServicesProviderAdapter = XuberServicesProviderAdapter(activity)


    }

}