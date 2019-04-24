package com.xjek.taxiservice.views.main

import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.Location
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
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
import com.xjek.base.data.Constants.RideStatus.STARTED
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.LocationCallBack
import com.xjek.base.utils.LocationUtils
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.ActivityTaxiMainBinding
import com.xjek.taxiservice.views.bottomsheets.RideStatusBottomSheet
import kotlinx.android.synthetic.main.activity_taxi_main.*
import com.xjek.taxiservice.views.main.ActivityTaxiMain.Companion.showLoader
import java.util.*

class ActivityTaxiMain : BaseActivity<ActivityTaxiMainBinding>(),
        ActivityTaxMainNavigator,
        OnMapReadyCallback {

    private lateinit var activityTaxiMainBinding: ActivityTaxiMainBinding
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mViewModel: ActivityTaxiModule

    private val taxiCheckStatusTimer = Timer()

    companion object {
        var showLoader: MutableLiveData<Boolean>? = null
    }

    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null
    private var mBottomSheet: RideStatusBottomSheet? = null

    override fun getLayoutId(): Int = R.layout.activity_taxi_main

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
            taxiCheckStatusTimer.schedule(object : TimerTask() {
                override fun run() {
                    println("RRR :: ActivityTaxiMain.run")
                    if (mGoogleMap != null) updateCurrentLocation()
                }
            }, 0, 8000)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {
        runOnUiThread {
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
            mGoogleMap!!.uiSettings.isCompassEnabled = true
        }

        if (getPermissionUtil().hasPermission(this, PERMISSIONS_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    mLastKnownLocation = location
                    if (location != null) {
                        mViewModel.latitude.value = location.latitude
                        mViewModel.longitude.value = location.longitude
                        mViewModel.callTaxiCheckStatusAPI()
                    }
                    updateMapLocation(LatLng(location!!.latitude, location.longitude))
                }

                override fun onFailure(messsage: String?) {
                }
            })
        else if (getPermissionUtil().requestPermissions(this, PERMISSIONS_LOCATION, PERMISSIONS_CODE_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    if (location != null) {
                        mViewModel.latitude.value = location.latitude
                        mViewModel.longitude.value = location.longitude
                        mViewModel.callTaxiCheckStatusAPI()
                    }
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

    override fun onDestroy() {
        super.onDestroy()
        taxiCheckStatusTimer.cancel()
    }

    override fun showErrorMessage(error: String) {
        showLoader!!.value = false
    }

    private fun checkStatusAPIResponse() {
        observeLiveData(mViewModel.checkStatusTaxiLiveData) { checkStatusResponse ->
            if (checkStatusResponse?.statusCode.equals("200")) try {
                if (checkStatusResponse?.responseData!!.provider_details != null) {
                    println("RRR :: Status = ${checkStatusResponse.responseData!!.request.status}")
                    btTempProviderStatus.text = checkStatusResponse.responseData!!.request.status
                    if (!checkStatusResponse.responseData!!.request.status.isNullOrEmpty()) {
                        println("RRR :: inside if = ")
                        when (checkStatusResponse.responseData!!.request.status) {
                            SCHEDULED -> {
                                println("RRR :: inside SCHEDULED = ")
                            }
                            CANCELLED -> {
                                println("RRR :: inside CANCELLED = ")
                            }
                            ACCEPTED -> {
                                println("RRR :: inside ACCEPTED = ")
                            }
                            STARTED -> {
                                println("RRR :: inside STARTED = ")
                                mBottomSheet!!.whenStatusStarted(checkStatusResponse.responseData)
                            }
                            ARRIVED -> {
                                println("RRR :: inside ARRIVED = ")
                                mBottomSheet!!.whenStatusArrived(checkStatusResponse.responseData)
                            }
                            PICKED_UP -> {
                                println("RRR :: inside PICKED_UP = ")
                                mBottomSheet!!.whenStatusArrived(checkStatusResponse.responseData)
                            }
                            DROPPED -> {
                                println("RRR :: inside DROPPED = ")
                                mBottomSheet!!.whenStatusArrived(checkStatusResponse.responseData)
                            }
                            COMPLETED -> {
                                println("RRR :: inside COMPLETED = ")
                                mBottomSheet!!.whenStatusArrived(checkStatusResponse.responseData)
                            }
                        }
                    } else println("RRR :: inside else = ${checkStatusResponse.responseData!!.request.status}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //      WaitingTIme
        observeLiveData(mViewModel.waitingTimeLiveData) {
            if (mViewModel.waitingTimeLiveData.value != null) {
                val waitingTime = mViewModel.waitingTimeLiveData.value!!.waitingStatus
                if (waitingTime == 1) {

                } else {

                }
            }
        }
    }
}