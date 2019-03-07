package com.appoets.gojek.provider.views.notification

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.appoets.basemodule.base.BaseFragment
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.FragmentNotificationBinding
import com.appoets.gojek.provider.views.adapters.NotificationAdapter
import com.appoets.gojek.provider.views.dashboard.DashBoardNavigator

class NotificationFragment:BaseFragment<FragmentNotificationBinding>(){

    private  lateinit var  mFragmentNotificationBinding: FragmentNotificationBinding
    private  var mNotificationViewModel:NotificationViewModel?=null
    private  var notificationAdapter:NotificationAdapter?=null
    private  var linearLayoutManager:LinearLayoutManager?=null
    private  lateinit var  rvNotification:RecyclerView
    private var dashBoardNavigator: DashBoardNavigator? = null
    private var appCompatActivity: AppCompatActivity? = null




    override fun onAttach(context: Context?) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_notification
    }

    override fun initView(mRootView: View, mViewDataBinding: ViewDataBinding?) {
        mFragmentNotificationBinding=mViewDataBinding as FragmentNotificationBinding
        rvNotification=mRootView.findViewById(R.id.rv_notification)
        mNotificationViewModel = NotificationViewModel()
        mFragmentNotificationBinding.notificationmodel=mNotificationViewModel
        appCompatActivity = if (dashBoardNavigator?.getInstance() != null) dashBoardNavigator?.getInstance() else activity as AppCompatActivity
        notificationAdapter= NotificationAdapter()
        linearLayoutManager= LinearLayoutManager(activity)
        rvNotification.layoutManager=linearLayoutManager
        rvNotification.adapter=notificationAdapter
        dashBoardNavigator!!.setTitle(appCompatActivity!!.resources.getString(R.string.header_label_notification))


    }

}