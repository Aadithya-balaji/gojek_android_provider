package com.gox.partner.views.account

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseFragment
import com.gox.base.extensions.clearPreferences
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.FragmentAccountBinding
import com.gox.partner.models.AccountMenuModel
import com.gox.partner.views.account_card.PaymentTypesActivity
import com.gox.partner.views.dashboard.DashBoardNavigator
import com.gox.partner.views.earnings.EarningsActivity
import com.gox.partner.views.invitereferals.InviteReferralsActivity
import com.gox.partner.views.language.LanguageActivity
import com.gox.partner.views.manage_bank_details.ManageBankDetailsActivity
import com.gox.partner.views.manage_documents.ManageDocumentsActivity
import com.gox.partner.views.manage_payment.ManagePaymentActivity
import com.gox.partner.views.manage_services.ManageServicesActivity
import com.gox.partner.views.on_board.OnBoardActivity
import com.gox.partner.views.privacypolicy.PrivacyActivity
import com.gox.partner.views.profile.ProfileActivity
import com.gox.partner.views.support.SupportActivity
import kotlinx.android.synthetic.main.header_layout.*

class AccountFragment : BaseFragment<FragmentAccountBinding>(), AccountNavigator {

    private lateinit var mBinding: FragmentAccountBinding
    private lateinit var mViewModel: AccountViewModel
    private lateinit var dashBoardNavigator: DashBoardNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }

    override fun getLayoutId() = R.layout.fragment_account

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mViewModel = provideViewModel { AccountViewModel() }
        mViewModel.navigator = this
        mBinding = mViewDataBinding as FragmentAccountBinding
        mBinding.lifecycleOwner = this
        mBinding.accountViewModel = mViewModel

        dashBoardNavigator.setTitle(resources.getString(R.string.title_my_account))
        dashBoardNavigator.setRightIcon(R.drawable.ic_logout)
        dashBoardNavigator.hideRightIcon(false)
        dashBoardNavigator.showLogo(false)

        observeLiveData(mViewModel.successResponse) {
            dashBoardNavigator.updateLocation(false)
            clearPreferences<String>()
            launchNewActivity(OnBoardActivity::class.java, false)
            activity!!.finishAffinity()
        }

        dashBoardNavigator.getInstance().iv_right.setOnClickListener {
            ViewUtils.showAlert(activity!!, getString(R.string.xjek_logout_alert), object : ViewUtils.ViewCallBack {
                override fun onPositiveButtonClick(dialog: DialogInterface) {
                    mViewModel.logoutApp()
                    dialog.dismiss()
                }

                override fun onNegativeButtonClick(dialog: DialogInterface) {
                    dialog.dismiss()
                }
            })
        }

        val accountMenuTitles = resources.getStringArray(R.array.title_account)
        val accountMenuIcons = resources.obtainTypedArray(R.array.icon_account)
        val accountMenus = List(accountMenuTitles.size) {
            AccountMenuModel(accountMenuTitles[it], accountMenuIcons.getResourceId(it, -1))
        }

        accountMenuIcons.recycle()
        mViewModel.setAccountMenus(accountMenus)
        mViewModel.setAdapter()
    }

    override fun onMenuItemClicked(position: Int) = when (position) {

        0 -> launchNewActivity(ProfileActivity::class.java, false)

        1 -> launchNewActivity(ManageServicesActivity::class.java, false)

        2 -> launchNewActivity(ManageDocumentsActivity::class.java, false)

        3 -> launchNewActivity(ManageBankDetailsActivity::class.java, false)

        4 -> launchNewActivity(PaymentTypesActivity::class.java, false)

        5 -> launchNewActivity(ManagePaymentActivity::class.java, false)

        6 -> launchNewActivity(EarningsActivity::class.java, false)

        7 -> launchNewActivity(InviteReferralsActivity::class.java, false)

        8 -> launchNewActivity(PrivacyActivity::class.java, false)

        9 -> launchNewActivity(SupportActivity::class.java, false)

        10 -> launchNewActivity(LanguageActivity::class.java, false)

        else -> {
            throw IllegalStateException()
        }
    }
}