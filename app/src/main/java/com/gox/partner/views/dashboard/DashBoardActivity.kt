package com.gox.partner.views.dashboard

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.data.Constants.ModuleTypes.ORDER
import com.gox.base.data.Constants.ModuleTypes.SERVICE
import com.gox.base.data.Constants.ModuleTypes.TRANSPORT
import com.gox.base.data.Constants.RequestPermission.PERMISSIONS_LOCATION
import com.gox.base.data.Constants.RideStatus.SEARCHING
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.writePreferences
import com.gox.base.location_service.BaseLocationService
import com.gox.base.location_service.BaseLocationService.Companion.BROADCAST
import com.gox.base.socket.SocketListener
import com.gox.base.socket.SocketManager
import com.gox.base.utils.CommonMethods
import com.gox.base.utils.LocationCallBack
import com.gox.base.utils.LocationUtils
import com.gox.base.utils.ViewUtils
import com.gox.foodservice.ui.dashboard.FoodieDashboardActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityDashboardBinding
import com.gox.partner.views.account.AccountFragment
import com.gox.partner.views.home.HomeFragment
import com.gox.partner.views.incoming_request_taxi.IncomingRequestDialog
import com.gox.partner.views.notification.NotificationFragment
import com.gox.partner.views.order.OrderFragment
import com.gox.taxiservice.views.main.TaxiDashboardActivity
import com.gox.xuberservice.xuberMainActivity.XuberDashBoardActivity
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.header_layout.*
import kotlinx.android.synthetic.main.toolbar_header.view.*
import java.util.*
import kotlin.concurrent.schedule

