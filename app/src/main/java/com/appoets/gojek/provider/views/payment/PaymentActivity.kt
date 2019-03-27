package com.appoets.xjek.ui.payment

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityPaymentBinding
import com.appoets.gojek.provider.views.TransactionStatusActivity.TransactionStatusActivity
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
        val paymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel::class.java)
        mViewDataBinding.paymentViewModel = paymentViewModel
        paymentViewModel.setNavigator(this)
        mViewDataBinding.transactionAdapter = TransactionListAdapter(this)


    }

    override fun addWalletAmount() {

    }

    override fun goToTransactionStatusActivty() {

        openNewActivity(this, TransactionStatusActivity::class.java, true)
    }
}