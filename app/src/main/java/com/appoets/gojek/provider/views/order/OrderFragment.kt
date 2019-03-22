package com.appoets.gojek.provider.views.order

import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentOrderBinding
import com.appoets.xjek.ui.currentorder_fragment.CurrentOrderFragment
import com.appoets.xjek.ui.pastorder_fragment.PastOrderFragment

class OrderFragment : BaseFragment<FragmentOrderBinding>(), OrderNavigator {


    private lateinit var mFragmentOrderBinding: FragmentOrderBinding
    private var mOrderViewModel: OrderViewModel? = null

    override fun getLayoutId(): Int = R.layout.fragment_order


    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mFragmentOrderBinding = mViewDataBinding as FragmentOrderBinding
        mOrderViewModel = OrderViewModel()
        mOrderViewModel?.let { it.setNavigator(this) }
        mFragmentOrderBinding.ordermodel = mOrderViewModel
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.order_container, PastOrderFragment())?.commit()

    }


    override fun getCurrentOrder() {

        mFragmentOrderBinding.tvOrderPast.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_unselected)
        mFragmentOrderBinding.tvOrderPast.setTextColor(ContextCompat.getColor(this.activity!!, R.color.xuber_black))
        mFragmentOrderBinding.tvOrderCurrent.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_selected)
        mFragmentOrderBinding.tvOrderCurrent.setTextColor(ContextCompat.getColor(this.activity!!, R.color.selected_provider_tc))

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.order_container, CurrentOrderFragment())?.commit()

    }

    override fun getPastOrder() {

        mFragmentOrderBinding.tvOrderPast.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_selected)
        mFragmentOrderBinding.tvOrderPast.setTextColor(ContextCompat.getColor(this.activity!!, R.color.selected_provider_tc))
        mFragmentOrderBinding.tvOrderCurrent.background = ContextCompat.getDrawable(this!!.activity!!, R.drawable.bg_service_unselected)
        mFragmentOrderBinding.tvOrderCurrent.setTextColor(ContextCompat.getColor(this.activity!!, R.color.xuber_black))

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.order_container, PastOrderFragment())?.commit()

    }


}