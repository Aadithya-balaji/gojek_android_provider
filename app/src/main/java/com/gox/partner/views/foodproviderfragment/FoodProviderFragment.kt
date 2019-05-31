package com.gox.partner.views.foodproviderfragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseFragment
import com.gox.partner.R
import com.gox.partner.databinding.FoodProviderFragmentBinding
import com.gox.partner.views.adapters.FoodProviderAdapter

class FoodProviderFragment : BaseFragment<FoodProviderFragmentBinding>() {
    lateinit var mViewDataBinding: FoodProviderFragmentBinding
    override fun getLayoutId(): Int = R.layout.food_provider_fragment
    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FoodProviderFragmentBinding
        mViewDataBinding.foodProviderAdapter = FoodProviderAdapter(activity)
    }

}