package com.xgek.xubermodule.xuberMainActivity

import android.Manifest
import android.annotation.SuppressLint
import android.content.res.Resources
import android.location.Location
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.appoets.basemodule.base.BaseActivity
import com.appoets.basemodule.utils.Constants.MapConfig.DEFAULT_LOCATION
import com.appoets.basemodule.utils.Constants.MapConfig.DEFAULT_ZOOM
import com.appoets.basemodule.utils.LocationCallBack
import com.appoets.basemodule.utils.LocationUtils
import com.appoets.basemodule.utils.ViewUtils
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.xgek.xubermodule.R
import com.xgek.xubermodule.databinding.ActivityXubermainBinding
import kotlinx.android.synthetic.main.activity_xubermain.*
import permissions.dispatcher.*

@RuntimePermissions
class XuberMainActivity : BaseActivity<ActivityXubermainBinding>(), XuberMainNavigator, OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener {

    private lateinit var mViewDataBinding : ActivityXubermainBinding
    private lateinit var xuberMainViewModel: XuberMainViewModel

    private lateinit var fragmentMap: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null

    private var mFlowStatus: String = "EMPTY"


    private lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>


    override fun getLayoutId(): Int = R.layout.activity_xubermain

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityXubermainBinding
        xuberMainViewModel = ViewModelProviders.of(this).get(XuberMainViewModel::class.java)
        xuberMainViewModel.setNavigator(this)

        mViewDataBinding.xuberViewModel = xuberMainViewModel
        mViewDataBinding.executePendingBindings()


        setBottomSheetListener()
        initialiseMap()
//        showServiceScreen()
    }

    private fun setBottomSheetListener() {
        sheetBehavior = BottomSheetBehavior.from<FrameLayout>(container)
        /*  *//**
         * bottom sheet state change listener
         * we are changing button text when sheet changed state
         * *//*
        sheetBehavior.setBottomSheetCallback(object : BottomSheetCallback() {
            @SuppressLint("SwitchIntDef")
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    STATE_HIDDEN -> sheetBehavior.isHideable = true
                    STATE_EXPANDED -> sheetBehavior.isHideable = true
                    STATE_COLLAPSED -> sheetBehavior.isHideable = true
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) = Unit
        })*/
    }

    private fun initialiseMap() {
        fragmentMap = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        fragmentMap.getMapAsync(this@XuberMainActivity)

    }

    override fun goToLocationPick() {
        try {
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        this.mGoogleMap = mGoogleMap

        this.mGoogleMap?.setOnCameraMoveListener(this@XuberMainActivity)
        this.mGoogleMap?.setOnCameraIdleListener(this@XuberMainActivity)

        updateLocationUIWithPermissionCheck()
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun updateLocationUI() {

        mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = false
        mGoogleMap!!.uiSettings.isCompassEnabled = false


        LocationUtils.getLastKnownLocation(this@XuberMainActivity, object : LocationCallBack.LastKnownLocation {
            override fun onSuccess(location: Location) {
                mLastKnownLocation = location
                updateMapLocation(LatLng(
                        location.latitude,
                        location.longitude
                ))
            }

            override fun onFailure(messsage: String?) {
                updateMapLocation(DEFAULT_LOCATION)
            }
        })
    }

    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = false) {
        if (!isAnimateMap) {
            mGoogleMap?.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(location, DEFAULT_ZOOM))
        } else {
            mGoogleMap?.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(location, DEFAULT_ZOOM))
        }
        //mGoogleMap!!.addMarker(MarkerOptions().icon(ViewUtils.bitmapDescriptorFromVector(this@TaxiMainActivity,R.drawable.ic_taxi_pin)).position(location).anchor(0.5f,0.5f))
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onLocationPermissionDenied() {
        ViewUtils.showToast(this@XuberMainActivity, R.string.location_permission_denied, false)
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onShowRationale(request: PermissionRequest) {
        ViewUtils.showRationaleAlert(this@XuberMainActivity, R.string.location_permission_rationale, request)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun goBack() {
    }

    override fun showCurrentLocation() {
    }

    override fun moveStatusFlow() {
    }

    override fun onMapReady(p0: GoogleMap?) {
    }

    override fun onCameraMove() {
    }

    override fun onCameraIdle() {
    }

}