package com.xjek.taxiservice.views.main

import android.annotation.SuppressLint
import android.content.*
import android.content.res.Resources
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
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
import com.xjek.base.data.Constants.RideStatus.STARTED
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.location_service.BaseLocationService
import com.xjek.base.location_service.BaseLocationService.BROADCAST
import com.xjek.base.utils.LocationCallBack
import com.xjek.base.utils.LocationUtils
import com.xjek.base.utils.ViewUtils
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.ActivityTaxiMainBinding
import com.xjek.taxiservice.model.ResponseData
import com.xjek.taxiservice.views.invoice.TaxiTaxiInvoiceActivity
import com.xjek.taxiservice.views.tollcharge.TollChargeDialog
import com.xjek.taxiservice.views.verifyotp.VerifyOtpDialog
import kotlinx.android.synthetic.main.layout_status_indicators.*
import kotlinx.android.synthetic.main.taxi_bottom.*
import java.util.*

class TaxiDashboardActivity : BaseActivity<ActivityTaxiMainBinding>(),
        TaxiDashboardNavigator,
        OnMapReadyCallback {

    private lateinit var activityTaxiMainBinding: ActivityTaxiMainBinding
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mViewModel: TaxiDashboardViewModel
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null

    override fun getLayoutId(): Int = R.layout.activity_taxi_main

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.activityTaxiMainBinding = mViewDataBinding as ActivityTaxiMainBinding
        mViewModel = ViewModelProviders.of(this).get(TaxiDashboardViewModel::class.java)
        mViewModel.navigator = this
        activityTaxiMainBinding.taximainmodule = mViewModel

        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bsContainer)
        sheetBehavior.peekHeight = 250

        if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>

        initializeMap()
        checkStatusAPIResponse()

        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BROADCAST))
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
                        updateMapLocation(LatLng(location.latitude, location.longitude))
                    }
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
                        updateMapLocation(LatLng(location.latitude, location.longitude))
                    }
                }

                override fun onFailure(messsage: String?) {
                }
            })
    }

    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = false) {
        if (!isAnimateMap) mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
        else mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
    }

    override fun showErrorMessage(error: String) {
        mViewModel.showLoading.value = false
    }

    private fun checkStatusAPIResponse() {
        mViewModel.showLoading.value = true
        observeLiveData(mViewModel.checkStatusTaxiLiveData) { checkStatusResponse ->
            if (checkStatusResponse?.statusCode.equals("200")) try {
                mViewModel.showLoading.value = false
                if (checkStatusResponse.responseData.request.status.isNotEmpty()) {
                    println("RRR :: Status = ${checkStatusResponse.responseData.request.status}")
                    if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    when (checkStatusResponse.responseData.request.status) {
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
                            whenStatusStarted(checkStatusResponse.responseData)
                        }
                        ARRIVED -> {
                            println("RRR :: inside ARRIVED = ")
                            whenStatusArrived(checkStatusResponse.responseData)
                        }
                        PICKED_UP -> {
                            println("RRR :: inside PICKED_UP = ")
                            whenStatusPickedUp(checkStatusResponse.responseData)
                        }
                        DROPPED -> {
                            println("RRR :: inside DROPPED = ")
                            val strCheckRequestModel = Gson().toJson(checkStatusResponse.responseData)
                            startActivity(Intent(this, TaxiTaxiInvoiceActivity::class.java).putExtra("ResponseData", strCheckRequestModel))
                            finish()
                        }
                        COMPLETED -> {
                            println("RRR :: inside COMPLETED = ")
                            val strCheckRequestModel = Gson().toJson(checkStatusResponse.responseData)
                            startActivity(Intent(this, TaxiTaxiInvoiceActivity::class.java).putExtra("ResponseData", strCheckRequestModel))
                            finish()
                        }
                    }
                } else println("RRR :: inside else = ${checkStatusResponse.responseData.request.status}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        observeLiveData(mViewModel.waitingTimeLiveData) {
            if (mViewModel.waitingTimeLiveData.value != null) {
                val waitingTime = mViewModel.waitingTimeLiveData.value!!.waitingStatus
                if (waitingTime == 1) {

                } else {

                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    private fun whenStatusStarted(responseData: ResponseData) {
        btn_cancel.visibility = View.VISIBLE
        btn_arrived.visibility = View.VISIBLE
        btn_picked_up.visibility = View.GONE
        llWaitingTimeContainer.visibility = View.GONE

        Glide
                .with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder))
                .load(responseData.request.user.picture)
                .into(civProfile)

        tv_user_name.text = responseData.request.user.first_name + " " + responseData.request.user.last_name
        tv_user_address_one.text = responseData.request.s_address
        rate.rating = responseData.request.user.rating.toFloat()

        btn_arrived.setOnClickListener {
            val params: HashMap<String, String> = HashMap()
            params["id"] = responseData.request.id.toString()
            params["status"] = ARRIVED
            params["_method"] = "PATCH"
            mViewModel.taxiStatusUpdate(params)
        }
    }

    private fun whenStatusArrived(responseData: ResponseData) {
        ib_location_pin.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)
        btn_cancel.visibility = View.VISIBLE
        btn_arrived.visibility = View.GONE
        btn_picked_up.visibility = View.VISIBLE

        Glide
                .with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder))
                .load(responseData.request.user.picture)
                .into(civProfile)

        tv_user_name.text = responseData.request.user.first_name + " " + responseData.request.user.last_name
        tv_user_address_one.text = responseData.request.s_address
        rate.rating = responseData.request.user.rating.toFloat()

        btn_picked_up.setOnClickListener {
            val otpDialogFragment = VerifyOtpDialog.newInstance(
                    responseData.request.otp,
                    responseData.request.id
            )
            otpDialogFragment.show(supportFragmentManager, "VerifyOtpDialog")
        }
    }

    private fun whenStatusPickedUp(responseData: ResponseData) {
        llWaitingTimeContainer.visibility = View.VISIBLE
        ib_location_pin.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)
        ib_steering.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)
        ib_location_pin.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)

        btn_cancel.visibility = View.GONE
        btn_arrived.visibility = View.GONE
        btn_picked_up.visibility = View.GONE
        btn_drop.visibility = View.VISIBLE

        vl_trip_started.visibility = View.VISIBLE

        Glide
                .with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder))
                .load(responseData.request.user.picture)
                .into(civProfile)

        tv_user_name.text = responseData.request.user.first_name + " " + responseData.request.user.last_name
        tv_user_address_one.text = responseData.request.s_address
        rate.rating = responseData.request.user.rating.toFloat()

        btn_drop.setOnClickListener {
            ViewUtils.showAlert(this, resources.getString(R.string.do_you_have_any_toll), object : ViewUtils.ViewCallBack {
                override fun onPositiveButtonClick(dialog: DialogInterface) {
                    val tollChargeDialog = TollChargeDialog()
                    val bundle = Bundle()
                    bundle.putString("requestID", responseData.request.id.toString())
                    tollChargeDialog.arguments = bundle
                    tollChargeDialog.show(supportFragmentManager, "tollCharge")
                }

                override fun onNegativeButtonClick(dialog: DialogInterface) {
                    dialog.dismiss()
                }
            })
        }
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRRR:: TaxiDashboardActivity")
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            if (location != null) {
                mViewModel.latitude.value = location.latitude
                mViewModel.longitude.value = location.longitude
                mViewModel.callTaxiCheckStatusAPI()
                updateMapLocation(LatLng(location.latitude, location.longitude))
            }
        }
    }

    private fun openNavigation(){

        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr=20.344,34.34&daddr=20.5666,45.345")))
    }
}