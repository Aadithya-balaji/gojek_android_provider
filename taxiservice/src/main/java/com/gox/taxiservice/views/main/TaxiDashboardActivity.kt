package com.gox.taxiservice.views.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Chronometer
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.gox.base.BuildConfig
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.chatmessage.ChatActivity
import com.gox.base.data.Constants
import com.gox.base.data.Constants.BroadCastTypes.BASE_BROADCAST
import com.gox.base.data.Constants.DEFAULT_ZOOM
import com.gox.base.data.Constants.ModuleTypes.TRANSPORT
import com.gox.base.data.Constants.RequestCode.PERMISSIONS_CODE_LOCATION
import com.gox.base.data.Constants.RequestPermission.PERMISSIONS_LOCATION
import com.gox.base.data.Constants.RideStatus.ARRIVED
import com.gox.base.data.Constants.RideStatus.COMPLETED
import com.gox.base.data.Constants.RideStatus.DROPPED
import com.gox.base.data.Constants.RideStatus.PICKED_UP
import com.gox.base.data.Constants.RideStatus.SEARCHING
import com.gox.base.data.Constants.RideStatus.STARTED
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.data.PreferencesKey.CAN_SAVE_LOCATION
import com.gox.base.data.PreferencesKey.CAN_SEND_LOCATION
import com.gox.base.data.PreferencesKey.CURRENT_TRANXIT_STATUS
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.writePreferences
import com.gox.base.location_service.BaseLocationService
import com.gox.base.location_service.BaseLocationService.Companion.BROADCAST
import com.gox.base.persistence.AppDatabase
import com.gox.base.persistence.LocationPointsEntity
import com.gox.base.socket.SocketListener
import com.gox.base.socket.SocketManager
import com.gox.base.utils.*
import com.gox.base.utils.distanceCalc.DistanceCalcModel
import com.gox.base.utils.distanceCalc.DistanceListener
import com.gox.base.utils.distanceCalc.DistanceProcessing
import com.gox.base.utils.distanceCalc.DistanceUtils
import com.gox.base.utils.polyline.DirectionUtils
import com.gox.base.utils.polyline.PolyLineListener
import com.gox.base.utils.polyline.PolylineUtil
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.ActivityTaxiMainBinding
import com.gox.taxiservice.interfaces.GetReasonsInterface
import com.gox.taxiservice.model.CancelRequestModel
import com.gox.taxiservice.model.DistanceApiProcessing
import com.gox.taxiservice.model.ResponseData
import com.gox.taxiservice.views.invoice.TaxiInvoiceActivity
import com.gox.taxiservice.views.reasons.TaxiCancelReasonFragment
import com.gox.taxiservice.views.tollcharge.TollChargeDialog
import com.gox.taxiservice.views.verifyotp.VerifyOtpDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_taxi_main.*
import kotlinx.android.synthetic.main.layout_status_indicators.*
import kotlinx.android.synthetic.main.layout_taxi_status_container.*
import org.json.JSONObject
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.concurrent.schedule

