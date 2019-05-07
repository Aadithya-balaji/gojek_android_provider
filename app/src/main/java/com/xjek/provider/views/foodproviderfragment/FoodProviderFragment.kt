package com.xjek.provider.views.foodproviderfragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.FoodProviderFragmentBinding
import com.xjek.provider.views.adapters.FoodProviderAdapter

class FoodProviderFragment : BaseFragment<FoodProviderFragmentBinding>() {
    lateinit var mViewDataBinding: FoodProviderFragmentBinding
    override fun getLayoutId(): Int = R.layout.food_provider_fragment
    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FoodProviderFragmentBinding
        mViewDataBinding.foodProviderAdapter = FoodProviderAdapter(activity)
    }

}