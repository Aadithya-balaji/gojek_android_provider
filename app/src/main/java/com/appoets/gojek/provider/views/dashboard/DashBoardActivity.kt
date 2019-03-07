package com.appoets.gojek.provider.views.dashboard

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.utils.Constant
import com.appoets.gojek.provider.views.account.AccountFragment
import com.appoets.gojek.provider.views.home.HomeFragment
import com.appoets.gojek.provider.views.notification.NotificationFragment
import com.appoets.gojek.provider.views.order.OrderFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class DashBoardActivity : BaseActivity<com.appoets.gojek.provider.databinding.ActivityDashboardBinding>(), BottomNavigationView.OnNavigationItemSelectedListener, DashBoardNavigator {



    /* override fun onNavigationItemReselected(p0: MenuItem) {
         if (currentFragment.equals(item.getTitle().toString())) return true;
         currentFragment = item.getTitle().toString();
         switchFragment(item.getTitle().toString(), Bundle(), false, true);
         return true
     }
 */

    lateinit var mDashboardBinding: com.appoets.gojek.provider.databinding.ActivityDashboardBinding
    private var mDashBoarViewModel: DashBoardViewModel? = null
    private var currentFragment: String? = null
    private var initialFragment: String = "Home"
    private var fragment: Fragment? = null
    private lateinit var mBottomNavigationView: BottomNavigationView
    private lateinit var tvTitle: TextView
    private  lateinit var  ivArrow: ImageView

    override fun getLayoutId(): Int {
        return R.layout.activity_dashboard
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBottomNavigationView = findViewById(R.id.bottom_navigation)
        mDashboardBinding = mViewDataBinding as com.appoets.gojek.provider.databinding.ActivityDashboardBinding
        mDashBoarViewModel = DashBoardViewModel()
        mDashboardBinding.dashboardModel = mDashBoarViewModel
        tvTitle = findViewById(R.id.tv_header)
        ivArrow = findViewById(R.id.iv_back)
        loadInitialFragment()

        //initListener
        initListener()

    }


    fun initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this)
    }

    //BottomNavigation
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (currentFragment.equals(item.getTitle().toString())) return true;
        currentFragment = item.getTitle().toString();
        switchFragment(item.getTitle().toString(), Bundle(), false, true);
        return true
    }


    // Change Fragment
    override fun switchFragment(fragmentName: String, bundle: Bundle, isNeedAnimation: Boolean, isClearBackStack: Boolean) {
        fragment = null
        when (fragmentName) {

            Constant.TAB_HOME -> {
                fragment = HomeFragment()
            }

            Constant.TAB_ORDER -> {
                fragment = OrderFragment()
            }

            Constant.TAB_NOTIFICATION -> {
                fragment = NotificationFragment()
            }

            Constant.TAB_ACCOUNT -> {
                fragment = AccountFragment()
            }

        }

        if (fragment != null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (isNeedAnimation)
                fragmentTransaction.setCustomAnimations(R.anim.in_right, R.anim.in_left, R.anim.out_left, R.anim.out_right)
            fragment?.let {
                fragmentTransaction.replace(R.id.frame_home_container, it, fragmentName)
                fragmentTransaction.addToBackStack(fragmentName)
                fragmentTransaction.commitAllowingStateLoss()
            }

        }

    }

    override fun getInstance(): DashBoardActivity {
        return this@DashBoardActivity
    }

    override fun setTitle(title: String) {
        tvTitle.setText(title)
    }

    fun loadInitialFragment() {
        switchFragment(initialFragment!!, Bundle(), false, true)
        currentFragment = initialFragment
    }

    override fun hideLeftArrow(isTrue: Boolean) {
        if(isTrue){
            ivArrow.visibility= View.GONE
        }else{
            ivArrow.visibility=View.VISIBLE
        }
    }
}