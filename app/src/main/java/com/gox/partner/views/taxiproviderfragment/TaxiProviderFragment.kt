package com.gox.partner.views.taxiproviderfragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseFragment
import com.gox.partner.R
import com.gox.partner.databinding.TaxiProviderFragmentBinding
import com.gox.partner.views.adapters.TaxiProviderAdapter

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