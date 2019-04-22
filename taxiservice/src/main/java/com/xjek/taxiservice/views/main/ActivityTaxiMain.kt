package com.xjek.taxiservice.views.main

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.location.Location
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.gson.Gson
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.Constants.DEFAULT_ZOOM
import com.xjek.base.data.Constants.RequestCode.PERMISSIONS_CODE_LOCATION
import com.xjek.base.data.Constants.RequestPermission.PERMISSIONS_LOCATION
import com.xjek.base.data.Constants.RideStatus.ACCEPTED
import com.xjek.base.data.Constants.RideStatus.ARRIVED
import com.xjek.base.data.Constants.RideStatus.CANCELLED
import com.xjek.base.data.Constants.RideStatus.COMPLETED
import com.xjek.base.data.Constants.RideStatus.DROPPED
import com.xjek.base.data.Constants.RideStatus.PICKED_UP
import com.xjek.base.data.Constants.RideStatus.SCHEDULED
import com.xjek.base.data.Constants.RideStatus.SEARCHING
import com.xjek.base.data.Constants.RideStatus.STARTED
import com.xjek.base.data.PreferencesKey.TAXI_CHECK_REQUEST_DATA
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.writePreferences
import com.xjek.base.utils.LocationCallBack
import com.xjek.base.utils.LocationUtils
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.ActivityTaxiMainBinding
import com.xjek.taxiservice.location_service.TaxiLocationService
import com.xjek.taxiservice.location_service.TaxiLocationService.BROADCAST
import com.xjek.taxiservice.location_service.TaxiLocationService.EXTRA_LOCATION
import com.xjek.taxiservice.views.bottomsheets.RideStatusBottomSheet

class ActivityTaxiMain : BaseActivity<ActivityTaxiMainBinding>(),
        ActivityTaxMainNavigator,
        OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener {

    private lateinit var activityTaxiMainBinding: ActivityTaxiMainBinding
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mViewModel: ActivityTaxiModule

    companion object {
        var showLoader: MutableLiveData<Boolean>? = null
    }

    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null
    private var mBottomSheet: RideStatusBottomSheet? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_taxi_main
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.activityTaxiMainBinding = mViewDataBinding as ActivityTaxiMainBinding
        mViewModel = ViewModelProviders.of(this).get(ActivityTaxiModule::class.java)
        activityTaxiMainBinding.taximainmodule = mViewModel
        mBottomSheet = RideStatusBottomSheet()
        mBottomSheet!!.show(supportFragmentManager, "bottomstatus")
        mBottomSheet!!.isCancelable = false

        initializeMap()
        checkStatusAPIResponse()
    }

    private fun initializeMap() {
        fragmentMap = supportFragmentManager.findFragmentById(R.id.taxi_map_fragment) as SupportMapFragment
        fragmentMap.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        try {
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
            updateCurrentLocation()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onCameraMove() {

    }

    override fun onCameraIdle() {
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {

        mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
        mGoogleMap!!.uiSettings.isCompassEnabled = true

        if (getPermissionUtil().hasPermission(this, PERMISSIONS_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    mLastKnownLocation = location
                    updateMapLocation(LatLng(location!!.latitude, location.longitude))
                }

                override fun onFailure(messsage: String?) {
                }
            })
        else if (getPermissionUtil().requestPermissions(this, PERMISSIONS_LOCATION, PERMISSIONS_CODE_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    updateMapLocation(LatLng(location!!.latitude, location.longitude))
                }

                override fun onFailure(messsage: String?) {
                }
            })
    }

    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = false) {
        if (!isAnimateMap) mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
        else mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
    }

    override fun onResume() {
        super.onResume()
        if (!isMyServiceRunning(TaxiLocationService::class.java))
            startService(Intent(this, TaxiLocationService::class.java))
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BROADCAST))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRR MyReceiver.onReceive")
            val location = intent!!.getParcelableExtra<Location>(EXTRA_LOCATION)
            if (location != null) {

                mViewModel.latitude.value = location.latitude
                mViewModel.longitude.value = location.longitude
                mViewModel.callTaxiCheckStatusAPI()
            }
        }
    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE))
            if (serviceClass.name == service.service.className) return true
        return false
    }

    override fun showErrorMessage(error: String) {
        showLoader!!.value = false
    }


    private fun checkStatusAPIResponse() {

        observeLiveData(mViewModel.checkStatusTaxiLiveData) {

            val checkStatusModel = mViewModel.checkStatusTaxiLiveData.value

            if (checkStatusModel?.statusCode.equals("200")) {
                val providerDetailsModel =
                        checkStatusModel?.responseData!!.provider_details

                try {
                    if (providerDetailsModel != null) {
                        writePreferences(TAXI_CHECK_REQUEST_DATA, Gson().toJson(checkStatusModel.responseData))

                        when (checkStatusModel.responseData!!.request!!.status) {
                            SEARCHING -> {

                            }
                            SCHEDULED -> {

                            }
                            CANCELLED -> {

                            }
                            ACCEPTED -> {

                            }
                            STARTED -> {
                                mBottomSheet!!.whenStatusStarted(checkStatusModel.responseData)
                            }
                            ARRIVED -> {

                            }
                            PICKED_UP -> {

                            }
                            DROPPED -> {

                            }
                            COMPLETED -> {

                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }



        //WaitingTIme
        observeLiveData(mViewModel.waitingTimeLiveData){
            if(mViewModel.waitingTimeLiveData.value!=null){
                val waitingTime=   mViewModel.waitingTimeLiveData.value!!.waitingStatus
                if(waitingTime==1){

                }else{

                }
            }
        }
    }
}