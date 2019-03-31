package com.xjek.provider.views.account

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentAccountBinding
import com.xjek.provider.views.TransactionStatusActivity.TransactionStatusActivity
import com.xjek.provider.views.dashboard.DashBoardNavigator
import com.xjek.provider.views.invitereferals.InviteReferalsActivity
import com.xjek.provider.views.payment.PaymentActivity
import com.xjek.provider.views.profile.Profile
import com.xjek.provider.views.support.SupportActivity

class AccountFragment : BaseFragment<FragmentAccountBinding>(), AccountNavigator {

    private lateinit var mFragmentAccountBinding: FragmentAccountBinding
    override fun getLayoutId(): Int = R.layout.fragment_account
    private lateinit var dashBoardNavigator: DashBoardNavigator


    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }

    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mFragmentAccountBinding = mViewDataBinding as FragmentAccountBinding
        val mAccountViewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
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