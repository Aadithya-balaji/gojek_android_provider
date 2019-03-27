package com.appoets.xjek.ui.currentorder_fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.base.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentCurrentOrdersBinding
import com.appoets.xjek.adapter.CurrentOrdersAdapter

class CurrentOrderFragment : BaseFragment<FragmentCurrentOrdersBinding>(), CurrentOrderNavigator {


    lateinit var mViewDataBinding: FragmentCurrentOrdersBinding

    override fun getLayoutId(): Int = R.layout.fragment_current_orders

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentCurrentOrdersBinding
        val currentOrderViewModel = ViewModelProviders.of(this).get(CurrentOrderViewModel::class.java)
        currentOrderViewModel.setNavigator(this)
        mViewDataBinding.currentorderviewmodel = currentOrderViewModel

        val currentOrderAdapter = CurrentOrdersAdapter(activity)
        mViewDataBinding.currrentOrdersAdapter = currentOrderAdapter


    }


    override fun goToDetailPage() {
    }

}