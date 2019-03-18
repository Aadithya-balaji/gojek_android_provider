package com.appoets.gojek.provider.views.foodproviderfragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FoodProviderFragmentBinding
import com.appoets.gojek.provider.views.adapters.FoodProviderAdapter

class FoodProviderFragment : BaseFragment<FoodProviderFragmentBinding>()
{
    lateinit var mViewDataBinding: FoodProviderFragmentBinding
    override fun getLayoutId(): Int  = R.layout.food_provider_fragment
    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

            this.mViewDataBinding = mViewDataBinding as FoodProviderFragmentBinding
            mViewDataBinding.foodProviderAdapter = FoodProviderAdapter(activity)
    }

}