class TaxiDashboardActivity : BaseActivity<ActivityTaxiMainBinding>(),
        TaxiDashboardNavigator,
        OnMapReadyCallback,
        PolyLineListener,
        DistanceListener,
        GetReasonsInterface,
        Chronometer.OnChronometerTickListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        View.OnClickListener {

    private var currentLat: Double = 0.0
    private var currentlng: Double = 0.0

    private lateinit var mSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var mBinding: ActivityTaxiMainBinding
    private lateinit var mViewModel: TaxiDashboardViewModel
    private lateinit var mMapFragment: SupportMapFragment
    private lateinit var context: Context

    private var polyLine: ArrayList<LatLng> = ArrayList()
    private var mLastKnownLocation: Location? = null
    private var canDrawPolyLine: Boolean = true
    private var mGoogleMap: GoogleMap? = null
    private var mPolyline: Polyline? = null
    private var polyUtil = PolyUtil()
    private var providerMarker: Marker? = null

    private var srcMarker: Marker? = null
    private var isWaitingTime: Boolean = false
    private var lastWaitingTime: Long? = 0
    private var isNeedToUpdateWaiting: Boolean = true
    private var isLocationDialogShown: Boolean = false
    private var isGPSEnabled: Boolean = false
    private var startLatLng = LatLng(0.0, 0.0)
    private var endLatLng = LatLng(0.0, 0.0)
    private var checkStatusApiCounter = 0
    private var roomConnected: Boolean = false
    private var reqID: Int = 0

    private var doubleBackToExit: Boolean = false
    private var distanceApiCallCount = 0

    private var iteratePointsForDistanceCalc = ArrayList<LatLng>()
    private var tempPointForDistanceCal: LatLng? = null
    private var iteratePointsForDistanceCal = 500.0
    private var iteratePointsForApi = 50.0
    private var points = ArrayList<LocationPointsEntity>()
    private var tempPoints = ArrayList<LatLng>()
    private var tempPoint: LatLng? = null
    private var polylineKey = ""
    private var sosCall = ""
    private var checkRequestTimer: Timer? = null
    private var patternMatcher: Pattern? = null
    private  var tempCurrentLatLon :LatLng?=LatLng(0.0000,0.0000)



    override fun getLayoutId() = R.layout.activity_taxi_main

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        this.mBinding = mViewDataBinding as ActivityTaxiMainBinding
        mViewModel = ViewModelProviders.of(this).get(TaxiDashboardViewModel::class.java)
        mViewModel.navigator = this
        context = this
        checkRequestTimer = Timer()
        mBinding.lifecycleOwner = this
        mBinding.taximainmodule = mViewModel
        mViewModel.currentStatus.value = ""
        mSheetBehavior = BottomSheetBehavior.from(bsContainer)
//        mSheetBehavior.peekHeight = resources.getDimension(R.dimen._280sdp).toInt()
        mSheetBehavior.peekHeight = 850
        btnWaiting.setOnClickListener(this)
        cmWaiting.onChronometerTickListener = this
        polylineKey = getText(R.string.google_map_key).toString()
        if (mSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) mSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        fab_taxi_menu.isIconAnimated = false
        fab_taxi_menu.setPadding(50, 50, 50, 50)

        checkRequestTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mViewModel.callTaxiCheckStatusAPI()
            }
        }, 0, 5000)

        observeLiveData(mViewModel.showLoading) {
            loadingObservable.value = it
        }

        ibNavigation.setOnClickListener {
            openGoogleNavigation()
        }

        tvSos.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(sosCall)))
        }

        fab_taxi_menu_chat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        btn_cancel.setOnClickListener {
            TaxiCancelReasonFragment().show(supportFragmentManager, "TaxiCancelReasonFragment")
        }

        MapsInitializer.initialize(this)
        initializeMap()

        observeLiveDataVariables()
    }

    private fun initializeMap() {
        mMapFragment = supportFragmentManager.findFragmentById(R.id.taxi_map_fragment) as SupportMapFragment
        mMapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        try {
            this.mGoogleMap?.setOnCameraMoveListener(this)
            this.mGoogleMap?.setOnCameraIdleListener(this)
            mGoogleMap!!.isMyLocationEnabled = false
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
            updateCurrentLocation()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onCameraMove() {
//        if (mSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
//            mSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCameraIdle() {
//        mSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    @SuppressLint("MissingPermission")
    override fun updateCurrentLocation() {
        runOnUiThread {
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
            mGoogleMap!!.uiSettings.isCompassEnabled = true
        }
        if (getPermissionUtil().hasPermission(this, PERMISSIONS_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack {
                override fun onSuccess(location: Location) {
                    mLastKnownLocation = location
                    mViewModel.latitude.value = location.latitude
                    mViewModel.longitude.value = location.longitude
                    mViewModel.callTaxiCheckStatusAPI()
                    updateMapLocation(LatLng(location.latitude, location.longitude))
                }

                override fun onFailure(message: String) {
                }
            })
        else if (getPermissionUtil().requestPermissions(this, PERMISSIONS_LOCATION, PERMISSIONS_CODE_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack {
                override fun onSuccess(location: Location) {
                    mViewModel.latitude.value = location.latitude
                    mViewModel.longitude.value = location.longitude
                    mViewModel.callTaxiCheckStatusAPI()
                    updateMapLocation(LatLng(location.latitude, location.longitude))
                }

                override fun onFailure(message: String) {
                }
            })
    }

    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = false) {
        providerMarker?.remove()
        try {
            providerMarker = mGoogleMap?.addMarker(MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap
            (bitmapFromVector(BaseApplication.getBaseApplicationContext, R.drawable.ic_marker_provider))))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!isAnimateMap) mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
        else mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
    }

    override fun showErrorMessage(error: String) {
        runOnUiThread { mViewModel.showLoading.value = false }
    }

    private fun observeLiveDataVariables() {
        mViewModel.showLoading.value = true

        mViewModel.checkStatusTaxiLiveData.observe(this, Observer { checkStatusResponse ->
            if (checkStatusResponse?.statusCode.equals("200")) try {
                mViewModel.showLoading.value = false

                if (!checkStatusResponse.responseData.request.status.isNullOrEmpty()) {
                    println("RRR :: Status = ${checkStatusResponse.responseData.request.status}")
                   /* if (currentLat != 0.0 || currentLat != checkStatusResponse.responseData.request.s_latitude &&
                            currentlng != 0.0 || currentlng != checkStatusResponse.responseData.request.s_longitude) {

                        mViewModel.currentStatus.value = ""
                    }*/


                    if(!roomConnected){
                        reqID = checkStatusResponse.responseData.request.id
                        PreferencesHelper.put(PreferencesKey.TRANSPORT_REQ_ID, reqID)
                        if (reqID!=0) {
                            SocketManager.emit(Constants.RoomName.TRANSPORT_ROOM_NAME, Constants.RoomId.getTransportRoom(reqID))
                        }

                    }

                    //if (mSheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) mSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                    if (mViewModel.currentStatus.value != checkStatusResponse.responseData.request.status) {

                        currentLat = checkStatusResponse.responseData.request.s_latitude!!
                        currentlng = checkStatusResponse.responseData.request.s_longitude!!

                        writePreferences(PreferencesKey.FIRE_BASE_PROVIDER_IDENTITY,
                                checkStatusResponse.responseData.provider_details.id)
                        mViewModel.currentStatus.value = checkStatusResponse.responseData.request.status
                        writePreferences(CURRENT_TRANXIT_STATUS, mViewModel.currentStatus.value)

                        sosCall = "tel:${checkStatusResponse.responseData.sos}"

                        fab_taxi_menu_call.setOnClickListener {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:${checkStatusResponse.responseData.request.user.mobile}")
                            startActivity(intent)
                        }

                        writePreferences(Constants.Chat.ADMIN_SERVICE, TRANSPORT)
                        writePreferences(Constants.Chat.USER_ID, checkStatusResponse.responseData.request.user_id)
                        writePreferences(Constants.Chat.REQUEST_ID, checkStatusResponse.responseData.request.id)
                        writePreferences(Constants.Chat.PROVIDER_ID, checkStatusResponse.responseData.request.provider_id)
                        writePreferences(Constants.Chat.USER_NAME, checkStatusResponse.responseData.request.user.first_name +
                                checkStatusResponse.responseData.request.user.last_name)
                        writePreferences(Constants.Chat.PROVIDER_NAME, checkStatusResponse.responseData.provider_details.first_name +
                                checkStatusResponse.responseData.provider_details.last_name)

                        val requestID = checkStatusResponse.responseData.request.id.toString()

                       /* if (!roomConnected) {
                            roomConnected = true
                            val reqID = checkStatusResponse.responseData.request.id
                            PreferencesHelper.put(PreferencesKey.TRANSPORT_REQ_ID, reqID)
                            SocketManager.emit(Constants.RoomName.TRANSPORT_ROOM_NAME, Constants.RoomId.TRANSPORT_ROOM)
                        }*/

                        when (checkStatusResponse.responseData.request.status) {
                            SEARCHING -> {
                                val params = HashMap<String, String>()
                                params[Constants.Common.ID] = requestID
                                mViewModel.taxiWaitingTime(params)
                            }

                            STARTED -> whenStatusStarted(checkStatusResponse.responseData)

                            ARRIVED -> whenStatusArrived(checkStatusResponse.responseData)

                            PICKED_UP -> {
                                tvSos.visibility = View.VISIBLE
                                whenStatusPickedUp(checkStatusResponse.responseData)
                            }

                            DROPPED -> {
                                println("RRR :: inside DROPPED = ")
                                writePreferences(CAN_SEND_LOCATION, false)
                                writePreferences(CAN_SAVE_LOCATION, false)
                                startActivityForResult(Intent(this, TaxiInvoiceActivity::class.java)
                                        .putExtra("ResponseData", Gson().toJson(checkStatusResponse.responseData)), 100)
                                finish()
                            }

                            COMPLETED -> {
                                println("RRR :: inside COMPLETED = ")
                                writePreferences(CAN_SEND_LOCATION, false)
                                writePreferences(CAN_SAVE_LOCATION, false)
                                startActivityForResult(Intent(this, TaxiInvoiceActivity::class.java)
                                        .putExtra("ResponseData", Gson().toJson(checkStatusResponse.responseData)), 100)
                                finish()
                            }
                        }
                    }
                } else {
                    BROADCAST = BASE_BROADCAST
                    finish()
                    println("RRR :: inside else = ${checkStatusResponse.responseData.request.status}")
                }

                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)

            } catch (e: Exception) {
                e.printStackTrace()
                BROADCAST = BASE_BROADCAST
                finish()
            }
        })

        mViewModel.distanceApiProcessing.observe(this, Observer {
            var canShowTollChargeDialog = false
            if (it.isNotEmpty() && it.size > 0 && it.size == distanceApiCallCount) {
                println("RRR::distanceApiProcessing::inside if = ${it.size}")
                for (items in it) {
                    mViewModel.distanceMeter.value = mViewModel.distanceMeter.value!! + items.distance
                    println("RRR::distanceMeter.value ::2:: = ${mViewModel.distanceMeter.value}")
                    mViewModel.showLoading.value = false
                    canShowTollChargeDialog = true
                }
            }

            if (canShowTollChargeDialog) ViewUtils.showAlert(this, getString(R.string.toll_charge_desc),
                    getString(R.string.yes), getString(R.string.no), object : ViewUtils.ViewCallBack {
                override fun onPositiveButtonClick(dialog: DialogInterface) {
                    val tollChargeDialog = TollChargeDialog()
                    val bundle = Bundle()
                    bundle.putString("requestID", mViewModel.checkStatusTaxiLiveData.value!!.responseData.request.id.toString())
                    tollChargeDialog.arguments = bundle
                    tollChargeDialog.show(supportFragmentManager, "tollCharge")
                }

                override fun onNegativeButtonClick(dialog: DialogInterface) {
                    val params: HashMap<String, String> = HashMap()
                    params["id"] = mViewModel.checkStatusTaxiLiveData.value!!.responseData.request.id.toString()
                    params["status"] = DROPPED
                    params["_method"] = "PATCH"
                    params["toll_price"] = "0"
                    mViewModel.taxiDroppingStatus(params)
                    dialog.dismiss()
                }
            })
        })

        mViewModel.taxiCancelRequest.observe(this, Observer<CancelRequestModel> {
            finish()
            mViewModel.showLoading.value = false
            ViewUtils.showToast(this, resources.getString(R.string.request_canceled), true)
        })

        SocketManager.onEvent(Constants.RoomName.STATUS, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK transport STATUS ")
            if(it.isNotEmpty() && it[0].toString().contains(TRANSPORT)){
                roomConnected = true
            }
        })

        SocketManager.onEvent(Constants.RoomName.RIDE_REQ, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK transport request ")
            mViewModel.callTaxiCheckStatusAPI()
        })

        SocketManager.setOnSocketRefreshListener(object : SocketListener.ConnectionRefreshCallBack {
            override fun onRefresh() {
                SocketManager.emit(Constants.RoomName.TRANSPORT_ROOM_NAME, Constants.RoomId.getTransportRoom(reqID))
            }
        })
    }

    override fun reasonForCancel(reason: String) {
        if (reason.isNotEmpty()) {
            val params = java.util.HashMap<String, String>()
            params[Constants.Common.ID] = mViewModel.checkStatusTaxiLiveData
                    .value!!.responseData.request.id.toString()
            params[Constants.Common.ADMIN_SERVICE] = "TRANSPORT"
            params[Constants.XUberProvider.REASON] = reason
            mViewModel.cancelRequest(params)
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BROADCAST))
        mViewModel.callTaxiCheckStatusAPI()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    private fun whenStatusStarted(responseData: ResponseData) {
        writePreferences(CAN_SEND_LOCATION, true)
        writePreferences(CAN_SAVE_LOCATION, false)
        setWaitingTime()
        btn_cancel.visibility = View.VISIBLE
        btn_arrived.visibility = View.VISIBLE
        btn_picked_up.visibility = View.GONE
        llWaitingTimeContainer.visibility = View.GONE

        Glide.with(this)
                .applyDefaultRequestOptions(RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .error(R.drawable.ic_user_place_holder))
                .load(responseData.request.user.picture)
                .into(civProfile)

        tv_user_name.text = responseData.request.user.first_name + " " + responseData.request.user.last_name
        tv_user_address_one.text = responseData.request.s_address
        rate.rating = responseData.request.user.rating.toFloat()

        if (responseData.request.s_address.length > 2)
            tv_user_address_one.text = responseData.request.s_address
        else {
            val lat = responseData.request.s_latitude
            val lon = responseData.request.s_longitude
            val latLng: com.google.maps.model.LatLng?
            latLng = com.google.maps.model.LatLng(lat!!, lon!!)
            val address = getCurrentAddress(this, latLng)
            if (address.isNotEmpty()) tv_user_address_one.text = address[0].getAddressLine(0)
        }

        btn_arrived.setOnClickListener {
            val params: HashMap<String, String> = HashMap()
            params["id"] = responseData.request.id.toString()
            params["status"] = ARRIVED
            params["_method"] = "PATCH"
            mViewModel.taxiStatusUpdate(params)
        }

        drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!),
                LatLng(responseData.request.s_latitude!!, responseData.request.s_longitude!!))
    }

    private fun whenStatusArrived(responseData: ResponseData) {
        writePreferences(CAN_SEND_LOCATION, true)
        writePreferences(CAN_SAVE_LOCATION, false)
        ib_location_pin.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)
        btn_cancel.visibility = View.VISIBLE
        btn_arrived.visibility = View.GONE
        btn_picked_up.visibility = View.VISIBLE
        llWaitingTimeContainer.visibility = View.GONE

        Glide.with(this)
                .applyDefaultRequestOptions(RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .error(R.drawable.ic_user_place_holder))
                .load(responseData.request.user.picture)
                .into(civProfile)

        tv_user_name.text = responseData.request.user.first_name + " " + responseData.request.user.last_name
        tv_user_address_one.text = responseData.request.s_address
        rate.rating = responseData.request.user.rating.toFloat()

        if (responseData.request.s_address.length > 2)
            tv_user_address_one.text = responseData.request.s_address
        else {
            val lat = responseData.request.s_latitude
            val lon = responseData.request.s_longitude
            val latLng: com.google.maps.model.LatLng?
            latLng = com.google.maps.model.LatLng(lat!!, lon!!)
            val address = getCurrentAddress(this, latLng)
            if (address.isNotEmpty()) tv_user_address_one.text = address[0].getAddressLine(0)
        }

        btn_picked_up.setOnClickListener {
            if (BaseApplication.getCustomPreference!!.getBoolean(PreferencesKey.RIDE_OTP, false)) {
                if (!isWaitingTime) {
                    val otpDialogFragment = VerifyOtpDialog.newInstance(
                            responseData.request.otp,
                            responseData.request.id
                    )
                    otpDialogFragment.show(supportFragmentManager, "VerifyOtpDialog")
                } else ViewUtils.showToast(this, getString(R.string.waiting_timer_running), false)
            } else {
                val params: HashMap<String, String> = HashMap()
                params["id"] = responseData.request.id.toString()
                params["status"] = PICKED_UP
                params["_method"] = "PATCH"
                mViewModel.taxiStatusUpdate(params)
            }
        }

        drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!),
                LatLng(responseData.request.s_latitude!!, responseData.request.s_longitude!!))
    }

    private fun whenStatusPickedUp(responseData: ResponseData) {
        writePreferences(CAN_SEND_LOCATION, true)
        writePreferences(CAN_SAVE_LOCATION, true)
            setWaitingTime()
        llWaitingTimeContainer.visibility = View.VISIBLE

        ib_location_pin.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)
        ib_steering.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)
        ib_location_pin.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)

        btn_cancel.visibility = View.GONE
        btn_arrived.visibility = View.GONE
        btn_picked_up.visibility = View.GONE
        btn_drop.visibility = View.VISIBLE
        tv_pickup_location.text = getText(R.string.taxi_drop_location)
        vl_trip_started.visibility = View.VISIBLE

        Glide
                .with(this)
                .applyDefaultRequestOptions(RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .error(R.drawable.ic_user_place_holder))
                .load(responseData.request.user.picture)
                .into(civProfile)

        tv_user_name.text = responseData.request.user.first_name + " " + responseData.request.user.last_name
        tv_user_address_one.text = responseData.request.d_address
        rate.rating = responseData.request.user.rating.toFloat()

        if (responseData.request.d_address.length > 2)
            tv_user_address_one.text = responseData.request.d_address
        else {
            val lat = responseData.request.d_latitude
            val lon = responseData.request.d_longitude
            val latLng: com.google.maps.model.LatLng?
            latLng = com.google.maps.model.LatLng(lat!!, lon!!)
            val address = getCurrentAddress(this, latLng)
            if (address.isNotEmpty()) tv_user_address_one.text = address[0].getAddressLine(0)
        }

        btn_drop.setOnClickListener {
            mViewModel.distanceMeter.value = 0.0
            if (isWaitingTime!!) ViewUtils.showToast(this, getString(R.string.waiting_timer_running), false)
            else {
                mViewModel.distanceApiProcessing.value = arrayListOf()
                points.clear()
                tempPoints.clear()
                mViewModel.iteratePointsForApi.clear()
                iteratePointsForDistanceCalc.clear()

                points = AppDatabase.getAppDataBase(this)!!.locationPointsDao().getAllPoints()
                        as ArrayList<LocationPointsEntity>

                if (points.size > 2) {
                    for (point in points) {
                        val latLng = LatLng(point.lat, point.lng)
                        if (latLng.latitude != 0.0 && latLng.longitude != 0.0) tempPoints.add(latLng)
                    }
                    if (tempPoints.size > 2) locationProcessing(tempPoints)
                } else ViewUtils.showAlert(this, getString(R.string.toll_charge_desc),
                        getString(R.string.yes), getString(R.string.no), object : ViewUtils.ViewCallBack {
                    override fun onPositiveButtonClick(dialog: DialogInterface) {
                        val tollChargeDialog = TollChargeDialog()
                        val bundle = Bundle()
                        bundle.putString("requestID", mViewModel.checkStatusTaxiLiveData.value!!.responseData.request.id.toString())
                        tollChargeDialog.arguments = bundle
                        tollChargeDialog.show(supportFragmentManager, "tollCharge")
                    }

                    override fun onNegativeButtonClick(dialog: DialogInterface) {
                        val params: HashMap<String, String> = HashMap()
                        params["id"] = mViewModel.checkStatusTaxiLiveData.value!!.responseData.request.id.toString()
                        params["status"] = DROPPED
                        params["_method"] = "PATCH"
                        params["toll_price"] = "0"
                        mViewModel.taxiDroppingStatus(params)
                        dialog.dismiss()
                    }
                })
            }
        }

        drawRoute(LatLng(responseData.request.s_latitude!!, responseData.request.s_longitude!!),
                LatLng(responseData.request.d_latitude!!, responseData.request.d_longitude!!))
    }

    private fun getCurrentAddress(context: Context, currentLocation: com.google.maps.model.LatLng): List<Address> {
        var addresses: List<Address> = java.util.ArrayList()
        val geoCoder: Geocoder
        try {
            if (Geocoder.isPresent()) {
                geoCoder = Geocoder(context, Locale.getDefault())
                addresses = geoCoder.getFromLocation(currentLocation.lat, currentLocation.lng, 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addresses
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            if (location != null) {
                val isGpsEnabled = intent.getBooleanExtra("ISGPS_EXITS", false)
                if (isGpsEnabled) updateMap(location)
                else if (!isLocationDialogShown) {
                    isLocationDialogShown = true
                    CommonMethods.checkGps(context)
                }
            }
        }
    }

    //Here Pattern matcher is used for to validate whether the location values are zero or not
    private fun updateMap(location: Location) {
        val latitudeMatcher = patternMatcher!!.matcher(mViewModel.latitude.value!!.toString())
        val longtitudeMatcher = patternMatcher!!.matcher(mViewModel.longitude.value!!.toString())
        val engLatitudeMatcher = patternMatcher!!.matcher(endLatLng.latitude.toString())
        //Pattern Matcher true means the all values are   zero in lat or lon
        if (latitudeMatcher.matches() == true && longtitudeMatcher.matches() == true)
            updateCurrentLocation()
        else{
            mViewModel.latitude.value=location.latitude
            mViewModel.longitude.value=location.longitude
        }
        println("RRRR :: TaxiDashboardActivity " + mViewModel.latitude.value + " :: " + mViewModel.longitude.value)

        if (roomConnected) {
            val locationObj = JSONObject()
            locationObj.put("latitude", location.latitude)
            locationObj.put("longitude", location.longitude)
            locationObj.put("room", Constants.RoomId.getTransportRoom(reqID))
            SocketManager.emit("send_location", locationObj)
            Log.e("SOCKET", "SOCKET_SK Location update called")
        }

        val startingLatitudeMatcher=patternMatcher!!.matcher(mViewModel.latitude.value.toString())
        val startLongitudeMatcher=patternMatcher!!.matcher(mViewModel.longitude.value.toString())

        if (latitudeMatcher.matches()==false && longtitudeMatcher.matches()==false) {
            startLatLng = tempCurrentLatLon!!
            tempCurrentLatLon = LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!)
            endLatLng = LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!)
        }

        if (latitudeMatcher.matches() == false && longtitudeMatcher.matches() == false && polyLine.size > 0) {
            try {
                CarMarkerAnimUtil().carAnimWithBearing(srcMarker!!, startLatLng, endLatLng)
                polyLineRerouting(startLatLng, polyLine)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else if (latitudeMatcher.matches() == false && longtitudeMatcher.matches()==false&& polyLine.size == 0) try {
            drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!), mViewModel.polyLineDest.value!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun polyLineRerouting(point: LatLng, polyLine: ArrayList<LatLng>) {
        println("----->     RRR ~.polyLineRerouting     <-----")
        println("RRR containsLocation = " + polyUtil.containsLocation(point, polyLine, true))
        println("RRR isLocationOnEdge = " + polyUtil.isLocationOnEdge(point, polyLine, true, 50.0))
        println("RRR locationIndexOnPath = " + polyUtil.locationIndexOnPath(point, polyLine, true, 50.0))
        println("RRR locationIndexOnEdgeOrPath = " + polyUtil.locationIndexOnEdgeOrPath
        (point, polyLine, false, true, 50.0))

        val index = polyUtil.locationIndexOnEdgeOrPath(point, polyLine, false, true, 50.0)

        if (index > 0) {
            polyLine.subList(0, index + 2).clear()
//            polyLine.add(0, point)
            mPolyline!!.remove()
            val options = PolylineOptions()
            options.addAll(polyLine)
            mPolyline = mGoogleMap!!.addPolyline(options.width(5f).color
            (ContextCompat.getColor(baseContext, R.color.colorBlack)))
        } else if (index < 0) {
            canDrawPolyLine = true
            drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!),
                    mViewModel.polyLineDest.value!!)
        }
    }

    private fun drawRoute(src: LatLng, dest: LatLng) {
        mViewModel.tempSrc.value = src
        mViewModel.tempDest.value = dest
        if (canDrawPolyLine) {
            canDrawPolyLine = false
            Handler().postDelayed({ canDrawPolyLine = true }, 10000)
            PolylineUtil(this).execute(DirectionUtils().getDirectionsUrl
            (src, dest, polylineKey))
        }
        mViewModel.polyLineSrc.value = src
        mViewModel.polyLineDest.value = dest
    }

    override fun whenDone(output: DistanceCalcModel) {
        println("RRR :: TaxiDashboardActivity.whenDone")
        val distanceProcessing = DistanceApiProcessing()
        distanceProcessing.id = distanceApiCallCount
        distanceProcessing.apiResponseStatus = "success"

        val values = mViewModel.distanceApiProcessing.value!!
        for (leg in output.routes[0].legs) {
            distanceProcessing.distance = distanceProcessing.distance + leg.distance.value
            println("distanceProcessing.distance  = ${leg.distance.value}")
            println("distanceProcessing.distance  = ${distanceProcessing.distance}")
        }

        values.add(distanceProcessing)
        /*startActivity(Intent(this, MainActivity::class.java)
                .putExtra("ResponseData", Gson().toJson(output)))*/

        mViewModel.distanceApiProcessing.postValue(values)
    }

    override fun whenDone(output: PolylineOptions) {
        try {
            mGoogleMap!!.clear()

            mPolyline = mGoogleMap!!.addPolyline(output.width(5f).color
            (ContextCompat.getColor(baseContext, R.color.colorBlack)))

            polyLine = output.points as ArrayList<LatLng>

            val builder = LatLngBounds.Builder()

            for (latLng in polyLine) builder.include(latLng)

            mGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))

            srcMarker = mGoogleMap!!.addMarker(MarkerOptions().position(polyLine[0]).icon
            (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.iv_marker_car))))