class DashBoardActivity : BaseActivity<ActivityDashboardBinding>(), DashBoardNavigator,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var mViewModel: DashBoardViewModel
    private var mIncomingRequestDialog = IncomingRequestDialog()
    private var locationServiceIntent: Intent? = null
    private var checkStatusApiCounter = 0
    private var mHomeFragment = HomeFragment()
    private var locationRequest: LocationRequest? = null
    private var isGPSEnabled: Boolean = false
    private var isLocationDialogShown: Boolean = false
    private lateinit var context: Context
    private var googleApiClient: GoogleApiClient? = null
    private var dialog: Dialog? = null

    override fun getLayoutId(): Int = R.layout.activity_dashboard

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        context = this
        setUpGClient()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        binding = mViewDataBinding as ActivityDashboardBinding
        mViewModel = ViewModelProviders.of(this).get(DashBoardViewModel::class.java)
        mViewModel.navigator = this
        binding.dashboardModel = mViewModel
        setSupportActionBar(binding.tbrHome.app_bar)
        mViewModel.latitude.value = 0.0
        mViewModel.longitude.value = 0.0
        supportFragmentManager.beginTransaction().add(R.id.frame_home_container, mHomeFragment).commit()
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().replace(R.id.frame_home_container, mHomeFragment).commit()
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
        if (getPermissionUtil().hasAllPermisson(PERMISSIONS_LOCATION, context, 150)) {
            updateLocation(true)
        }
        mViewModel.getProfile()
        getApiResponse()

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val permission = getPermissionUtil().onRequestPermissionsResult(permissions, grantResults)
        if (permission != null && permission.isNotEmpty()) run {
            getPermissionUtil().isFirstTimePermission = true
            getPermissionUtil().hasAllPermisson(permission, context, 150)
        } else updateLocation(true)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.callCheckStatusAPI()
        writePreferences(PreferencesKey.CAN_SAVE_LOCATION, false)
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BROADCAST))
//        AppDatabase.getAppDataBase(this)!!.locationPointsDao().deleteAllPoint()
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
        try {
            if (isNeedShow) {
                tbr_iv_logo.visibility = View.VISIBLE
                tbr_rl_right.visibility = View.GONE
            } else {
                tbr_rl_right.visibility = View.VISIBLE
                tbr_iv_logo.visibility = View.GONE
            }
            tbr_home.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun setRightIcon(rightIcon: Int) {
        iv_right.setImageResource(rightIcon)
    }

    override fun hideRightIcon(isNeedHide: Boolean) {
        try {
            if (isNeedHide && iv_right != null) iv_right.visibility = View.GONE else iv_right.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun updateLocation(isTrue: Boolean) {
        println("RRRR :: DashBoardActivity.updateLocation")
        if (isTrue) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BROADCAST))
            startService(locationServiceIntent)
        } else {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
            stopService(locationServiceIntent)
        }
    }

    @Synchronized
    private fun setUpGClient() {
        googleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()
        googleApiClient!!.connect()
    }

    @SuppressLint("MissingPermission")
    override fun updateCurrentLocation() {
        try {
            LocationUtils.getLastKnownLocation(context, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    mViewModel.latitude.value = location!!.latitude
                    mViewModel.longitude.value = location.longitude
                    mHomeFragment.updateMapLocation(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!))
                    mViewModel.callCheckStatusAPI()
                    when {
                        mViewModel.currentStatus.value == TRANSPORT -> {
                            val intent = Intent(this@DashBoardActivity, TaxiDashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        mViewModel.currentStatus.value == SERVICE -> {
                            val intent = Intent(this@DashBoardActivity, XuberDashBoardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                        mViewModel.currentStatus.value == ORDER -> {
                            val intent = Intent(this@DashBoardActivity, TaxiDashboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                            startActivity(intent)
                        }
                    }
                }

                override fun onFailure(messsage: String?) {
                    mViewModel.callCheckStatusAPI()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getApiResponse() {
        println("RRR :: HomeFragment.getApiResponse")
        mViewModel.checkRequestLiveData.observe(this, Observer { checkStatusData ->
            run {
                writePreferences(PreferencesKey.CURRENCY_SYMBOL, checkStatusData.responseData.provider_details.currency_symbol)
                if (checkStatusData.statusCode == "200") if (!checkStatusData.responseData.requests.isNullOrEmpty()) {
                    mViewModel.currentStatus.value = checkStatusData.responseData.requests[0].status
                    writePreferences(PreferencesKey.FIRE_BASE_PROVIDER_IDENTITY, checkStatusData.responseData.provider_details.id)
                    writePreferences(PreferencesKey.CURRENCY_SYMBOL, checkStatusData.responseData.provider_details.currency_symbol)
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
                                if (getPermissionUtil().hasPermission(this, PERMISSIONS_LOCATION)) {
                                    val intent = Intent(this, TaxiDashboardActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                }
                            }
                            SERVICE -> {
                                BROADCAST = SERVICE
                                if (getPermissionUtil().hasPermission(this, PERMISSIONS_LOCATION)) {
                                    val intent = Intent(this, XuberDashBoardActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                }

                            }
                            ORDER -> {
                                BROADCAST = ORDER
                                if (getPermissionUtil().hasPermission(this, PERMISSIONS_LOCATION)) {
                                    val intent = Intent(this, FoodieDashboardActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                    startActivity(intent)
                                }
                            }
                            else -> BROADCAST = "BASE_BROADCAST"
                        }
                    }
                } else {
                    if (mIncomingRequestDialog.isShown()) {
                        mIncomingRequestDialog.dismiss()
                    }
                }
            }
        })

        observeLiveData(mViewModel.mProfileResponse) {
            val cityID = it.profileData.city.id
            PreferencesHelper.put(PreferencesKey.CITY_ID, cityID)
            SocketManager.emit(Constants.ROOM_NAME.COMMON_ROOM_NAME, Constants.ROOM_ID.COMMON_ROOM)
        }

        SocketManager.onEvent(Constants.ROOM_NAME.NEW_REQ, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK new request " + it[0])
            mViewModel.callCheckStatusAPI()
        })

        SocketManager.setOnSocketRefreshListener(object : SocketListener.connectionRefreshCallBack {
            override fun onRefresh() {
                SocketManager.emit(Constants.ROOM_NAME.COMMON_ROOM_NAME, Constants.ROOM_ID.COMMON_ROOM)
            }
        })
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRRR:: DashboardActivity")
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            val isGpsEnabled = intent.getBooleanExtra("ISGPS_EXITS", false)
            if (isGpsEnabled) {
                if (location != null) {
                    mViewModel.latitude.value = location.latitude
                    mViewModel.longitude.value = location.longitude
                    if (checkStatusApiCounter++ % 2 == 0) mViewModel.callCheckStatusAPI()
                }
            } else {
                if (isLocationDialogShown == false) {
                    isLocationDialogShown = true
                    CommonMethods.checkGps(context)
                }

            }
        }
    }

    override fun showErrorMessage(s: String) {
        ViewUtils.showNormalToast(this, s)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.e("Location","------Result"+requestCode)
        when (requestCode) {
            500 ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        isGPSEnabled = true
                        isLocationDialogShown = false
                        if (getPermissionUtil().hasPermission(this, PERMISSIONS_LOCATION)) {
                            ViewUtils.showGpsDialog(context)
                             Timer().schedule(10000) {
                                ViewUtils.dismissGpsDialog()
                                updateCurrentLocation()
                            }

                        }
                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }

            300 -> {
                 // getPermissionUtil().hasAllPermisson( Constants.RequestPermission.PERMISSIONS_LOCATION,context as AppCompatActivity ,300)

            }

        }
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun getInstance(): DashBoardActivity = this@DashBoardActivity



    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onConnected(p0: Bundle?) {
        // CommonMethods.checkGps(context)
    }

    override fun onConnectionSuspended(p0: Int) {

    }


}