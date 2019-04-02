package com.xjek.xjek.ui.pastorder_fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentPastOrdersBinding
import com.xjek.provider.views.adapters.PastOrdersAdapter

class PastOrderFragment : BaseFragment<FragmentPastOrdersBinding>(), PastOrderNavigator {


    lateinit var mViewDataBinding: FragmentPastOrdersBinding
    override fun getLayoutId(): Int = R.layout.fragment_past_orders;

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentPastOrdersBinding
        val pastOrderViewModel = PastOrderViewModel()
        mViewDataBinding.pastfragmentviewmodel = pastOrderViewModel
        val pastOrdersAdapter = PastOrdersAdapter(activity)
        mViewDataBinding.pastOrdersAdapter = pastOrdersAdapter
    }

    override fun gotoDetailPage() {

    }

}