//            CarMarkerAnimUtil().carAnimWithBearing(srcMarker!!, polyLine[0], polyLine[1])
            srcMarker!!.rotation = CarMarkerAnimUtil().bearingBetweenLocations(polyLine[0], polyLine[1]).toFloat()

            mGoogleMap!!.addMarker(MarkerOptions().position(polyLine[polyLine.size - 1]).icon
            (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.ic_marker_stop))))

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun getDistanceTime(meters: Double, seconds: Double) {
        runOnUiThread {
            mViewModel.driverSpeed.value = meters / seconds
            println("RRR :::: speed = ${mViewModel.driverSpeed.value}")
        }
    }

    override fun whenDirectionFail(statusCode: String) {
        println("RRR::TaxiDashboardActivity.whenDirectionFail")
        val distanceProcessing = DistanceApiProcessing()
        distanceProcessing.id = distanceApiCallCount
        distanceProcessing.apiResponseStatus = statusCode + "directionApiFailed"
        distanceProcessing.distance = 0.0

        val values: ArrayList<DistanceApiProcessing> = arrayListOf()
        values.add(distanceProcessing)
        mViewModel.distanceApiProcessing.postValue(values)

        println("RRR whenDirectionFail = $statusCode")
        when (statusCode) {
            "NOT_FOUND" -> showLog(getString(R.string.NoRoadMapAvailable))
            "ZERO_RESULTS" -> showLog(getString(R.string.NoRoadMapAvailable))
            "MAX_WAYPOINTS_EXCEEDED" -> showLog(getString(R.string.WayPointLlimitExceeded))
            "MAX_ROUTE_LENGTH_EXCEEDED" -> showLog(getString(R.string.RoadMapLimitExceeded))
            "INVALID_REQUEST" -> showLog(getString(R.string.InvalidInputs))
            "OVER_DAILY_LIMIT" -> showLog(getString(R.string.MayBeInvalidAPIBillingPendingMethodDeprecated))
            "OVER_QUERY_LIMIT" -> showLog(getString(R.string.TooManyRequestlimitExceeded))
            "REQUEST_DENIED" -> showLog(getString(R.string.DirectionsServiceNotEnabled))
            "UNKNOWN_ERROR" -> showLog(getString(R.string.ServerError))
            else -> showLog(statusCode)
        }
    }

    override fun whenFail(statusCode: String) {
        if (statusCode == "OVER_DAILY_LIMIT" || statusCode.contains(
                        "You have exceeded your daily request quota for this API")) {
            polylineKey = BaseApplication.getCustomPreference!!.getString(
                    PreferencesKey.ALTERNATE_MAP_KEY, "")
            PolylineUtil(this).execute(DirectionUtils().getDirectionsUrl
            (mViewModel.tempSrc.value, mViewModel.tempDest.value, polylineKey))
        }

        println("RRR whenFail = $statusCode")
        when (statusCode) {
            "NOT_FOUND" -> showLog(getString(R.string.NoRoadMapAvailable))
            "ZERO_RESULTS" -> showLog(getString(R.string.NoRoadMapAvailable))
            "MAX_WAYPOINTS_EXCEEDED" -> showLog(getString(R.string.WayPointLlimitExceeded))
            "MAX_ROUTE_LENGTH_EXCEEDED" -> showLog(getString(R.string.RoadMapLimitExceeded))
            "INVALID_REQUEST" -> showLog(getString(R.string.InvalidInputs))
            "OVER_DAILY_LIMIT" -> showLog(getString(R.string.MayBeInvalidAPIBillingPendingMethodDeprecated))
            "OVER_QUERY_LIMIT" -> showLog(getString(R.string.TooManyRequestlimitExceeded))
            "REQUEST_DENIED" -> showLog(getString(R.string.DirectionsServiceNotEnabled))
            "UNKNOWN_ERROR" -> showLog(getString(R.string.ServerError))
            else -> showLog(statusCode)
        }
    }

    private fun showLog(msg: String) = ViewUtils.showNormalToast(this, msg)

    private fun bitmapFromVector(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)

        return bitmap
    }

    private fun openGoogleNavigation() {
        if (mViewModel.polyLineSrc.value != null && mViewModel.polyLineDest.value != null)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                    "http://maps.google.com/maps?saddr=" +
                            "${mViewModel.polyLineSrc.value!!.latitude}," +
                            "${mViewModel.polyLineSrc.value!!.longitude}" +
                            "&daddr=" +
                            "${mViewModel.polyLineDest.value!!.latitude}," +
                            "${mViewModel.polyLineDest.value!!.longitude}")))
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        val time = SystemClock.elapsedRealtime() - chronometer!!.base
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
        val t = (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) +
                ":" + if (s < 10) "0$s" else s
        chronometer.text = t
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnWaiting -> if (isWaitingTime) {
                changeWaitingTimeBackground(false)
                isWaitingTime = false
                lastWaitingTime = SystemClock.elapsedRealtime()
                val requestID = mViewModel.checkStatusTaxiLiveData.value!!.responseData.request.id.toString()
                val params = HashMap<String, String>()
                params[Constants.Common.ID] = requestID
                params["status"] = "0"
                mViewModel.taxiWaitingTime(params)
                cmWaiting.stop()
            } else {
                changeWaitingTimeBackground(true)
                isWaitingTime = true
                val temp: Long = 0

                if (lastWaitingTime != temp)
                    cmWaiting.base = (cmWaiting.base + SystemClock.elapsedRealtime()) - lastWaitingTime!!
                else
                    cmWaiting.base = SystemClock.elapsedRealtime()

                if (mViewModel.checkStatusTaxiLiveData.value != null) {
                    val requestID = mViewModel.checkStatusTaxiLiveData.value!!.responseData.request.id.toString()
                    val params = HashMap<String, String>()
                    params[Constants.Common.ID] = requestID
                    params["status"] = "1"
                    mViewModel.taxiWaitingTime(params)
                }
                cmWaiting.start()
            }
        }
    }

    private fun setWaitingTime() {
        val time = mViewModel.checkStatusTaxiLiveData.value!!.responseData.waitingTime
        if (isNeedToUpdateWaiting && time > 0) {
            isNeedToUpdateWaiting = false
            cmWaiting.base = SystemClock.elapsedRealtime() - (time * 1000)
            val h = ((time * 1000) / 3600000).toInt()
            val m = ((time * 1000) - h * 3600000).toInt() / 60000
            val s = ((time * 1000) - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000

            val formattedTime = (if (h < 10) "0$h" else h).toString() + ":" +
                    (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            cmWaiting.text = formattedTime
            if (mViewModel.checkStatusTaxiLiveData.value!!.responseData.waitingStatus == 1) {
                cmWaiting.start()
                isWaitingTime = true
                changeWaitingTimeBackground(true)
            }
        } else if (isWaitingTime == true) {
            //      cmWaiting.base = SystemClock.elapsedRealtime() - (time * 1000)
            changeWaitingTimeBackground(true)
        } else changeWaitingTimeBackground(false)
    }

    private fun changeWaitingTimeBackground(isWaitingTime: Boolean) {
        if (isWaitingTime) {
            btnWaiting.backgroundTintList = ContextCompat.getColorStateList(this, R.color.taxi_bg_yellow)
            btnWaiting.setTextColor(ContextCompat.getColor(this, R.color.white))
        } else {
            btnWaiting.backgroundTintList = ContextCompat.getColorStateList(this, R.color.white)
            btnWaiting.setTextColor(ContextCompat.getColor(this, R.color.black))
        }
    }

    override fun onBackPressed() {
        if (doubleBackToExit) {
            finishAffinity()
            return
        }

        doubleBackToExit = true
        ViewUtils.showToast(this, getString(R.string.click_back_exit), true)
        Handler().postDelayed({ doubleBackToExit = false }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            500 -> when (resultCode) {
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
            }
            100 -> finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun locationProcessing(latLng: ArrayList<LatLng>) {
        println("GGGG :: locationProcessing = " + latLng.size)

        mViewModel.iteratePointsForApi.add(latLng[0])
        for (i in latLng.indices) if (i < latLng.size - 1)
            iteratePointsApi(latLng[i], latLng[i + 1])

        mViewModel.iteratePointsForApi.add(latLng[latLng.size - 1])
        longLog(Gson().toJson(mViewModel.iteratePointsForApi), "BBB")
        println("GGGG :: locationProcessing::iteratePointsApi = " + mViewModel.iteratePointsForApi.size)

        iteratePointsForDistanceCalc.add(latLng[0])
        for (i in mViewModel.iteratePointsForApi.indices) if (i < mViewModel.iteratePointsForApi.size - 1)
            iteratePointsForDistanceCal(mViewModel.iteratePointsForApi[i], mViewModel.iteratePointsForApi[i + 1])

        iteratePointsForDistanceCalc.add(latLng[latLng.size - 1])
        longLog(Gson().toJson(iteratePointsForDistanceCalc), "CCC")
        println("GGGG :: locationProcessing::iteratePointsForDistanceCalc = " + iteratePointsForDistanceCalc.size)

        try {
            distanceApiCallCount = 0
            val key = getString(R.string.google_map_key)
            googleApiCall(mViewModel.iteratePointsForApi, key)
//            googleApiCall(iteratePointsForDistanceCalc)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun iteratePointsApi(s: LatLng, e: LatLng) {
        var dist = distBt(s, e)
        if (dist >= iteratePointsForApi) {
            mViewModel.iteratePointsForApi.add(e)
            tempPoint = null
        } else if (tempPoint != null) {
            dist = distBt(tempPoint!!, e)
            if (dist >= iteratePointsForApi) {
                mViewModel.iteratePointsForApi.add(e)
                tempPoint = null
            }
        } else tempPoint = s
    }

    private fun iteratePointsForDistanceCal(s: LatLng, e: LatLng) {
        var dist = distBt(s, e)
        if (dist >= iteratePointsForDistanceCal) {
            iteratePointsForDistanceCalc.add(e)
            tempPointForDistanceCal = null
        } else if (tempPointForDistanceCal != null) {
            dist = distBt(tempPointForDistanceCal!!, e)
            if (dist >= iteratePointsForDistanceCal) {
                iteratePointsForDistanceCalc.add(e)
                tempPointForDistanceCal = null
            }
        } else tempPointForDistanceCal = s
    }

    private fun distBt(a: LatLng, b: LatLng): Double {
        val startPoint = Location("start")
        startPoint.latitude = a.latitude
        startPoint.longitude = a.longitude

        val endPoint = Location("end")
        endPoint.latitude = b.latitude
        endPoint.longitude = b.longitude
        return startPoint.distanceTo(endPoint).toDouble()
    }

    private fun googleApiCall(list: MutableList<LatLng>, key: String) {
        mViewModel.showLoading.value = true
        distanceApiCallCount++
        if (list.size > 25) {
            DistanceProcessing(this).execute(DistanceUtils().getUrl(list.subList(0, 24), key))
            googleApiCall(list.subList(25, list.size - 1), key)
        } else DistanceProcessing(this).execute(DistanceUtils().getUrl(list, key))
    }

    override fun showCurrentLocation() = Dexter.withActivity(this)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    updateCurrentLocation()
                }

                override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()
}