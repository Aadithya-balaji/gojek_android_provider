package com.xjek.provider.views.manage_payment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityManagePaymentBinding
import com.xjek.provider.views.adapters.PaymentModeAdapter
import com.xjek.provider.views.transaction.TranascationFragment
import com.xjek.provider.views.wallet.WalletFragment
import java.util.*

class  ManagePaymentActivity:BaseActivity<ActivityManagePaymentBinding>(),ManagePaymentNavigator{
    private  lateinit var activityManagePaymentBinding:ActivityManagePaymentBinding
    private  lateinit var  managePaymentViewModel:ManagePaymentViewModel

    override fun getLayoutId(): Int {
        return  R.layout.activity_manage_payment
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityManagePaymentBinding=mViewDataBinding as ActivityManagePaymentBinding
        managePaymentViewModel= ManagePaymentViewModel()
        managePaymentViewModel.navigator=this

        val paymentFragmentList=Vector<Fragment>()

        val walletFragment= WalletFragment()
        val  transactionFragment=TranascationFragment()

        paymentFragmentList.add(walletFragment)
        paymentFragmentList.add(transactionFragment)

        var paymentTypes=resources.getStringArray(R.array.payment_mode).toMutableList()
        val flexboxLayoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
        }

        val recyclerView: RecyclerView = findViewById(R.id.rv_payment)
        recyclerView.apply {
            layoutManager = flexboxLayoutManager
            adapter =PaymentModeAdapter(this@ManagePaymentActivity,paymentTypes)
        }

    }

}