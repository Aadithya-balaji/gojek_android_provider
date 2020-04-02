package com.gox.xuberservice.xuberMainActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.*
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.Chronometer
import android.widget.PopupWindow
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.chatmessage.ChatActivity
import com.gox.base.data.Constants
import com.gox.base.data.Constants.ModuleTypes.SERVICE
import com.gox.base.data.Constants.RideStatus.ACCEPTED
import com.gox.base.data.Constants.RideStatus.ARRIVED
import com.gox.base.data.Constants.RideStatus.COMPLETED
import com.gox.base.data.Constants.RideStatus.DROPPED
import com.gox.base.data.Constants.RideStatus.PICKED_UP
import com.gox.base.data.Constants.XUberProvider.CANCEL
import com.gox.base.data.Constants.XUberProvider.START
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.writePreferences
import com.gox.base.location_service.BaseLocationService
import com.gox.base.socket.SocketListener
import com.gox.base.socket.SocketManager
import com.gox.base.utils.*
import com.gox.base.utils.polyline.DirectionUtils
import com.gox.base.utils.polyline.PolyLineListener
import com.gox.base.utils.polyline.PolylineUtil
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.ActivityXuberMainBinding
import com.gox.xuberservice.extracharge.XUberExtraChargeDialog
import com.gox.xuberservice.interfaces.GetExtraChargeInterface
import com.gox.xuberservice.interfaces.GetFilePathInterface
import com.gox.xuberservice.interfaces.GetReasonsInterface
import com.gox.xuberservice.invoice.XUberInvoiceDialog
import com.gox.xuberservice.model.CancelRequestModel
import com.gox.xuberservice.model.UpdateRequest
import com.gox.xuberservice.model.XuperCheckRequest
import com.gox.xuberservice.rating.DialogXuberRating
import com.gox.xuberservice.reasons.XUberCancelReasonFragment
import com.gox.xuberservice.uploadImage.UploadPictureDialog
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_xuber_main.*
import kotlinx.android.synthetic.main.bottom_service_status_sheet.*
import kotlinx.android.synthetic.main.bottom_service_status_sheet.view.*
import kotlinx.android.synthetic.main.dialog_info_window.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.concurrent.schedule

