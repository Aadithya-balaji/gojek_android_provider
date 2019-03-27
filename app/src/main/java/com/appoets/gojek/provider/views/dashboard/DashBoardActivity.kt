package com.appoets.gojek.provider.views.dashboard

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.views.account.AccountFragment
import com.appoets.gojek.provider.views.home.HomeFragment
import com.appoets.gojek.provider.views.notification.NotificationFragment
import com.appoets.gojek.provider.views.order.OrderFragment

class DashBoardActivity : BaseActivity<com.appoets.gojek.provider.databinding.ActivityDashboardBinding>(), DashBoardNavigator {


    lateinit var mDashboardBinding: com.appoets.gojek.provider.databinding.ActivityDashboardBinding
    private var mDashBoarViewModel: DashBoardViewModel? = null
    private lateinit var tvTitle: TextView
    private lateinit var ivArrow: ImageView
    private lateinit var tbrRlLeft: RelativeLayout
    private lateinit var tbrIvLogo: ImageView
    private lateinit var ivRightIcon: ImageView
    override fun getLayoutId(): Int = R.layout.activity_dashboard

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDashboardBinding = mViewDataBinding as com.appoets.gojek.provider.databinding.ActivityDashboardBinding
        mDashBoarViewModel = ViewModelProviders.of(this).get(DashBoardViewModel::class.java)
        mDashboardBinding.dashboardModel = mDashBoarViewModel
        tvTitle = findViewById(R.id.tv_header)
        ivArrow = findViewById(R.id.iv_back)
        tbrRlLeft = findViewById(R.id.tbr_rl_right)
        tbrIvLogo = findViewById(R.id.tbr_iv_logo)
        ivRightIcon = findViewById(R.id.iv_right)
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

    override fun setTitle(title: String) {
        tvTitle.setText(title)
    }

    override fun hideLeftArrow(isTrue: Boolean) {
        if (isTrue) {
            ivArrow.visibility = View.GONE
        } else {
            ivArrow.visibility = View.VISIBLE
        }
    }

    override fun setLeftTitle(strTitle: String) {
        tbrIvLogo.visibility = View.GONE
        tbrRlLeft.visibility = View.VISIBLE
        tvTitle.setText(strTitle)
    }

    override fun showLogo(isNeedShow: Boolean) {

        if (isNeedShow) {
            tbrIvLogo.visibility = View.VISIBLE
            tbrRlLeft.visibility = View.GONE
        } else {
            tbrRlLeft.visibility = View.VISIBLE
            tbrIvLogo.visibility = View.GONE
        }
    }

    override fun switchFragment(fragmentName: String, bundle: Bundle, isNeedAnimation: Boolean, isClearBackStack: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getInstance(): DashBoardActivity {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRightIcon(rightIcon: Int) {
        ivRightIcon.setImageResource(rightIcon)
    }


    override fun hideRightIcon(isNeedHide: Boolean) {
        if (isNeedHide) {
            ivRightIcon.visibility = View.GONE
        } else {
            ivRightIcon.visibility = View.VISIBLE
        }
    }

}


