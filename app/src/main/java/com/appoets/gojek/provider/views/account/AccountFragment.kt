package com.appoets.gojek.provider.views.account

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.base.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentAccountBinding
import com.appoets.gojek.provider.views.TransactionStatusActivity.TransactionStatusActivity
import com.appoets.gojek.provider.views.dashboard.DashBoardNavigator
import com.appoets.xjek.ui.invitereferals.InviteReferalsActivity
import com.appoets.xjek.ui.payment.PaymentActivity
import com.appoets.xjek.ui.profile.Profile
import com.appoets.xjek.ui.support.SupportActivity

class AccountFragment : BaseFragment<FragmentAccountBinding>(), AccountNavigator {

    private lateinit var mFragmentAccountBinding: FragmentAccountBinding
    override fun getLayoutId(): Int = R.layout.fragment_account
    private lateinit var dashBoardNavigator: DashBoardNavigator


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }

    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mFragmentAccountBinding = mViewDataBinding as FragmentAccountBinding
        val mAccountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        mAccountViewModel.setNavigator(this)
        mFragmentAccountBinding.myaccountfragmentviewmodel = mAccountViewModel
        dashBoardNavigator.setTitle(resources.getString(R.string.myaccount))
        dashBoardNavigator.setRightIcon(R.drawable.more)
        dashBoardNavigator.showLogo(false)
    }

    override fun gotoProfilePage() {

        openNewActivity(activity, Profile::class.java, false)
    }

    override fun gotoInvitPage() {

        openNewActivity(activity, InviteReferalsActivity::class.java, false)

    }

    override fun gotoPaymentPage() {
        openNewActivity(activity, PaymentActivity::class.java, false)

    }

    override fun gotoTransacationPage() {

        openNewActivity(activity, TransactionStatusActivity::class.java, false)

    }

    override fun gotoPrivacyPage() {

    }

    override fun gotoSupportPage() {

        openNewActivity(activity, SupportActivity::class.java, false)
    }

}