package com.appoets.gojek.provider.views.notification

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.basemodule.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentNotificationBinding
import com.appoets.gojek.provider.views.adapters.NotificationAdapter

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    private lateinit var mFragmentNotificationBinding: FragmentNotificationBinding
    private var mNotificationViewModel: NotificationViewModel? = null


    override fun getLayoutId(): Int = R.layout.fragment_notification


    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mFragmentNotificationBinding = mViewDataBinding as FragmentNotificationBinding
        mNotificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        mFragmentNotificationBinding.notificationmodel = mNotificationViewModel
        mFragmentNotificationBinding.notificationAdapter = NotificationAdapter()


    }

}