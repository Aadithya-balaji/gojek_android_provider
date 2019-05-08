package com.xjek.provider.views.taxiproviderfragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.views.adapters.TaxiProviderAdapter

class TaxiProviderFragment : BaseFragment<TaxiProviderFragmentBinding>(), TaxiProviderNavigator {

    lateinit var mViewDataBinding: TaxiProviderFragmentBinding
    override fun getLayoutId(): Int = R.layout.taxi_provider_fragment


    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as TaxiProviderFragmentBinding
        val taxiProviderViewModel = ViewModelProviders.of(this).get(TaxiProviderViewModel::class.java)
        mViewDataBinding.taxiProviderViewModel = taxiProviderViewModel
        mViewDataBinding.taxiProviderAdapter = TaxiProviderAdapter(activity)


    }

}