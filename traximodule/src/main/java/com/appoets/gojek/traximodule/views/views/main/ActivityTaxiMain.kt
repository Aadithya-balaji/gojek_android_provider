package com.appoets.gojek.traximodule.views.views.main

import android.content.res.Resources
import android.location.Location
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.traximodule.R
import com.appoets.gojek.traximodule.views.views.bottomsheets.BottomStatuslayout
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.model.LatLng


class ActivityTaxiMain : BaseActivity<com.appoets.gojek.traximodule.databinding.ActivityTaxiMainBinding>(), ActivityTaxMainNavigator, OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener {


    private lateinit var activityTaxiMainBinding: com.appoets.gojek.traximodule.databinding.ActivityTaxiMainBinding
    private lateinit var fragmentMap: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null
    private  var bottomStatuslayout:BottomStatuslayout?=null

    override fun getLayoutId(): Int {
        return R.layout.activity_taxi_main
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.activityTaxiMainBinding = mViewDataBinding as com.appoets.gojek.traximodule.databinding.ActivityTaxiMainBinding
        val taxiModule = ActivityTaxiModule()
        taxiModule.setNavigator(this)
        activityTaxiMainBinding.taximainmodule = taxiModule
        bottomStatuslayout= BottomStatuslayout()
        bottomStatuslayout!!.show(supportFragmentManager,"bottomstatus")
        bottomStatuslayout!!.isCancelable=false

        //Initalize map
        initalizeMap()
    }

    fun initalizeMap() {
        fragmentMap = supportFragmentManager.findFragmentById(com.appoets.gojek.traximodule.R.id.taxi_map_fragment) as SupportMapFragment
        fragmentMap.getMapAsync(this@ActivityTaxiMain)

    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        try {
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, com.appoets.gojek.traximodule.R.raw.style_json))
            val latlong = LatLng(-33.8523341, 151.2106085)
            val location = CameraUpdateFactory.newLatLngZoom(
                    latlong, 15f)
            mGoogleMap!!.animateCamera(location)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }


    override fun onCameraMove() {
    }

    override fun onCameraIdle() {
    }

}