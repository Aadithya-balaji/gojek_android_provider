package com.appoets.gojek.provider.views.dashboard

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.views.account.AccountFragment
import com.appoets.gojek.provider.views.home.HomeFragment
import com.appoets.gojek.provider.views.notification.NotificationFragment
import com.appoets.gojek.provider.views.order.OrderFragment

class DashBoardActivity : BaseActivity<com.appoets.gojek.provider.databinding.ActivityDashboardBinding>() {

    lateinit var mDashboardBinding: com.appoets.gojek.provider.databinding.ActivityDashboardBinding
    private var mDashBoarViewModel: DashBoardViewModel? = null

    override fun getLayoutId(): Int = R.layout.activity_dashboard

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDashboardBinding = mViewDataBinding as com.appoets.gojek.provider.databinding.ActivityDashboardBinding
        mDashBoarViewModel = ViewModelProviders.of(this).get(DashBoardViewModel::class.java)
        mDashboardBinding.dashboardModel = mDashBoarViewModel

        supportFragmentManager.beginTransaction().add(R.id.frame_home_container, HomeFragment()).commit()


        mDashboardBinding.bottomNavigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.action_home -> {

                    supportFragmentManager.beginTransaction().replace(R.id.frame_home_container, HomeFragment()).commit()
                    true
                }
                R.id.action_order -> {

                    supportFragmentManager.beginTransaction().replace(R.id.frame_home_container, OrderFragment()).commit()
                    true
                }
                R.id.action_notification -> {

                    supportFragmentManager.beginTransaction().replace(R.id.frame_home_container, NotificationFragment()).commit()
                    true
                }

                R.id.action_account -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_home_container, AccountFragment()).commit()
                    true

                }
                else -> false
            }
        }

    }
}


