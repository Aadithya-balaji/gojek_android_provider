package com.xjek.taxiservice.views.main

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
import android.widget.Chronometer
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.Constants
import com.xjek.base.data.Constants.BroadCastTypes.BASE_BROADCAST
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
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.data.PreferencesKey.CAN_SAVE_LOCATION
import com.xjek.base.data.PreferencesKey.CURRENT_TRANXIT_STATUS
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.writePreferences
import com.xjek.base.location_service.BaseLocationService
import com.xjek.base.location_service.BaseLocationService.Companion.BROADCAST
import com.xjek.base.socket.SocketListener
import com.xjek.base.socket.SocketManager
import com.xjek.base.utils.*
import com.xjek.base.utils.polyline.DirectionUtils
import com.xjek.base.utils.polyline.PolyLineListener
import com.xjek.base.utils.polyline.PolylineUtil
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.ActivityTaxiMainBinding
import com.xjek.taxiservice.model.ResponseData
import com.xjek.taxiservice.views.invoice.TaxiInvoiceActivity
import com.xjek.taxiservice.views.tollcharge.TollChargeDialog
import com.xjek.taxiservice.views.verifyotp.VerifyOtpDialog
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.layout_status_indicators.*
import kotlinx.android.synthetic.main.layout_taxi_status_container.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class TaxiDashboardActivity : BaseActivity<ActivityTaxiMainBinding>(),
        TaxiDashboardNavigator,
        OnMapReadyCallback,
        PolyLineListener,
        Chronometer.OnChronometerTickListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        View.OnClickListener {

    private lateinit var activityTaxiMainBinding: ActivityTaxiMainBinding
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mViewModel: TaxiDashboardViewModel
    private lateinit var sheetBehavior: BottomSheetBehavior<LinearLayout>

    private var isWaitingTime: Boolean? = false
    private var lastWaitingTime: Long? = 0
    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null
    private var canDrawPolyLine: Boolean = true
    private var mPolyline: Polyline? = null
    private var isNeedToUpdateWaiting: Boolean = false

    private var startLatLng = LatLng(0.0, 0.0)
    private var endLatLng = LatLng(0.0, 0.0)
    private var srcMarker: Marker? = null
    private var polyUtil = PolyUtil()

    private var polyLine: ArrayList<LatLng> = ArrayList()
    private var checkStatusApiCounter = 0

    private var roomConnected: Boolean = false

    override fun getLayoutId(): Int = R.layout.activity_taxi_main

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        this.activityTaxiMainBinding = mViewDataBinding as ActivityTaxiMainBinding
        mViewModel = ViewModelProviders.of(this).get(TaxiDashboardViewModel::class.java)
        mViewModel.navigator = this
        activityTaxiMainBinding.lifecycleOwner = this
        activityTaxiMainBinding.taximainmodule = mViewModel
        mViewModel.currentStatus.value = ""
        sheetBehavior = BottomSheetBehavior.from<LinearLayout>(bsContainer)
        sheetBehavior.peekHeight = 550
        btnWaiting.setOnClickListener(this)
        cmWaiting.onChronometerTickListener = this
        if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>

        ibNavigation.setOnClickListener {
            openGoogleNavigation()
        }

        tvSos.setOnClickListener {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:911")))
        }

        btn_cancel.setOnClickListener {

        }

        initializeMap()

//        if (true) {
//              Guindy Location :: 12.998219, 80.205836
//              Tranxit         :: 13.058687, 80.253300
//              drawRoute(LatLng(12.998219, 80.205836), LatLng(13.058687, 80.253300))
//        } else
        checkStatusAPIResponse()
        isNeedToUpdateWaiting = true
    }

    private fun initializeMap() {
        fragmentMap = supportFragmentManager.findFragmentById(R.id.taxi_map_fragment) as SupportMapFragment
        fragmentMap.getMapAsync(this)
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
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onCameraIdle() {
        sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
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

        mViewModel.checkStatusTaxiLiveData.observe(this, Observer { checkStatusResponse ->
            run {
                if (checkStatusResponse?.statusCode.equals("200")) try {
                    mViewModel.showLoading.value = false
                    if (checkStatusResponse.responseData.request.status.isNotEmpty()) {
                        println("RRR :: Status = ${checkStatusResponse.responseData.request.status}")
                        //if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                        if (mViewModel.currentStatus.value != checkStatusResponse.responseData.request.status) {
                            writePreferences(PreferencesKey.FIRE_BASE_PROVIDER_IDENTITY, checkStatusResponse.responseData.provider_details.id)
                            mViewModel.currentStatus.value = checkStatusResponse.responseData.request.status
                            writePreferences(CURRENT_TRANXIT_STATUS, mViewModel.currentStatus.value)

                            val requestID = checkStatusResponse.responseData.request.id.toString()

                            if (!roomConnected) {
                                roomConnected = true
                                val reqID = checkStatusResponse.responseData.request.id
                                PreferencesHelper.put(PreferencesKey.REQ_ID, reqID)
                                SocketManager.emit(Constants.ROOM_NAME.TRANSPORT_ROOM_NAME, Constants.ROOM_ID.TRANSPORT_ROOM)
                            }

                            when (checkStatusResponse.responseData.request.status) {
                                SEARCHING -> {
                                    val params = HashMap<String, String>()
                                    params[com.xjek.base.data.Constants.Common.ID] = requestID
                                    mViewModel.taxiWaitingTime(params)
                                }

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
                                    writePreferences(CAN_SAVE_LOCATION, true)
                                }

                                ARRIVED -> {
                                    println("RRR :: inside ARRIVED = ")
                                    whenStatusArrived(checkStatusResponse.responseData)
                                    writePreferences(CAN_SAVE_LOCATION, true)
                                }

                                PICKED_UP -> {
                                    println("RRR :: inside PICKED_UP = ")
                                    whenStatusPickedUp(checkStatusResponse.responseData)
                                    writePreferences(CAN_SAVE_LOCATION, true)
                                }

                                DROPPED -> {
                                    writePreferences(CAN_SAVE_LOCATION, false)
                                    println("RRR :: inside DROPPED = ")
                                    val strCheckRequestModel = Gson().toJson(checkStatusResponse.responseData)
                                    startActivityForResult(Intent(this, TaxiInvoiceActivity::class.java)
                                            .putExtra("ResponseData", strCheckRequestModel), 100)
                                    finish()
                                }

                                COMPLETED -> {
                                    writePreferences(CAN_SAVE_LOCATION, false)
                                    println("RRR :: inside COMPLETED = ")
                                    val strCheckRequestModel = Gson().toJson(checkStatusResponse.responseData)
                                    startActivityForResult(Intent(this, TaxiInvoiceActivity::class.java)
                                            .putExtra("ResponseData", strCheckRequestModel), 100)
                                    finish()
                                }
                            }
                        }
                    } else {
                        BROADCAST = BASE_BROADCAST
                        finish()
                        println("RRR :: inside else = ${checkStatusResponse.responseData.request.status}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        observeLiveData(mViewModel.waitingTimeLiveData) {
            if (mViewModel.waitingTimeLiveData.value != null) {
                val waitingTime = mViewModel.waitingTimeLiveData.value!!.waitingStatus
                if (waitingTime == 1) {

                } else {

                }
            }
        }

        SocketManager.onEvent(Constants.ROOM_NAME.RIDE_REQ, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK transport request " + it[0])
            mViewModel.callTaxiCheckStatusAPI()
        })

        SocketManager.setOnSocketRefreshListener(object : SocketListener.connectionRefreshCallBack {
            override fun onRefresh() {
                if (roomConnected) {
                    roomConnected = false
                    SocketManager.emit(Constants.ROOM_NAME.TRANSPORT_ROOM_NAME, Constants.ROOM_ID.TRANSPORT_ROOM)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BROADCAST))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    private fun whenStatusStarted(responseData: ResponseData) {
        setWaitingTime()
        btn_cancel.visibility = View.VISIBLE
        btn_arrived.visibility = View.VISIBLE
        btn_picked_up.visibility = View.GONE
        llWaitingTimeContainer.visibility = View.VISIBLE

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

        if (responseData.request.s_address.length > 2)
            tv_user_address_one.text = responseData.request.s_address
        else {
            val lat = responseData.request.s_latitude
            val lon = responseData.request.s_longitude
            val latLng: com.google.maps.model.LatLng?
            latLng = com.google.maps.model.LatLng(lat, lon)
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
                LatLng(responseData.request.s_latitude, responseData.request.s_longitude))
    }

    private fun whenStatusArrived(responseData: ResponseData) {
        ib_location_pin.background = ContextCompat.getDrawable(this, R.drawable.bg_status_complete)
        btn_cancel.visibility = View.VISIBLE
        btn_arrived.visibility = View.GONE
        btn_picked_up.visibility = View.VISIBLE
        llWaitingTimeContainer.visibility = View.VISIBLE

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

        if (responseData.request.s_address.length > 2)
            tv_user_address_one.text = responseData.request.s_address
        else {
            val lat = responseData.request.s_latitude
            val lon = responseData.request.s_longitude
            val latLng: com.google.maps.model.LatLng?
            latLng = com.google.maps.model.LatLng(lat, lon)
            val address = getCurrentAddress(this, latLng)
            if (address.isNotEmpty()) tv_user_address_one.text = address[0].getAddressLine(0)
        }

        btn_picked_up.setOnClickListener {
            val otpDialogFragment = VerifyOtpDialog.newInstance(
                    responseData.request.otp,
                    responseData.request.id
            )
            otpDialogFragment.show(supportFragmentManager, "VerifyOtpDialog")
        }

        drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!),
                LatLng(responseData.request.s_latitude, responseData.request.s_longitude))
    }

    private fun whenStatusPickedUp(responseData: ResponseData) {
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
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder))
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
            latLng = com.google.maps.model.LatLng(lat, lon)
            val address = getCurrentAddress(this, latLng)
            if (address.isNotEmpty()) tv_user_address_one.text = address[0].getAddressLine(0)
        }

        btn_drop.setOnClickListener {
            //            val params: HashMap<String, String> = HashMap()
//            params["id"] = responseData.request.id.toString()
//            params["status"] = DROPPED
//            params["_method"] = "PATCH"
//            mViewModel.taxiStatusUpdate(params)

            ViewUtils.showAlert(this, "Do you have any Toll charge", object : ViewUtils.ViewCallBack {
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

        drawRoute(LatLng(responseData.request.s_latitude, responseData.request.s_longitude),
                LatLng(responseData.request.d_latitude, responseData.request.d_longitude))
    }

    private fun getCurrentAddress(context: Context, currentLocation: com.google.maps.model.LatLng): List<Address> {
        var addresses: List<Address> = java.util.ArrayList()
        val geocoder: Geocoder
        try {
            if (Geocoder.isPresent()) {
                geocoder = Geocoder(context, Locale.getDefault())
                addresses = geocoder.getFromLocation(currentLocation.lat, currentLocation.lng, 1)
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            }
        } catch (e: Exception) {
            Log.d("EXception", "EXception" + e.message)
        }

        return addresses
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRRR:: TaxiDashboardActivity")
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            if (location != null) {
                mViewModel.latitude.value = location.latitude
                mViewModel.longitude.value = location.longitude

                if (checkStatusApiCounter++ % 2 == 0) mViewModel.callTaxiCheckStatusAPI()

                if (startLatLng.latitude > 0) endLatLng = startLatLng
                startLatLng = LatLng(location.latitude, location.longitude)

                if (endLatLng.latitude > 0 && polyLine.size > 0) try {
                    CarMarkerAnimUtil().carAnim(srcMarker!!, endLatLng, startLatLng)
                    polyLineRerouting(endLatLng, polyLine)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

//                updateMapLocation(LatLng(location.latitude, location.longitude))
//                PY 01 K 3875
            }
        }
    }

    private fun polyLineRerouting(point: LatLng, polyLine: ArrayList<LatLng>) {
        println("----->     RRR TaxiDashBoardActivity.polyLineRerouting     <-----")
        println("RRR containsLocation = " + polyUtil.containsLocation(point, polyLine, true))
        println("RRR isLocationOnEdge = " + polyUtil.isLocationOnEdge(point, polyLine, true, 10.0))
        println("RRR locationIndexOnPath = " + polyUtil.locationIndexOnPath(point, polyLine, true, 10.0))
        println("RRR locationIndexOnEdgeOrPath = " + polyUtil.locationIndexOnEdgeOrPath(point, polyLine, false, true, 10.0))

        val index = polyUtil.locationIndexOnEdgeOrPath(point, polyLine, false, true, 10.0)
        if (index >= 0) {
            polyLine.subList(0, index + 2).clear()
            polyLine.add(0, point)
            mPolyline!!.remove()
            val options = PolylineOptions()
            options.addAll(polyLine)
            mPolyline = mGoogleMap!!.addPolyline(options.width(5f).color
            (ContextCompat.getColor(baseContext, R.color.taxi_bg_yellow)))
            println("RRR mPolyline = " + polyLine.size)
        } else {
            canDrawPolyLine = true
            drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!),
                    mViewModel.polyLineDest.value!!)
        }
    }

    private fun drawRoute(src: LatLng, dest: LatLng) {
        if (canDrawPolyLine) {
            canDrawPolyLine = false
            Handler().postDelayed({ canDrawPolyLine = true }, 10000)
            PolylineUtil(this).execute(DirectionUtils().getDirectionsUrl(src, dest, getText(R.string.google_map_key).toString()))
        }
        mViewModel.polyLineSrc.value = src
        mViewModel.polyLineDest.value = dest
    }

    override fun whenDone(output: PolylineOptions) {
        mGoogleMap!!.clear()

        mPolyline = mGoogleMap!!.addPolyline(output.width(5f).color
        (ContextCompat.getColor(baseContext, R.color.taxi_bg_yellow)))

        polyLine = output.points as ArrayList<LatLng>

        val builder = LatLngBounds.Builder()

        for (latLng in polyLine) builder.include(latLng)

        mGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))

        srcMarker = mGoogleMap!!.addMarker(MarkerOptions().position(polyLine[0]).icon
        (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.ic_taxi_car))))

        mGoogleMap!!.addMarker(MarkerOptions().position(polyLine[polyLine.size - 1]).icon
        (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.ic_taxi_pin))))

    }

    override fun whenFail(statusCode: String) {
        println("RRR whenFail = $statusCode")
        when (statusCode) {
            "NOT_FOUND" -> Toast.makeText(this, "No road map available...", Toast.LENGTH_SHORT).show()
            "ZERO_RESULTS" -> Toast.makeText(this, "No road map available...", Toast.LENGTH_SHORT).show()
            "MAX_WAYPOINTS_EXCEEDED" -> Toast.makeText(this, "Way point limit exceeded...", Toast.LENGTH_SHORT).show()
            "MAX_ROUTE_LENGTH_EXCEEDED" -> Toast.makeText(this, "Road map limit exceeded...", Toast.LENGTH_SHORT).show()
            "INVALID_REQUEST" -> Toast.makeText(this, "Invalid inputs...", Toast.LENGTH_SHORT).show()
            "OVER_DAILY_LIMIT" -> Toast.makeText(this, "MayBe invalid API/Billing pending/Method Deprecated...", Toast.LENGTH_SHORT).show()
            "OVER_QUERY_LIMIT" -> Toast.makeText(this, "Too many request, limit exceeded...", Toast.LENGTH_SHORT).show()
            "REQUEST_DENIED" -> Toast.makeText(this, "Directions service not enabled...", Toast.LENGTH_SHORT).show()
            "UNKNOWN_ERROR" -> Toast.makeText(this, "Server Error...", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, statusCode, Toast.LENGTH_SHORT).show()
        }
    }

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
        //      Guindy Location :: 12.998219, 80.205836
        //      Tranxit         :: 13.058687, 80.253300

