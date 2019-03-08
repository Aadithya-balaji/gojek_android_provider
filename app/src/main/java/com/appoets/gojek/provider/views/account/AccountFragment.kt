package com.appoets.gojek.provider.views.account

import android.content.Context
import android.view.View
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.appoets.basemodule.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.views.adapters.AccountAdapter
import com.appoets.gojek.provider.views.dashboard.DashBoardNavigator
import java.util.*


class AccountFragment : BaseFragment<com.appoets.gojek.provider.databinding.FragmentAccountBinding>() {
    private lateinit var mFragmentAccountBinding: com.appoets.gojek.provider.databinding.FragmentAccountBinding
    private var mAccountViewModel: AccountViewModel? = null
    private lateinit var rvAccount: RecyclerView
    private lateinit var gvAccount: GridView
    private var dashBoardNavigator: DashBoardNavigator? = null
    private var appCompatActivity: AppCompatActivity? = null
    private var accountAdapter: AccountAdapter? = null


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }


    override fun getLayoutId(): Int {
        return com.appoets.gojek.provider.R.layout.fragment_account
    }


    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mFragmentAccountBinding = mViewDataBinding as com.appoets.gojek.provider.databinding.FragmentAccountBinding
        mAccountViewModel = AccountViewModel()
        // rvAccount=mRootView.findViewById(R.id.rvc)
        gvAccount = mRootView.findViewById(R.id.gv_account) as GridView
        appCompatActivity = if (dashBoardNavigator?.getInstance() != null) dashBoardNavigator?.getInstance() else activity as AppCompatActivity

        //Get List Values from xml
        var accountTitle =resources.getStringArray(R.array.account)
        var accountTitleList= Arrays.asList(accountTitle)



        var accountIcons=resources.getIntArray(R.array.account_icons)
         var iconSize:Int=accountIcons.size

         accountAdapter= AccountAdapter(appCompatActivity!!,accountTitle,accountIcons)
        gvAccount.adapter=accountAdapter

        dashBoardNavigator!!.setTitle(appCompatActivity!!.resources.getString(R.string.header_label_myaccount))

    }
}