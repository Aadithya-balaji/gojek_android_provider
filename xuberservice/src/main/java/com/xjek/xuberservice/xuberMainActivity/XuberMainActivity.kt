package com.xjek.xuberservice.xuberMainActivity

import android.content.res.Resources
import android.location.Location
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseActivity
import com.xjek.base.utils.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.xjek.base.data.Constants
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.ActivityXubermainBinding
import com.xjek.xuberservice.serviceStatus.ServiceBottomSheet
import kotlinx.android.synthetic.main.activity_xubermain.*
import java.util.*


class XuberMainActivity : BaseActivity<ActivityXubermainBinding>(), XuberMainNavigator, OnMapReadyCallback, GoogleMap.OnCameraMoveListener,GoogleMap.OnCameraIdleListener {

    private lateinit var xuberMainViewModel: XuberMainViewModel
    private lateinit var fragmentMap: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null
    private val taxiCheckStatusTimer = Timer()
    private  lateinit var  activityXuberMainBinding:ActivityXubermainBinding


    private lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>
    override fun getLayoutId(): Int = R.layout.activity_xubermain
    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityXuberMainBinding=mViewDataBinding as ActivityXubermainBinding
        xuberMainViewModel= XuberMainViewModel()
        xuberMainViewModel.navigator=this
        activityXuberMainBinding.xuberViewModel=xuberMainViewModel
        setBottomSheetListener()
        initialiseMap()
        showBottomSheet()
    }

     fun showBottomSheet(){
         val serviceBottomSheet=ServiceBottomSheet()
         serviceBottomSheet!!.show(supportFragmentManager, "bottomservice")
         serviceBottomSheet!!.isCancelable = false
     }

    private fun setBottomSheetListener() {
        sheetBehavior = BottomSheetBehavior.from<FrameLayout>(container)
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
    }


    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = false) {
        if (!isAnimateMap) {
            mGoogleMap?.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(location, Constants.DEFAULT_ZOOM))
        } else {
            mGoogleMap?.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(location, Constants.DEFAULT_ZOOM))
        }
        //mGoogleMap!!.addMarker(MarkerOptions().icon(ViewUtils.bitmapDescriptorFromVector(this@TaxiMainActivity,R.drawable.ic_taxi_pin)).position(location).anchor(0.5f,0.5f))
    }



    override fun goBack() {
        onBackPressed()

    }

    override fun showCurrentLocation() {

        if (mLastKnownLocation != null) {
            updateMapLocation(LatLng(
                    mLastKnownLocation!!.latitude,
                    mLastKnownLocation!!.longitude
            ), true)
        }
    }

    override fun moveStatusFlow() {
    }

    override fun onMapReady(mGoogleMap: GoogleMap?) {
        try {
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        this.mGoogleMap = mGoogleMap

        this.mGoogleMap?.setOnCameraMoveListener(this@XuberMainActivity)
        this.mGoogleMap?.setOnCameraIdleListener(this@XuberMainActivity)
    }

    override fun onCameraMove() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCameraIdle() {
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) run {
            if (supportFragmentManager.backStackEntryCount == 1) {
                sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }



}