//        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
//                "http://maps.google.com/maps?saddr=" +
//                        "12.998219,80.205836" +
//                        "&daddr=" +
//                        "13.058687,80.253300")))

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
        val t = (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
        chronometer.text = t
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnWaiting -> {
                if (isWaitingTime == true) {
                    changeWaitingTimeBackground(false)
                    isWaitingTime = false
                    lastWaitingTime = SystemClock.elapsedRealtime()
                    val requestID = mViewModel.checkStatusTaxiLiveData.value!!.responseData.request.id.toString()
                    val params = HashMap<String, String>()
                    params[Constants.Common.ID] = requestID
                    params["status"] = "1"
                    mViewModel.taxiWaitingTime(params)
                    cmWaiting.stop()
                } else {
                    changeWaitingTimeBackground(true)
                    isWaitingTime = true
                    val temp: Long = 0
                    if (lastWaitingTime != temp)
                        cmWaiting.base = (cmWaiting.base + SystemClock.elapsedRealtime()) - lastWaitingTime!!
                    else cmWaiting.base = SystemClock.elapsedRealtime()
                    if (mViewModel.checkStatusTaxiLiveData.value != null) {
                        val requestID = mViewModel.checkStatusTaxiLiveData.value!!.responseData.request.id.toString()
                        val params = HashMap<String, String>()
                        params.put(Constants.Common.ID, requestID)
                        params.put("status", "1")
                        mViewModel.taxiWaitingTime(params)
                    }
                    cmWaiting.start()
                }
            }
        }
    }

    private fun setWaitingTime() {
        val time = mViewModel.checkStatusTaxiLiveData.value!!.responseData.waitingTime

        if (isNeedToUpdateWaiting && time > 0) {
            cmWaiting.base = SystemClock.elapsedRealtime() - (time * 1000)
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
            val formattedTime = (if (h < 10) "0$h" else h).toString() + ":" +
                    (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            cmWaiting.text = formattedTime
            if (mViewModel.checkStatusTaxiLiveData.value!!.responseData.waitingStatus == 1) cmWaiting.start()
            isWaitingTime = true
            changeWaitingTimeBackground(true)
        } else {
            isWaitingTime = false
            changeWaitingTimeBackground(false)
        }
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
        if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) finish()
    }
}