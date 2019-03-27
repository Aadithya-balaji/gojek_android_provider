package com.appoets.xjek.ui.pastorder_fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentPastOrdersBinding
import com.appoets.xjek.adapter.PastOrdersAdapter

class PastOrderFragment : BaseFragment<FragmentPastOrdersBinding>(), PastOrderNavigator {


    lateinit var mViewDataBinding: FragmentPastOrdersBinding
    override fun getLayoutId(): Int = R.layout.fragment_past_orders;

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentPastOrdersBinding
        val pastOrderViewModel = PastOrderViewModel()
        pastOrderViewModel.setNavigator(this)
        mViewDataBinding.pastfragmentviewmodel = pastOrderViewModel
        val pastOrdersAdapter = PastOrdersAdapter(activity)
        mViewDataBinding.pastOrdersAdapter = pastOrdersAdapter
    }

    override fun gotoDetailPage() {

    }

}