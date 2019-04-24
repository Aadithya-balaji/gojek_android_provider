package com.xjek.provider.views.account

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseFragment
import com.xjek.base.extensions.clearPreferences
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentAccountBinding
import com.xjek.provider.models.AccountMenuModel
import com.xjek.provider.views.dashboard.DashBoardNavigator
import com.xjek.provider.views.invitereferals.InviteReferalsActivity
import com.xjek.provider.views.language.LanguageActivity
import com.xjek.provider.views.manage_bank_details.ManageBankDetailsActivity
import com.xjek.provider.views.manage_documents.ManageDocumentsActivity
import com.xjek.provider.views.manage_payment.ManagePaymentActivity
import com.xjek.provider.views.manage_services.ManageServicesActivity
import com.xjek.provider.views.on_board.OnBoardActivity
import com.xjek.provider.views.privacypolicy.PrivacyActivity
import com.xjek.provider.views.profile.ProfileActivity
import com.xjek.provider.views.support.SupportActivity
import kotlinx.android.synthetic.main.header_layout.*

class AccountFragment : BaseFragment<FragmentAccountBinding>(), AccountNavigator {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel
    private lateinit var dashBoardNavigator: DashBoardNavigator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_account
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        viewModel = provideViewModel { AccountViewModel() }
        viewModel.navigator = this
        binding = mViewDataBinding as FragmentAccountBinding
        binding.lifecycleOwner = this
        binding.accountViewModel = viewModel

        dashBoardNavigator.setTitle(resources.getString(R.string.title_my_account))
        dashBoardNavigator.setRightIcon(R.drawable.ic_logout)
        dashBoardNavigator.hideRightIcon(false)
        dashBoardNavigator.showLogo(false)

        dashBoardNavigator.getInstance().iv_right.setOnClickListener {
            ViewUtils.showAlert(activity!!, getString(R.string.xjek_logout_alert), object : ViewUtils.ViewCallBack {
                override fun onPositiveButtonClick(dialog: DialogInterface) {
                    clearPreferences<String>()
                    launchNewActivity(OnBoardActivity::class.java, false)
                    activity!!.finishAffinity()
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
        viewModel.setAccountMenus(accountMenus)
        viewModel.setAdapter()
    }

    override fun onMenuItemClicked(position: Int) {
        when (resources.getStringArray(R.array.title_account)[position]) {
            getString(R.string.title_profile) -> {
                launchNewActivity(ProfileActivity::class.java, false)
            }

            getString(R.string.title_manage_documents) -> {
                launchNewActivity(ManageDocumentsActivity::class.java, false)
            }

            getString(R.string.title_manage_services) -> {
                launchNewActivity(ManageServicesActivity::class.java, false)
            }

            getString(R.string.title_payment) -> {
                launchNewActivity(ManagePaymentActivity::class.java,false)
            }

            getString(R.string.title_earnings) ->{
                //Earnings
            }

            getString(R.string.title_bank_details) -> {
                //Bank Details
                launchNewActivity(ManageBankDetailsActivity::class.java,false)
            }

            getString(R.string.title_invite_referrals) -> {
                launchNewActivity(InviteReferalsActivity::class.java, false)
            }

            getString(R.string.title_language) ->{
                launchNewActivity(LanguageActivity::class.java, false)
            }

            getString(R.string.title_privacy_policy) ->{
                launchNewActivity(PrivacyActivity::class.java, false)
            }

            getString(R.string.title_support) ->{
                launchNewActivity(SupportActivity::class.java, false)
            }
        }
    }
}