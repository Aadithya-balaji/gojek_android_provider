package com.gox.partner.views.manage_payment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityManagePaymentBinding
import com.gox.partner.views.adapters.PaymentAdapter
import com.gox.partner.views.transaction.TransactionFragment
import com.gox.partner.views.wallet.WalletFragment
import java.util.*

class ManagePaymentActivity : BaseActivity<ActivityManagePaymentBinding>(), ManagePaymentNavigator {

    private lateinit var mBinding: ActivityManagePaymentBinding
    private lateinit var mViewModel: ManagePaymentViewModel
    private lateinit var paymentAdapter: PaymentAdapter
    private lateinit var tbManagePayment: TabLayout

    override fun getLayoutId() = R.layout.activity_manage_payment

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityManagePaymentBinding
        mViewModel = ManagePaymentViewModel()
        mViewModel.navigator = this
        tbManagePayment = findViewById(R.id.tb_payment)
        mBinding.toolbarLayout.tvToolbarTitle.text = resources.getString(com.gox.partner.R.string.header_label_payment)
        mBinding.toolbarLayout.ivToolbarBack.setOnClickListener {
            finish()
        }

        val paymentFragmentList = Vector<Fragment>()

        val walletFragment = WalletFragment()
        val transactionFragment = TransactionFragment()

        paymentFragmentList.add(walletFragment)
        paymentFragmentList.add(transactionFragment)

        paymentAdapter = PaymentAdapter(supportFragmentManager, this@ManagePaymentActivity, paymentFragmentList)
        mBinding.vbPayment.adapter = paymentAdapter
        tbManagePayment.setupWithViewPager(mBinding.vbPayment)

    }

}