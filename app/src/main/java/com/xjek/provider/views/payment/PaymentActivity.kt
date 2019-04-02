package com.xjek.provider.views.payment

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityPaymentBinding
import com.xjek.provider.views.adapters.TransactionListAdapter
import com.xjek.provider.views.transaction_status.TransactionStatusActivity
import com.xjek.xjek.ui.payment.PaymentNavigator
import com.xjek.xjek.ui.payment.PaymentViewModel
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
        paymentViewModel.navigator = this
        mViewDataBinding.paymentViewModel = paymentViewModel
        mViewDataBinding.transactionAdapter = TransactionListAdapter(this)


    }

    override fun addWalletAmount() {

    }

    override fun goToTransactionStatusActivty() {

        openNewActivity(this, TransactionStatusActivity::class.java, true)
    }
}