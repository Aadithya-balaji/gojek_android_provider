package com.appoets.gojek.provider.views.TransactionStatusActivity

import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivtyTransactionStatusBinding
import com.appoets.gojek.provider.views.adapters.TransactionStatusListAdapter
import kotlinx.android.synthetic.main.toolbar_layout.view.*


class TransactionStatusActivity : BaseActivity<ActivtyTransactionStatusBinding>(), TransactionStatusNavigator {

    lateinit var mViewDataBinding: ActivtyTransactionStatusBinding

    override fun getLayoutId(): Int = R.layout.activty_transaction_status

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivtyTransactionStatusBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.transaction)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }
        mViewDataBinding. transactionStatusAdapter = TransactionStatusListAdapter(this)
    }

    override fun showStatus() {

    }

}