class XUberDashBoardActivity : BaseActivity<ActivityXuberMainBinding>(),
        XUberDashBoardNavigator,
        OnMapReadyCallback,
        Chronometer.OnChronometerTickListener,
        GetFilePathInterface,
        GetReasonsInterface,
        PolyLineListener,
        View.OnClickListener {

    private lateinit var mViewModel: XUberDashboardViewModel
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mBinding: ActivityXuberMainBinding
    private lateinit var context: Context

    private val invoicePage = XUberInvoiceDialog()
    private val ratingPage = DialogXuberRating()

    private var localServiceTime: Long? = null
    private var mGoogleMap: GoogleMap? = null
    private var frontImgMultiPart: MultipartBody.Part? = null
    private var backImgMultiPart: MultipartBody.Part? = null
    private var frontImgFile: File? = null
    private var backImgFile: File? = null
    private var canDrawPolyLine: Boolean = true
    private var isFront: Boolean = true
    private var startLatLng = LatLng(0.0, 0.0)
    private var endLatLng = LatLng(0.0, 0.0)
    private var srcMarker: Marker? = null
    private var polyUtil = PolyUtil()
    private var mPolyline: Polyline? = null
    private var polyLine: ArrayList<LatLng> = ArrayList()
    private var checkStatusApiCounter = 0
    private var popupWindow: PopupWindow? = null
    private var popupView: View? = null
    private var isGPSEnabled: Boolean = false
    private var isLocationDialogShown: Boolean = false
    private var roomConnected: Boolean = false
    private var reqID: Int = 0
    private var currentStatus: String = ""
    private var paymentMode: String = ""
    private var checkRequestTimer: Timer? = null


    override fun getLayoutId() = R.layout.activity_xuber_main

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        currentStatus = ""
        mBinding = mViewDataBinding as ActivityXuberMainBinding
        mViewModel = XUberDashboardViewModel()
        context = this
        checkRequestTimer = Timer()
        mViewModel.navigator = this
        mBinding.xuberViewModel = mViewModel
        mBinding.lifecycleOwner = this

        mViewModel.showLoading.observe(this, Observer {
            loadingObservable.value = it
        })

        mBinding.llBottomService.fbCamera.setOnClickListener(this)
        fab_xuber_menu.isIconAnimated = false
        fab_xuber_menu_chat.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        checkRequestTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mViewModel.callXUberCheckRequest()
            }
        }, 0, 5000)

        initialiseMap()
        getApiResponse()
    }

    fun getApiResponse() {
        mViewModel.xUberCheckRequest.observe(this, Observer<XuperCheckRequest> { xUberCheckRequest ->
            try {
                if (xUberCheckRequest!!.responseData!!.requests != null) {
                    val status = xUberCheckRequest.let { it.responseData!!.requests!!.status }
                    if (status != mViewModel.currentStatus.value) {

                        currentStatus = status!!.toUpperCase()

                        mViewModel.currentStatus.value = xUberCheckRequest.let { it.responseData!!.requests!!.status }
                        mBinding.tvXuberPickupLocation.text = xUberCheckRequest.let { it.responseData!!.requests!!.s_address }
                        mViewModel.userName.value = xUberCheckRequest.responseData!!.requests!!.user!!.first_name +
                                " " + xUberCheckRequest.responseData.requests!!.user!!.last_name!!
                        mViewModel.serviceType.value = xUberCheckRequest.responseData.requests.service!!.service_name
                        mViewModel.descImage.value = xUberCheckRequest.responseData.requests.allow_image.toString()
                        mViewModel.strDesc.value = xUberCheckRequest.responseData.requests.allow_description.toString()
                        mViewModel.userRating.value = String.format(resources.getString(R.string.xuper_rating_user),
                                xUberCheckRequest.responseData.requests.user!!.rating!!.toDouble())

                        setUserImage(xUberCheckRequest.responseData.requests.user.picture)

                        mViewModel.polyLineSrc.value = LatLng(xUberCheckRequest.responseData.requests.s_latitude!!,
                                xUberCheckRequest.responseData.requests.s_longitude!!)

                        if (!roomConnected) {
                            reqID = xUberCheckRequest.responseData.requests.id
                            PreferencesHelper.put(PreferencesKey.SERVICE_REQ_ID, reqID)
                            if (reqID != 0) {
                                SocketManager.emit(Constants.RoomName.SERVICE_ROOM_NAME, Constants.RoomId.getServiceRoom(reqID))
                            }

                        }

                        mViewModel.polyLineSrc.value = LatLng(xUberCheckRequest.responseData.requests.s_latitude,
                                xUberCheckRequest.responseData.requests.s_longitude)

                        fab_xuber_menu_call.setOnClickListener {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:${xUberCheckRequest.responseData.requests.user.mobile}")
                            startActivity(intent)
                        }

                        writePreferences(Constants.Chat.ADMIN_SERVICE, SERVICE)
                        writePreferences(Constants.Chat.USER_ID, xUberCheckRequest.responseData.requests.user_id)
                        writePreferences(Constants.Chat.REQUEST_ID, xUberCheckRequest.responseData.requests.id)
                        writePreferences(Constants.Chat.PROVIDER_ID, xUberCheckRequest.responseData.requests.provider_id)
                        writePreferences(Constants.Chat.USER_NAME, xUberCheckRequest.responseData.requests.user.first_name
                                + " " + xUberCheckRequest.responseData.requests.user.last_name)
                        writePreferences(Constants.Chat.PROVIDER_NAME, xUberCheckRequest.responseData.provider_details?.first_name
                                + " " + xUberCheckRequest.responseData.provider_details?.last_name)


                        when (status) {
                            ACCEPTED -> whenAccepted()

                            ARRIVED -> whenArrived()

                            // Start Service
                            PICKED_UP -> {
                                whenStarted()
                                startTheTimer()
                            }

                            // Complete the Service
                            DROPPED -> whenDropped(true)

                            // Confirm Payment when cash flow
                            COMPLETED -> whenPayment()

                        }
                    }
                } else {
                    BaseLocationService.BROADCAST = Constants.BroadCastTypes.BASE_BROADCAST
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                BaseLocationService.BROADCAST = Constants.BroadCastTypes.BASE_BROADCAST
                finish()
            }
        })

        mViewModel.xUberUpdateRequest.observe(this, Observer<UpdateRequest> { updateRequest ->
            if (updateRequest!!.statusCode.equals("200")) {
                loadingObservable.value = false
                when (updateRequest.responseData!!.status) {
                    ARRIVED -> whenArrived()
                    PICKED_UP -> {
                        whenStarted()
                        startTheTimer()
                    }

                    DROPPED -> whenDropped(false)
                }
            }
        })

        mViewModel.xUberCancelRequest.observe(this, Observer<CancelRequestModel> {
            loadingObservable.value = false
            ViewUtils.showToast(this, resources.getString(R.string.request_canceled), true)
            finish()
        })

        SocketManager.onEvent(Constants.RoomName.STATUS, Emitter.Listener {
            if (it[0].toString().contains(SERVICE)) {
                roomConnected = true
            }
        })

        SocketManager.onEvent(Constants.RoomName.SERVICE_REQ, Emitter.Listener {
            if (it[0].toString().contains("payment_mode")) {
                val data = it[0] as JSONObject
                paymentMode = data.getString("payment_mode")
                mViewModel.currentStatus.value = ""
            }
            mViewModel.callXUberCheckRequest()
        })

        SocketManager.setOnSocketRefreshListener(object : SocketListener.ConnectionRefreshCallBack {
            override fun onRefresh() {
                SocketManager.emit(Constants.RoomName.SERVICE_ROOM_NAME, Constants.RoomId.getServiceRoom(reqID))
            }
        })
    }

    private fun initialiseMap() {
        fragmentMap = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        fragmentMap.getMapAsync(this)
    }

    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = false) {
        if (!isAnimateMap) mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM))
        else mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM))
    }

    private fun whenDropped(isCheckRequest: Boolean) {
        fab_xuber_menu.visibility = View.GONE
        val bundle = Bundle()
        var currentPaymentMode = ""

        if (isCheckRequest) {
            mBinding.llBottomService.llServiceTime.visibility = View.GONE
            val strCheckRequest = Gson().toJson(mViewModel.xUberCheckRequest.value!!)
            bundle.putString("strCheckReq", strCheckRequest)
            bundle.putBoolean("fromCheckReq", true)
            cmXuberServiceTime.stop()
            currentPaymentMode = mViewModel.xUberCheckRequest.value?.responseData?.requests?.payment_mode!!
            if (paymentMode.equals(""))
                paymentMode = currentPaymentMode
        } else {
            mBinding.llBottomService.llServiceTime.visibility = View.GONE
            val strUpdateRequest = Gson().toJson(mViewModel.xUberUpdateRequest.value!!)
            bundle.putString("strUpdateReq", strUpdateRequest)
            bundle.putBoolean("fromCheckReq", false)
            cmXuberServiceTime.stop()
            currentPaymentMode = mViewModel.xUberUpdateRequest.value?.responseData?.payment_mode!!
            if (paymentMode.equals(""))
                paymentMode = currentPaymentMode
        }

        if (!paymentMode.equals(currentPaymentMode)) {
            paymentMode = currentPaymentMode
            showInvoice(bundle, true)
        } else showInvoice(bundle, false)

    }


    private fun whenPayment() {
        fab_xuber_menu.visibility = View.GONE
        mGoogleMap!!.clear()
        mBinding.llBottomService.fbCamera.visibility = View.GONE
        val bundle = Bundle()
        val strCheckRequest = Gson().toJson(mViewModel.xUberCheckRequest.value)
        bundle.putString("strCheckReq", strCheckRequest)
        bundle.putBoolean("isFromCheckRequest", true)
        val ratingDialog = supportFragmentManager.findFragmentByTag("ratingDialog")
        if (ratingDialog != null && ratingDialog.isVisible) {
        } else if (!ratingPage.isShown()) {
            ratingPage.arguments = bundle
            ratingPage.show(supportFragmentManager, "ratingDialog")
        }
    }

    private fun showInvoice(bundle: Bundle, update: Boolean) {
        llBottomService.visibility = View.GONE
        invoicePage.arguments = bundle
        if (update)
            invoicePage.dismiss()
        if (!invoicePage.isShown()) invoicePage.show(supportFragmentManager, "xuperinvoice")
        invoicePage.isCancelable = false
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(mGoogleMap: GoogleMap?) {
        try {
            mGoogleMap!!.isMyLocationEnabled = false
            mGoogleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

        this.mGoogleMap = mGoogleMap
        updateCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {
        runOnUiThread {
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
            mGoogleMap!!.uiSettings.isCompassEnabled = true
        }

        if (getPermissionUtil().hasPermission(this, Constants.RequestPermission.PERMISSIONS_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack {
                override fun onSuccess(location: Location) {
                    mViewModel.latitude.value = location.latitude
                    mViewModel.longitude.value = location.longitude
                    updateMapLocation(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!))
                    mViewModel.callXUberCheckRequest()
                }

                override fun onFailure(message: String) {
                }
            })
        else if (getPermissionUtil().requestPermissions(this, Constants.RequestPermission.PERMISSIONS_LOCATION,
                        Constants.RequestCode.PERMISSIONS_CODE_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack {
                override fun onSuccess(location: Location) {
                    mViewModel.latitude.value = location.latitude
                    mViewModel.longitude.value = location.longitude
                    updateMapLocation(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!))
                    mViewModel.callXUberCheckRequest()
                }

                override fun onFailure(message: String) {
                }
            })
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BaseLocationService.BROADCAST))
        mViewModel.callXUberCheckRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        checkRequestTimer?.cancel()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            val isGpsEnabled = intent.getBooleanExtra("ISGPS_EXITS", false)

            if (isGpsEnabled) updateMap(location)
            else if (!isLocationDialogShown) {
                isLocationDialogShown = true
                CommonMethods.checkGps(context)
            }
        }
    }

    private fun updateMap(location: Location) {
        mViewModel.latitude.value = location.latitude
        mViewModel.longitude.value = location.longitude

//        if (checkStatusApiCounter++ % 2 == 0) mViewModel.callXUberCheckRequest()

        if (roomConnected) {
            val locationObj = JSONObject()
            locationObj.put("latitude", location.latitude)
            locationObj.put("longitude", location.longitude)
            locationObj.put("room", Constants.RoomId.getServiceRoom(reqID))
            SocketManager.emit("send_location", locationObj)
        }

        if (BaseApplication.getCustomPreference!!.getBoolean(PreferencesKey.SERVICE_OTP, false)) {
            if (startLatLng.latitude != 0.0) endLatLng = startLatLng
            startLatLng = LatLng(location.latitude, location.longitude)

            if (endLatLng.latitude != 0.0 && polyLine.size > 0) try {
                CarMarkerAnimUtil().carAnim(srcMarker!!, endLatLng, startLatLng)
                polyLineRerouting(endLatLng, polyLine)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun polyLineRerouting(point: LatLng, polyLine: ArrayList<LatLng>) {
        val index = polyUtil.locationIndexOnEdgeOrPath(point, polyLine, false, true, 50.0)
        if (index >= 0) {
            polyLine.subList(0, index + 1).clear()
//            polyLine.add(0, point)
            mPolyline!!.remove()
            val options = PolylineOptions()
            options.addAll(polyLine)
            mPolyline = mGoogleMap!!.addPolyline(options.width(5f).color
            (ContextCompat.getColor(baseContext, R.color.colorBlack)))
        } else {
            canDrawPolyLine = true
            drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!), mViewModel.polyLineSrc.value!!)
        }
    }

    private fun drawRoute(src: LatLng, dest: LatLng) {
        mViewModel.tempSrc.value = src
        mViewModel.tempDest.value = dest
        if (canDrawPolyLine) {
            canDrawPolyLine = false
            Handler().postDelayed({ canDrawPolyLine = true }, 10000)
            PolylineUtil(this).execute(DirectionUtils().getDirectionsUrl(src, dest,
                    getText(R.string.google_map_key).toString()))
        }
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
            (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.xubermarkertest))))

            CarMarkerAnimUtil().carAnim(srcMarker!!, polyLine[0], polyLine[1])

            mGoogleMap!!.addMarker(MarkerOptions().position(polyLine[polyLine.size - 1]).icon
            (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.ic_marker_stop))))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getDistanceTime(meters: Double, seconds: Double) {

    }

    override fun whenFail(statusCode: String) {

        if (canDrawPolyLine) {
            canDrawPolyLine = false
            Handler().postDelayed({ canDrawPolyLine = true }, 20000)
            val key = BaseApplication.getCustomPreference!!.getString(PreferencesKey.ALTERNATE_MAP_KEY, "")
            PolylineUtil(this).execute(DirectionUtils().getDirectionsUrl
            (mViewModel.tempSrc.value, mViewModel.tempDest.value, key))
        }

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

    override fun showErrorMessage(error: String) {
        mViewModel.showLoading.value = false
        ViewUtils.showToast(this, error, false)
    }

    private fun whenAccepted() {
        if (mViewModel.xUberUpdateRequest.value != null)
            mViewModel.currentStatus.value = mViewModel.xUberUpdateRequest.value?.let { it.responseData!!.status!!.toUpperCase(Locale.getDefault()) }
        mBinding.llBottomService.llServiceTime.visibility = View.GONE
        mBinding.llBottomService.fbCamera.visibility = View.GONE
        mBinding.llBottomService.llConfirm.tvCancel.visibility = View.VISIBLE
        mBinding.llBottomService.llConfirm.tvAllow.text = ARRIVED
        mBinding.llBottomService.llConfirm.tvCancel.text = CANCEL
        drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!), mViewModel.polyLineSrc.value!!)
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, true)
    }

    private fun whenArrived() {
        if (mViewModel.xUberUpdateRequest.value != null)
            mViewModel.currentStatus.value = mViewModel.xUberUpdateRequest.value?.let {
                it.responseData!!.status!!.toUpperCase(Locale.getDefault())
            }
        fab_xuber_menu.visibility = View.GONE
        mBinding.llBottomService.llServiceTime.visibility = View.GONE

        if (BaseApplication.getCustomPreference!!.getBoolean(PreferencesKey.SERVICE_OTP, false))
            edtXuperOtp.visibility = View.VISIBLE
        else edtXuperOtp.visibility = View.GONE

        if (mViewModel.xUberCheckRequest.value?.responseData != null &&
                mViewModel.xUberCheckRequest.value!!.responseData?.requests!!.created_type == "ADMIN")
            edtXuperOtp.visibility = View.GONE


        if (mViewModel.xUberCheckRequest.value!!.responseData!!.requests!!.service!!.allow_before_image == 1)
            mBinding.llBottomService.fbCamera.visibility = View.VISIBLE
        else mBinding.llBottomService.fbCamera.visibility = View.GONE
        mBinding.llBottomService.llConfirm.tvCancel.visibility = View.GONE
        mBinding.llBottomService.llConfirm.tvAllow.text = START
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
    }

    private fun whenStarted() {
        if (mViewModel.xUberUpdateRequest.value != null)
            mViewModel.currentStatus.value = mViewModel.xUberUpdateRequest.value?.let { it.responseData!!.status!!.toUpperCase(Locale.getDefault()) }
        fab_xuber_menu.visibility = View.GONE
        mGoogleMap!!.clear()
        if (mViewModel.xUberCheckRequest.value!!.responseData!!.requests!!.service!!.allow_after_image == 1)
            mBinding.llBottomService.fbCamera.visibility = View.VISIBLE
        else mBinding.llBottomService.fbCamera.visibility = View.GONE
        edtXuperOtp.visibility = View.GONE
        mBinding.llBottomService.llServiceTime.visibility = View.VISIBLE
        mBinding.llBottomService.llConfirm.tvCancel.visibility = View.GONE
        mBinding.llBottomService.llConfirm.tvAllow.text = COMPLETED
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
    }

    private fun showRating() {
        if (mViewModel.xUberUpdateRequest.value != null)
            mViewModel.currentStatus.value = mViewModel.xUberUpdateRequest.value?.let { it.responseData!!.status!!.toUpperCase(Locale.getDefault()) }
        fab_xuber_menu.visibility = View.GONE
        mGoogleMap!!.clear()
        mBinding.llBottomService.fbCamera.visibility = View.GONE
        val bundle = Bundle()
        val strCheckRequest = Gson().toJson(mViewModel.xUberCheckRequest.value)
        bundle.putString("strCheckReq", strCheckRequest)
        bundle.putBoolean("isFromCheckRequest", true)
        val ratingDialog = supportFragmentManager.findFragmentByTag("ratingDialog")
        if (ratingDialog != null && ratingDialog.isVisible) {
        } else if (!ratingPage.isShown()) {
            ratingPage.arguments = bundle
            ratingPage.isCancelable = false
            ratingPage.show(supportFragmentManager, "ratingDialog")
        }
    }

    private fun setUserImage(strUrl: String) {
        Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .circleCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .error(R.drawable.ic_user_place_holder))
                .load(strUrl)
                .into(mBinding.llBottomService.ivXuperUSer)
    }

    override fun updateService(view: View) {
        when (view.id) {
            R.id.tvAllow -> {

                when (mViewModel.currentStatus.value) {
                    // Update Status As Arrived while the request is in accepted status
                    ACCEPTED -> {
                        if (BaseApplication.getCustomPreference!!.getBoolean(PreferencesKey.SERVICE_OTP, false))
                            edtXuperOtp.visibility = View.VISIBLE
                        else edtXuperOtp.visibility = View.GONE
                        mViewModel.updateRequest(ARRIVED, null, false, "", "")
                    }

                    // To Start the Service while the request is in Arrived state
                    ARRIVED -> if (BaseApplication.getCustomPreference!!.getBoolean(PreferencesKey.SERVICE_OTP, false) &&
                            mViewModel.xUberCheckRequest.value?.responseData!!.requests!!.created_type != "ADMIN") {
                        when {
                            mViewModel.otp.value.isNullOrEmpty() ->
                                ViewUtils.showToast(this, resources.getString(R.string.empty_otp), false)
                            mViewModel.otp.value != mViewModel.xUberCheckRequest.value!!.responseData!!.requests!!.otp ->
                                ViewUtils.showToast(this, resources.getString(R.string.invalid_otp), false)
                            else -> checkBeforeImageValidAndUpdateRequest()
                        }
                    } else {
                        checkBeforeImageValidAndUpdateRequest()
                    }

                    // Complete the service while request is in Started state
                    PICKED_UP -> if (backImgFile == null) {
                        if (mViewModel.xUberCheckRequest.value!!.responseData!!.requests!!.service!!.allow_after_image == 1)
                            ViewUtils.showToast(this, resources.getString(R.string.empty_back_image), false)
                        else showAdditionalChargeConfirm(DROPPED, null, false)
                    } else {
                        backImgMultiPart = getImageMultiPart(backImgFile!!, false)
                        showAdditionalChargeConfirm(DROPPED, backImgMultiPart, false)
                    }
                }
            }

            R.id.tvCancel -> XUberCancelReasonFragment().show(supportFragmentManager, "XUberCancelReasonFragment")
        }
    }

    private fun checkBeforeImageValidAndUpdateRequest() {
        if (mViewModel.xUberCheckRequest.value!!.responseData!!.requests!!.service!!.allow_before_image == 1 && frontImgFile == null) {
            ViewUtils.showToast(this, resources.getString(R.string.empty_front_image), false)
        } else {
            if (frontImgFile != null)
                frontImgMultiPart = getImageMultiPart(frontImgFile!!, true)
            mViewModel.updateRequest(PICKED_UP, frontImgMultiPart, true, "", "")
        }
    }

    private fun showAdditionalChargeConfirm(status: String, file: MultipartBody.Part?, isFrontImage: Boolean) {
        ViewUtils.showAlert(this, getString(R.string.additional_charge_desc),
                getString(R.string.yes), getString(R.string.no), object : ViewUtils.ViewCallBack {
            override fun onPositiveButtonClick(dialog: DialogInterface) {
                XUberExtraChargeDialog.newInstance(object : GetExtraChargeInterface {
                    override fun getExtraCharge(extraCharge: String, extraNotes: String) {
                        //
                        //   println("RRR :: extraCharge = [${extraCharge}], extraNotes = [${extraNotes}]")
                        mViewModel.updateRequest(status, file, isFrontImage, extraNotes, extraCharge)
                    }
                }).show(supportFragmentManager, "extraRate")
                dialog.dismiss()
            }

            override fun onNegativeButtonClick(dialog: DialogInterface) {
                mViewModel.updateRequest(status, file, isFrontImage, "", "")
                dialog.dismiss()
            }
        })
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        val time = Date().time - chronometer!!.base
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
        val formattedTime = (if (h < 10) "0$h" else h).toString() + ":" +
                (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
        Log.e("Chrono", "------$h---$m---$s")
        chronometer.text = formattedTime
    }

    @SuppressLint("SetTextI18n")
    fun startTheTimer() {
        if (!(mViewModel.xUberCheckRequest.value!!.responseData!!.requests!!.started_at.isNullOrEmpty())) {
            val startedTime = mViewModel.xUberCheckRequest.value!!.responseData!!.requests!!.started_at
            if (!startedTime.isNullOrEmpty()) {
                localServiceTime = CommonMethods.getLocalTime(startedTime, "yyyy-dd-MM HH:mm:ss")
                val timeinMilliSec = localServiceTime
                val timeinMilli = Date().time - (timeinMilliSec!!)
                val h = (timeinMilli / 3600000).toInt()
                val m = (timeinMilli - h * 3600000).toInt() / 60000
                val s = (timeinMilli - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
                cmXuberServiceTime.base = localServiceTime!!
                cmXuberServiceTime.text = "00:00:00"
                cmXuberServiceTime.start()
                cmXuberServiceTime.onChronometerTickListener = this
            }
        } else {
            cmXuberServiceTime.base = SystemClock.elapsedRealtime()
            cmXuberServiceTime.text = "00:00:00"
            cmXuberServiceTime.start()
        }
    }

    override fun getFilePath(filePath: Uri) {
        val currentStatus = mViewModel.currentStatus.value ?: ""
        val isFront = (currentStatus.equals(ARRIVED, true) || currentStatus.equals(ACCEPTED, true))
        getImageFile(isFront, filePath)
        if (!isFront)
            updateService(mBinding.llBottomService.tvAllow)
    }

    private fun getImageMultiPart(file: File, isFrontImage: Boolean): MultipartBody.Part {
        val fileBody: MultipartBody.Part
        val requestFile = RequestBody.create(MediaType.parse("*/*"), file)
        fileBody = if (isFrontImage)
            MultipartBody.Part.createFormData("before_picture", file.name, requestFile)
        else MultipartBody.Part.createFormData("after_picture", file.name, requestFile)
        return fileBody
    }

    private fun getImageFile(isFront: Boolean, fileUri: Uri) =
            if (isFront) frontImgFile = File(fileUri.path)
            else backImgFile = File(fileUri.path)

    override fun reasonForCancel(reason: String) {
        if (reason.isNotEmpty()) {
            val params = HashMap<String, String>()
            val id = mViewModel.xUberCheckRequest.value!!.responseData!!.requests!!.id.toString()
            params[Constants.Common.ID] = id
            params[Constants.Common.SERVICE_ID] = "3"
            params[CANCEL] = reason
            mViewModel.cancelRequest(params)
        }
    }

    private fun showInfoWindow(context: Context, v: View, allowDescription: String?, allowImage: String?) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = layoutInflater.inflate(R.layout.dialog_info_window, null)
        val ivClose = popupView!!.ivClose
        val ivDesImage = popupView!!.ivInfo
        val tvDescription = popupView!!.tv_description
        ivClose.setOnClickListener { popupWindow!!.dismiss() }

        if (!allowImage.isNullOrEmpty()) Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.image_placeholder)
                        .error(R.drawable.image_placeholder))
                .load(allowImage)
                .into(ivDesImage)

        if (allowDescription != null && !allowDescription.equals("null", true))
            tvDescription.text = allowDescription
        else
            tvDescription.text = getString(R.string.no_instruction_found)

        val displayFrame = Rect()
        v.getWindowVisibleDisplayFrame(displayFrame)
        val displayFrameWidth = displayFrame.right - displayFrame.left
        val loc = IntArray(2)
        v.getLocationInWindow(loc)
        val xoff: Int
        if (popupWindow == null) {
            popupWindow = PopupWindow(
                    popupView,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    true)
            popupWindow!!.isOutsideTouchable = false
            popupWindow!!.isFocusable = false
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        if (!popupWindow!!.isShowing) {
            val y = v.y
            popupView!!.animation = AnimationUtils.loadAnimation(context, R.anim.popup_anim_in)
            val margin = displayFrameWidth - (loc[0] + v.width)
            xoff = displayFrameWidth - margin - popupWindow!!.width - loc[0]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) popupWindow!!.elevation = 20f
            popupWindow!!.isOutsideTouchable = false
            popupWindow!!.showAtLocation(v, Gravity.END, xoff, y.toInt() + 200)
            popupWindow!!.animationStyle = R.anim.popup_anim_in
        }

        popupWindow!!.setOnDismissListener {
            if (popupWindow != null) popupWindow = null
        }
    }

    override fun showInfoWindow(view: View) =
            showInfoWindow(this, mBinding.llBottomService.ibInstruction,
                    mViewModel.strDesc.value.toString(), mViewModel.descImage.value.toString())

    override fun onClick(v: View?) {
        val currentStatus = mViewModel.currentStatus.value ?: ""
        if (currentStatus.isNotEmpty()) {
            isFront = (currentStatus.equals(ARRIVED, true) || currentStatus.equals(ACCEPTED, true))
            val dialogUploadPicture = UploadPictureDialog()
            val bundle = Bundle()
            bundle.putBoolean("isFront", isFront)
            dialogUploadPicture.arguments = bundle
            dialogUploadPicture.show(supportFragmentManager, "takepicture")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            500 ->
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        isGPSEnabled = true
                        isLocationDialogShown = false
                        if (getPermissionUtil().hasPermission(this, Constants.RequestPermission.PERMISSIONS_LOCATION)) {
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
}