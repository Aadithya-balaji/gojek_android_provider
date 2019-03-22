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
import com.xgek.xubermodule.arrived_fragment.ArrivedFragment
import com.xgek.xubermodule.databinding.ActivityXubermainBinding
import com.xgek.xubermodule.endservice_fragment.EndServicesFragment
import com.xgek.xubermodule.invoice_fragment.InvoiceFragment
import com.xgek.xubermodule.ratingFragment.RatingFragment
import com.xgek.xubermodule.startservice_fragment.StartServiceFragment
import kotlinx.android.synthetic.main.activity_xubermain.*
import permissions.dispatcher.*

@RuntimePermissions
class XuberMainActivity : BaseActivity<ActivityXubermainBinding>(), XuberMainNavigator, OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener, CommunicationListener {

    private lateinit var mViewDataBinding: ActivityXubermainBinding
    private lateinit var xuberMainViewModel: XuberMainViewModel

    private lateinit var fragmentMap: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null

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
        showServiceScreen()
    }

    private fun showServiceScreen() {
        val arrivedFragment: ArrivedFragment = ArrivedFragment.newInstance()
        replaceFragment(R.id.container, arrivedFragment, arrivedFragment.tag, false)
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

        updateLocationUIWithPermissionCheck()
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

    override fun onMessage(message: Any) {

        when (message) {

            "arrived" -> {

                val startServiceFragment: StartServiceFragment = StartServiceFragment.newInstance()
                replaceFragment(R.id.container, startServiceFragment, startServiceFragment.tag, true)
            }

            "startservice" -> {

                val endServicesFragment: EndServicesFragment = EndServicesFragment.newInstance()
                replaceFragment(R.id.container, endServicesFragment, endServicesFragment.tag, true)

            }

            "endservice" -> {

                val invoiceFragment: InvoiceFragment = InvoiceFragment.newInstance()
                replaceFragment(R.id.container, invoiceFragment, invoiceFragment.tag, true)
            }

            "confrimpayment" -> {

                val ratingFragment: RatingFragment = RatingFragment.newInstance()
                replaceFragment(R.id.container, ratingFragment, ratingFragment.tag, true)

            }
            "rating" -> {
                finish()
            }
        }
    }

}