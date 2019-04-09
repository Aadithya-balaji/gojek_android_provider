package com.xjek.provider.views.account

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseFragment
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentAccountBinding
import com.xjek.provider.models.AccountMenuModel
import com.xjek.provider.views.dashboard.DashBoardNavigator
import com.xjek.provider.views.manage_documents.ManageDocumentsActivity
import com.xjek.provider.views.manage_services.ManageServicesActivity

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

    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        viewModel = provideViewModel { AccountViewModel() }
        viewModel.navigator = this
        binding = mViewDataBinding as FragmentAccountBinding
        binding.lifecycleOwner = this
        binding.accountViewModel = viewModel

        dashBoardNavigator.setTitle(resources.getString(R.string.title_my_account))
        dashBoardNavigator.setRightIcon(R.drawable.more)
        dashBoardNavigator.showLogo(false)

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
        val intent: Intent
        when (position) {

            0 -> {
               // launchNewActivity(ProfileAc::class.java, false)
            }

            5 -> {
                launchNewActivity(ManageServicesActivity::class.java, false)
            }
            6 -> {
                launchNewActivity(ManageDocumentsActivity::class.java, false)
            }

            7->{
                ViewUtils.showToast(activity!!,"Language Clicked",true)
            }
        }
    }
}