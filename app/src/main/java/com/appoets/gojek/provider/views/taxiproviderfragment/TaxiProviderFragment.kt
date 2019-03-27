package com.appoets.gojek.provider.views.taxiproviderfragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.base.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.TaxiProviderFragmentBinding
import com.appoets.gojek.provider.views.adapters.TaxiProviderAdapter

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