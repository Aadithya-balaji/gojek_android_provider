package com.appoets.gojek.provider.views.order

import android.content.Context
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appoets.basemodule.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentOrderBinding
import com.appoets.gojek.provider.views.adapters.OrderAdpater
import com.appoets.gojek.provider.views.dashboard.DashBoardNavigator

class OrderFragment : BaseFragment<FragmentOrderBinding>(), OrderNavigator {



    private lateinit var mFragmentOrderBinding: FragmentOrderBinding
    private var mOrderViewModel: OrderViewModel? = null
    private lateinit var rvOrders: RecyclerView
    private lateinit var tvPastOrder: TextView
    private lateinit var tvCurrentOrder: TextView
    private var appCompatActivity: AppCompatActivity? = null
    private var dashBoardNavigator: DashBoardNavigator? = null
    private var isCurrentOrder: Boolean = true
    private var linearLayoutManager:LinearLayoutManager?=null
    private  var orderAdpater:OrderAdpater?=null
    private  var isPastOrderSelected:Boolean=false
    private  var isCurrentOrderSelected:Boolean=true

    override fun getLayoutId(): Int = R.layout.fragment_order





    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mFragmentOrderBinding = mViewDataBinding as FragmentOrderBinding
        mOrderViewModel = OrderViewModel()
        mOrderViewModel?.let { it.setNavigator(this) }
        orderAdpater= OrderAdpater()
        mFragmentOrderBinding.ordermodel = mOrderViewModel
        rvOrders = mRootView.findViewById(R.id.rv_orders)
        tvPastOrder = mRootView.findViewById(R.id.tv_order_past)
        tvCurrentOrder = mRootView.findViewById(R.id.tv_order_current)
        appCompatActivity = if (dashBoardNavigator?.getInstance() != null) dashBoardNavigator?.getInstance() else activity as AppCompatActivity
        linearLayoutManager=LinearLayoutManager(activity)
        rvOrders.layoutManager=linearLayoutManager
        rvOrders.adapter=orderAdpater
    }


    override fun getCurrentOrder() {
        if (isCurrentOrder == true) {
            tvCurrentOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_past_order)
            tvPastOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_current_order)
            isCurrentOrder = false
        } else {
            tvPastOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_current_order)
            tvCurrentOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_past_order)
            isCurrentOrder = true
        }

    }

    override fun getPastOrder() {
        if (isCurrentOrder == true) {
            tvCurrentOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_past_order)
            tvPastOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_current_order)
            isCurrentOrder = false
        } else {
            tvPastOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_current_order)
            tvCurrentOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_past_order)
            isCurrentOrder = true
        }
    }

    override fun setOrderType(view:View) {

        when (view.id){
            R.id.tv_order_past -> {
                if(isPastOrderSelected==true){

                }else{
                    isPastOrderSelected=true
                    isCurrentOrderSelected=false
                    tvPastOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_current_order)
                    tvCurrentOrder.background=ContextCompat.getDrawable(appCompatActivity!!,R.drawable.bg_past_order)
                }

            }

            R.id.tv_order_current -> {
                if(isCurrentOrderSelected==true){
                }else{
                    isCurrentOrderSelected=true
                    isPastOrderSelected=false
                    tvCurrentOrder.background = ContextCompat.getDrawable(appCompatActivity!!, R.drawable.bg_current_order)
                    tvPastOrder.background=ContextCompat.getDrawable(appCompatActivity!!,R.drawable.bg_past_order)
                }
            }
        }
    }
}