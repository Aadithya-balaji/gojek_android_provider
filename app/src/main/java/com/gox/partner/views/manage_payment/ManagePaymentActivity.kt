package com.gox.partner.views.manage_payment

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityManagePaymentBinding
import com.gox.partner.views.adapters.PaymentAdapter
import com.gox.partner.views.transaction.TranascationFragment
import com.gox.partner.views.wallet.WalletFragment
import java.util.*

class ManagePaymentActivity : BaseActivity<ActivityManagePaymentBinding>(), ManagePaymentNavigator, ViewPager.OnPageChangeListener {

    private lateinit var activityManagePaymentBinding: ActivityManagePaymentBinding
    private lateinit var managePaymentViewModel: ManagePaymentViewModel
    private lateinit var paymentAdapter: PaymentAdapter
    private lateinit var tbManagePayment: TabLayout

    override fun getLayoutId() = R.layout.activity_manage_payment

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityManagePaymentBinding = mViewDataBinding as ActivityManagePaymentBinding
        managePaymentViewModel = ManagePaymentViewModel()
        managePaymentViewModel.navigator = this
        tbManagePayment = findViewById(R.id.tb_payment)
        activityManagePaymentBinding.toolbarLayout.tvToolbarTitle.setText(resources.getString(com.gox.partner.R.string.header_label_payment))
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