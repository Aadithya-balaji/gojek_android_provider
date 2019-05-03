package com.xjek.provider.views.dashboard

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.Constants.ProjectTypes.ORDER
import com.xjek.base.data.Constants.ProjectTypes.SERVICE
import com.xjek.base.data.Constants.ProjectTypes.TRANSPORT
import com.xjek.base.data.Constants.RequestCode.PERMISSIONS_CODE_LOCATION
import com.xjek.base.data.Constants.RequestPermission.PERMISSIONS_LOCATION
import com.xjek.base.data.Constants.RideStatus.SEARCHING
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.location_service.BaseLocationService
import com.xjek.base.location_service.BaseLocationService.BROADCAST
import com.xjek.base.utils.LocationCallBack
import com.xjek.base.utils.LocationUtils
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityDashboardBinding
import com.xjek.provider.views.account.AccountFragment
import com.xjek.provider.views.home.HomeFragment
import com.xjek.provider.views.incoming_request_taxi.IncomingRequestDialog
import com.xjek.provider.views.notification.NotificationFragment
import com.xjek.provider.views.order.OrderFragment
import com.xjek.taxiservice.views.main.TaxiDashboardActivity
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.toolbar_header.view.*

class DashBoardActivity : BaseActivity<ActivityDashboardBinding>(), DashBoardNavigator {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var mViewModel: DashBoardViewModel

    private var mIncomingRequestDialog = IncomingRequestDialog()
    private var locationServiceIntent: Intent? = null
    private var currentLat: Double = 13.0561789
    private var currentLong: Double = 80.247998

    override fun getLayoutId(): Int = R.layout.activity_dashboard

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityDashboardBinding
        mViewModel = ViewModelProviders.of(this).get(DashBoardViewModel::class.java)
        mViewModel.navigator = this
        binding.dashboardModel = mViewModel
        setSupportActionBar(binding.tbrHome.app_bar)
        mViewModel.latitude.value = currentLat
        mViewModel.longitude.value = currentLong
        supportFragmentManager.beginTransaction().add(R.id.frame_home_container, HomeFragment()).commit()
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_home_container, HomeFragment()).commit()
                    true
                }

                R.id.action_history -> {
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

        locationServiceIntent = Intent(this, BaseLocationService::class.java)
        if (getPermissionUtil().hasPermission(this, PERMISSIONS_LOCATION)) {
            updateLocation(true)
            updateCurrentLocation()
        } else if (getPermissionUtil().requestPermissions(this, PERMISSIONS_LOCATION, PERMISSIONS_CODE_LOCATION)) {
            updateLocation(true)
            updateCurrentLocation()
        }

        getApiResponse()

    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    override fun setTitle(title: String) {
        tv_header.text = title
    }

    override fun hideLeftArrow(isTrue: Boolean) {
        if (isTrue) iv_back.visibility = View.GONE else iv_back.visibility = View.VISIBLE
    }

    override fun setLeftTitle(strTitle: String) {
        tbr_iv_logo.visibility = View.GONE
        tbr_rl_right.visibility = View.VISIBLE
        tv_header.text = strTitle
    }

    override fun showLogo(isNeedShow: Boolean) {
        if (isNeedShow) {
            tbr_iv_logo.visibility = View.VISIBLE
            tbr_rl_right.visibility = View.GONE
        } else {
            tbr_rl_right.visibility = View.VISIBLE
            tbr_iv_logo.visibility = View.GONE
        }
    }

    override fun setRightIcon(rightIcon: Int) {
        iv_right.setImageResource(rightIcon)
    }

    override fun hideRightIcon(isNeedHide: Boolean) {
        if (isNeedHide) iv_right.visibility = View.GONE else iv_right.visibility = View.VISIBLE
    }

    override fun updateLocation(isTrue: Boolean) {
        if (isTrue) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BROADCAST))
            startService(locationServiceIntent)
        } else {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
            stopService(locationServiceIntent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {
        try {
            LocationUtils.getLastKnownLocation(this, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    currentLat = location!!.latitude
                    currentLong = location.longitude
                    mViewModel.getRequest()
                }

                override fun onFailure(messsage: String?) {
                    currentLat = 13.0561789
                    currentLong = 80.247998
                    mViewModel.getRequest()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getApiResponse() {
        println("RRR :: HomeFragment.getApiResponse")
        observeLiveData(mViewModel.checkRequestLiveData) { checkStatusData ->
            if (checkStatusData.statusCode == "200") {
                if (checkStatusData.responseData.requests.isNotEmpty())
                    Log.e("CheckStatus", "-----" + checkStatusData.responseData.requests[0].status)
                when (checkStatusData.responseData.requests[0].request.status) {
                    SEARCHING -> {
                        if (!mIncomingRequestDialog.isShown()) {
                            val bundle = Bundle()
                            val strRequest = Gson().toJson(checkStatusData)
                            bundle.putString("requestModel", strRequest)
                            mIncomingRequestDialog.arguments = bundle
                            mIncomingRequestDialog.show(supportFragmentManager, "mIncomingRequestDialog")
                        }
                    }

                    else -> when (checkStatusData.responseData.requests[0].service.admin_service_name) {
                        TRANSPORT -> {
                            BROADCAST = TRANSPORT
                            startActivity(Intent(this, TaxiDashboardActivity::class.java))
                        }
                        SERVICE -> {
                            if (!BROADCAST.equals(SERVICE)) {
                                BROADCAST = SERVICE
                                startActivity(Intent(this, XuberMainActivity::class.java))
                            }
                        }
                        ORDER -> {
                            if (BROADCAST != ORDER) {
                                BROADCAST = ORDER
                                startActivity(Intent(this, TaxiDashboardActivity::class.java))
                            }
                        }

                        else -> BROADCAST = "BASE_BROADCAST"
                    }
                }
            }
        }
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRRR:: DashboardActivity")
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            if (location != null) {
                currentLat = location.latitude
                currentLong = location.longitude

                mViewModel.latitude.value = currentLat
                mViewModel.longitude.value = currentLong

                mViewModel.getRequest()
            }
        }
    }

    override fun showErrorMessage(s: String) {
        ViewUtils.showNormalToast(this, s)
    }

    override fun getInstance(): DashBoardActivity = this@DashBoardActivity
}