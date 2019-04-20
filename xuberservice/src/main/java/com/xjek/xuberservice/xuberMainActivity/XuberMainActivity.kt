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
import com.xjek.xuberservice.arrived_fragment.ArrivedFragment
import com.xjek.xuberservice.databinding.ActivityXubermainBinding
import com.xjek.xuberservice.endservice_fragment.EndServicesFragment
import com.xjek.xuberservice.invoice_fragment.InvoiceFragment
import com.xjek.xuberservice.ratingFragment.RatingFragment
import com.xjek.xuberservice.startservice_fragment.StartServiceFragment
import kotlinx.android.synthetic.main.activity_xubermain.*


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
        xuberMainViewModel.navigator = this

        mViewDataBinding.xuberViewModel = xuberMainViewModel
        mViewDataBinding.executePendingBindings()


        setBottomSheetListener()
        initialiseMap()
        showServiceScreen()
    }

    private fun showServiceScreen() {
        val arrivedFragment: ArrivedFragment = ArrivedFragment.newInstance()
        replaceExistingFragment(R.id.container, arrivedFragment, arrivedFragment.tag, false)
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

    /*@SuppressLint("MissingPermission")
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
                updateMapLocation(Constants.DEFAULT_LOCATION)
            }
        })
    }*/

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

    /*@OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onLocationPermissionDenied() {
        ViewUtils.showToast(this@XuberMainActivity, resources.getString(R.string
                .location_permission_denied), false)
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun onShowRationale(request: PermissionRequest) {
        ViewUtils.showRationaleAlert(this@XuberMainActivity, resources.getString(R.string
                .location_permission_rationale), request)
    }
*/

   /* override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }*/

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

    override fun onMessage(message: Any) {

        when (message) {

            "arrived" -> {

                val startServiceFragment: StartServiceFragment = StartServiceFragment.newInstance()
                replaceExistingFragment(R.id.container, startServiceFragment, startServiceFragment.tag, true)
            }

            "startservice" -> {

                val endServicesFragment: EndServicesFragment = EndServicesFragment.newInstance()
                replaceExistingFragment(R.id.container, endServicesFragment, endServicesFragment.tag, true)

            }

            "endservice" -> {

                val invoiceFragment: InvoiceFragment = InvoiceFragment.newInstance()
                replaceExistingFragment(R.id.container, invoiceFragment, invoiceFragment.tag, true)
            }

            "confrimpayment" -> {

                val ratingFragment: RatingFragment = RatingFragment.newInstance()
                replaceExistingFragment(R.id.container, ratingFragment, ratingFragment.tag, true)

            }
            "rating" -> {
                finish()
            }
        }
    }

}