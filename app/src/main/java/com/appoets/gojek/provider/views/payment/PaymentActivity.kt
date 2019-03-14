package com.appoets.xjek.ui.payment

import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityPaymentBinding
import com.appoets.gojek.provider.views.adapters.TransactionListAdapter
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class PaymentActivity : BaseActivity<ActivityPaymentBinding>(), PaymentNavigator {

    lateinit var mViewDataBinding: ActivityPaymentBinding

    override fun getLayoutId(): Int = R.layout.activity_payment

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityPaymentBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.payment)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }
        mViewDataBinding.transactionAdapter = TransactionListAdapter(this)


    }

    override fun addWalletAmount() {

    }
}