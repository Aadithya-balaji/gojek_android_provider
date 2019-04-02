package com.xjek.xjek.ui.currentorder_fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentCurrentOrdersBinding
import com.xjek.provider.views.adapters.CurrentOrdersAdapter

class CurrentOrderFragment : BaseFragment<FragmentCurrentOrdersBinding>(), CurrentOrderNavigator {


    lateinit var mViewDataBinding: FragmentCurrentOrdersBinding

    override fun getLayoutId(): Int = R.layout.fragment_current_orders

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentCurrentOrdersBinding
        val currentOrderViewModel = ViewModelProviders.of(this).get(CurrentOrderViewModel::class.java)
        mViewDataBinding.currentorderviewmodel = currentOrderViewModel

        val currentOrderAdapter = CurrentOrdersAdapter(activity)
        mViewDataBinding.currrentOrdersAdapter = currentOrderAdapter


    }


    override fun goToDetailPage() {
    }

}