package com.appoets.gojek.provider.views.notification

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.base.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentNotificationBinding
import com.appoets.gojek.provider.views.adapters.NotificationAdapter
import com.appoets.gojek.provider.views.dashboard.DashBoardNavigator

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    private lateinit var mFragmentNotificationBinding: FragmentNotificationBinding
    private var mNotificationViewModel: NotificationViewModel? = null
    private lateinit var dashBoardNavigator: DashBoardNavigator


    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
        dashBoardNavigator.setTitle(resources.getString(R.string.header_label_notification))
        dashBoardNavigator.showLogo(false)
    }


    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mFragmentNotificationBinding = mViewDataBinding as FragmentNotificationBinding
        mNotificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        mFragmentNotificationBinding.notificationmodel = mNotificationViewModel
        mFragmentNotificationBinding.notificationAdapter = NotificationAdapter()


    }

}