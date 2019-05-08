package com.xjek.provider.views.manage_payment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.views.adapters.PaymentAdapter
import com.xjek.provider.views.transaction.TranascationFragment
import com.xjek.provider.views.wallet.WalletFragment
import java.util.*

class ManagePaymentActivity : BaseActivity<ActivityManagePaymentBinding>(), ManagePaymentNavigator, ViewPager.OnPageChangeListener {


    private lateinit var activityManagePaymentBinding: ActivityManagePaymentBinding
    private lateinit var managePaymentViewModel: ManagePaymentViewModel
    private lateinit var paymentAdapter: PaymentAdapter
    private lateinit var tbManagePayment: TabLayout

    override fun getLayoutId(): Int {
        return R.layout.activity_manage_payment
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityManagePaymentBinding = mViewDataBinding as ActivityManagePaymentBinding
        managePaymentViewModel = ManagePaymentViewModel()
        managePaymentViewModel.navigator = this
        tbManagePayment = findViewById(R.id.tb_payment)
        activityManagePaymentBinding.toolbarLayout.tvToolbarTitle.setText(resources.getString(com.xjek.provider.R.string.header_label_payment))
        activityManagePaymentBinding.toolbarLayout.ivToolbarBack.setOnClickListener {
            finish()
        }
        val paymentFragmentList = Vector<Fragment>()

        val walletFragment = WalletFragment()
        val transactionFragment = TranascationFragment()

        paymentFragmentList.add(walletFragment)
        paymentFragmentList.add(transactionFragment)

        paymentAdapter = PaymentAdapter(supportFragmentManager, this@ManagePaymentActivity, paymentFragmentList)
        activityManagePaymentBinding.vbPayment.adapter = paymentAdapter
        tbManagePayment.setupWithViewPager(activityManagePaymentBinding.vbPayment)

    }

    override fun onPageScrollStateChanged(state: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPageSelected(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addCard() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}