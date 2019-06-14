package com.gox.partner.views.transaction_status

import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivtyTransactionStatusBinding
import com.gox.partner.views.adapters.TransactionStatusListAdapter
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class TransactionStatusActivity : BaseActivity<ActivtyTransactionStatusBinding>(), TransactionStatusNavigator {

    lateinit var mBinding: ActivtyTransactionStatusBinding

    override fun getLayoutId() = R.layout.activty_transaction_status

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mBinding = mViewDataBinding as ActivtyTransactionStatusBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.transaction)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }
        mViewDataBinding.transactionStatusAdapter = TransactionStatusListAdapter(this)
    }

}
