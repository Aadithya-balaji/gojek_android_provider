package com.xjek.xuberservice.xuberMainActivity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import com.xjek.base.data.Constants.RideStatus.ACCEPTED
import com.xjek.base.data.Constants.RideStatus.ARRIVED
import com.xjek.base.data.Constants.RideStatus.COMPLETED
import com.xjek.base.data.Constants.RideStatus.DROPPED
import com.xjek.base.data.Constants.RideStatus.PICKED_UP
import com.xjek.base.data.Constants.XuperProvider.CANCEL
import com.xjek.base.data.Constants.XuperProvider.START
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.writePreferences
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.location_service.BaseLocationService
import com.xjek.base.utils.*
import com.xjek.base.socket.SocketListener
import com.xjek.base.socket.SocketManager
import com.xjek.base.utils.CarMarkerAnimUtil
import com.xjek.base.utils.CommonMethods
import com.xjek.base.utils.PolyUtil
import com.xjek.base.utils.ViewUtils
import com.xjek.base.utils.polyline.DirectionUtils
import com.xjek.base.utils.polyline.PolyLineListener
import com.xjek.base.utils.polyline.PolylineUtil
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.ActivityXuberMainBinding
import com.xjek.xuberservice.interfaces.GetFilePathInterface
import com.xjek.xuberservice.interfaces.GetReasonsInterface
import com.xjek.xuberservice.invoice.DialogXuperInvoice
import com.xjek.xuberservice.model.CancelRequestModel
import com.xjek.xuberservice.model.UpdateRequest
import com.xjek.xuberservice.model.XuperCheckRequest
import com.xjek.xuberservice.rating.DialogXuperRating
import com.xjek.xuberservice.reasons.XUberCancelReasonFragment
import com.xjek.xuberservice.uploadImage.DialogUploadPicture
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

class XuberDashBoardActivity : BaseActivity<ActivityXuberMainBinding>(),
        XuberDasbBoardNavigator,
        OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        Chronometer.OnChronometerTickListener,
        GetFilePathInterface,
        GetReasonsInterface,
        PolyLineListener, View.OnClickListener {


    private lateinit var mViewModel: XuberDashboardViewModel
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mBinding: ActivityXuberMainBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>
    private var localServiceTime: Long? = null
    private var mGoogleMap: GoogleMap? = null
    private var frontImgFile: File? = null
    private var backImgFile: File? = null
    private var frontImgMultiPart: MultipartBody.Part? = null
    private var backImgMultiPart: MultipartBody.Part? = null
    private val invoicePage = DialogXuperInvoice()
    private val ratingPage = DialogXuperRating()
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

    override fun getLayoutId(): Int = R.layout.activity_xuber_main

    private var roomConnected: Boolean = false


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityXuberMainBinding
        mViewModel = XuberDashboardViewModel()
        mViewModel.navigator = this
        mBinding.xuberViewModel = mViewModel
        mBinding.lifecycleOwner = this
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>
        mBinding.llBottomService.fbCamera.setOnClickListener(this)
        initialiseMap()
        getApiResponse()
    }


    fun getApiResponse() {
        mViewModel.xuperCheckRequest.observe(this, Observer<XuperCheckRequest> { xuberCheckRequest ->
            try {
                if (xuberCheckRequest!!.responseData!!.requests != null) {
                    val status = xuberCheckRequest.let { it.responseData!!.requests!!.status }
                    if (status != mViewModel.currentStatus.value) {
                        mViewModel.currentStatus.value = xuberCheckRequest.let { it.responseData!!.requests!!.status }
                        mBinding.tvXuberPickupLocation.text = xuberCheckRequest.let { it.responseData!!.requests!!.s_address }
                        mViewModel.userName.value = xuberCheckRequest.responseData!!.requests!!.user!!.first_name +
                                " " + xuberCheckRequest.responseData.requests!!.user!!.last_name!!
                        mViewModel.serviceType.value = xuberCheckRequest.responseData.requests.service!!.service_name
                        mViewModel.descImage.value = xuberCheckRequest.responseData!!.requests!!.allow_image.toString()
                        mViewModel.strDesc.value = xuberCheckRequest.responseData!!.requests!!.allow_description.toString()
                        mViewModel.userRating.value=String.format(resources.getString(R.string.xuper_rating_user),xuberCheckRequest.responseData!!.requests!!.user!!.rating!!.toDouble())

                        if (xuberCheckRequest.responseData.requests.user!!.picture != null) {
                            setUserImage(xuberCheckRequest.responseData.requests.user.picture.toString())
                        } else {
                            setUserImage("")
                        }

                        mViewModel.polyLineSrc.value = LatLng(xuberCheckRequest.responseData.requests.s_latitude!!,
                                xuberCheckRequest.responseData.requests.s_longitude!!)

                    if (!roomConnected) {
                        roomConnected = true
                        val reqID = xuberCheckRequest.responseData.requests.id
                        PreferencesHelper.put(PreferencesKey.REQ_ID, reqID)
                        SocketManager.emit(Constants.ROOM_NAME.TRANSPORT_ROOM_NAME, Constants.ROOM_ID.TRANSPORT_ROOM)
                    }

                    mViewModel.polyLineSrc.value = LatLng(xuberCheckRequest.responseData.requests.s_latitude!!,
                            xuberCheckRequest.responseData.requests.s_longitude!!)

                        when (status) {
                            ACCEPTED -> whenAccepted()

                            ARRIVED -> whenArrived()

                            PICKED_UP -> {
                                whenStarted()
                                startTheTimer()
                            }

                            DROPPED -> whenDropped(true)

                            COMPLETED -> whenPayment(xuberCheckRequest.responseData)
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

        //Update Request
        mViewModel.xuperUdpateRequest.observe(this, Observer<UpdateRequest> { updateRequest ->
            if (updateRequest!!.statusCode.equals("200")) {
                loadingObservable.value = false
                when (updateRequest.responseData!!.status) {
                    ARRIVED -> whenArrived()

                    PICKED_UP -> {
                        whenStarted()
                        startTheTimer()
                    }

                    DROPPED -> whenDropped(false)

                    COMPLETED -> {

                    }
                }
            }
        })

        mViewModel.xuperCancelRequest.observe(this, Observer<CancelRequestModel> {
            loadingObservable.value = false
            ViewUtils.showToast(this@XuberDashBoardActivity, resources.getString(R.string.request_canceled), true)
            finish()
        })


        SocketManager.onEvent(Constants.ROOM_NAME.SERVICE_REQ, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK service request " + it[0])
            mViewModel.callXuberCheckRequest()
        })

        SocketManager.setOnSocketRefreshListener(object : SocketListener.connectionRefreshCallBack {
            override fun onRefresh() {
                if (roomConnected) {
                    roomConnected = false
                    SocketManager.emit(Constants.ROOM_NAME.SERVICE_ROOM_NAME, Constants.ROOM_ID.SERVICE_ROOM)
                }
            }
        })

    }

    private fun initialiseMap() {
        fragmentMap = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        fragmentMap.getMapAsync(this@XuberDashBoardActivity)
    }

    override fun goToLocationPick() {
        try {
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        this.mGoogleMap?.setOnCameraMoveListener(this)
        this.mGoogleMap?.setOnCameraIdleListener(this)
    }

    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = false) {
        if (!isAnimateMap) mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM))
        else mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM))
    }

    //Completed Not Payment Successful
    fun whenDropped(isCheckRequest: Boolean) {
        val bundle = Bundle()
        if (isCheckRequest) {
            mBinding.llBottomService.llServiceTime.visibility = View.GONE
            val strCheckRequest = Gson().toJson(mViewModel.xuperCheckRequest.value!!)
            bundle.putString("strCheckReq", strCheckRequest)
            bundle.putBoolean("fromCheckReq", true)
            cmXuberServiceTime.stop()

        } else {
            mBinding.llBottomService.llServiceTime.visibility = View.GONE
            val strUpdateRequest = Gson().toJson(mViewModel.xuperUdpateRequest.value!!)
            bundle.putString("strUpdateReq", strUpdateRequest)
            bundle.putBoolean("fromCheckReq", false)
            cmXuberServiceTime.stop()
        }
        llBottomService.visibility = View.GONE
        invoicePage.arguments = bundle
        if (invoicePage.isShown() == false)
            invoicePage.show(supportFragmentManager, "xuperinvoice")
        invoicePage.isCancelable = false

    }

    override fun goBack() {
        onBackPressed()
    }

    override fun moveStatusFlow() {

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
        this.mGoogleMap?.setOnCameraMoveListener(this)
        this.mGoogleMap?.setOnCameraIdleListener(this)

        updateCurrentLocation()
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {
        runOnUiThread {
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
            mGoogleMap!!.uiSettings.isCompassEnabled = true
        }

        if (getPermissionUtil().hasPermission(this, Constants.RequestPermission.PERMISSIONS_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    if (location != null) {
                        mViewModel.latitude.value = location.latitude
                        mViewModel.longitude.value = location.longitude
                        updateMapLocation(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!))
                        mViewModel.callXuberCheckRequest()
                    }
                }

                override fun onFailure(messsage: String?) {
                }
            })
        else if (getPermissionUtil().requestPermissions(this, Constants.RequestPermission.PERMISSIONS_LOCATION, Constants.RequestCode.PERMISSIONS_CODE_LOCATION))
            LocationUtils.getLastKnownLocation(applicationContext, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    if (location != null) {
                        mViewModel.latitude.value = location.latitude
                        mViewModel.longitude.value = location.longitude
                        updateMapLocation(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!))
                        mViewModel.callXuberCheckRequest()
                    }
                }

                override fun onFailure(messsage: String?) {
                }
            })
    }

    override fun onCameraMove() {
        /*if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED*/
    }

    override fun onCameraIdle() {
        //  sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BaseLocationService.BROADCAST))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRRR:: XuberDashboardActivity")
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            if (location != null) {
                mViewModel.latitude.value = location.latitude
                mViewModel.longitude.value = location.longitude
//                updateMapLocation(LatLng(location.latitude, location.longitude))

                if (checkStatusApiCounter++ % 2 == 0) mViewModel.callXuberCheckRequest()

                if(roomConnected) {
                    var locationObj = JSONObject()
                    locationObj.put("latitude",location.latitude)
                    locationObj.put("longitude",location.longitude)
                    locationObj.put("room",Constants.ROOM_ID.SERVICE_ROOM)
                    SocketManager.emit("send_location", locationObj)
                    Log.e("SOCKET", "SOCKET_SK Location update service called")
                }


                if (startLatLng.latitude > 0) endLatLng = startLatLng
                startLatLng = LatLng(location.latitude, location.longitude)

                if (endLatLng.latitude > 0 && polyLine.size > 0) try {
                    CarMarkerAnimUtil().carAnim(srcMarker!!, endLatLng, startLatLng)
                    polyLineRerouting(endLatLng, polyLine)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun polyLineRerouting(point: LatLng, polyLine: ArrayList<LatLng>) {
        val index = polyUtil.locationIndexOnEdgeOrPath(point, polyLine, false, true, 10.0)
        if (index >= 0) {
            polyLine.subList(0, index + 1).clear()
            polyLine.add(0, point)
            mPolyline!!.remove()
            val options = PolylineOptions()
            options.addAll(polyLine)
            mPolyline = mGoogleMap!!.addPolyline(options.width(5f).color
            (ContextCompat.getColor(baseContext, R.color.xuper_blue)))
            println("RRR mPolyline = " + polyLine.size)
        } else {
            canDrawPolyLine = true
            drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!), mViewModel.polyLineSrc.value!!)
        }
    }

    private fun drawRoute(src: LatLng, dest: LatLng) {
        if (canDrawPolyLine) {
            canDrawPolyLine = false
            Handler().postDelayed({ canDrawPolyLine = true }, 10000)
            PolylineUtil(this).execute(DirectionUtils().getDirectionsUrl(src, dest, getText(R.string.google_map_key).toString()))
        }
    }

    override fun whenDone(output: PolylineOptions) {
        mGoogleMap!!.clear()

        mPolyline = mGoogleMap!!.addPolyline(output.width(5f).color
        (ContextCompat.getColor(baseContext, R.color.xuper_blue)))

        polyLine = output.points as ArrayList<LatLng>

        val builder = LatLngBounds.Builder()

        for (latLng in polyLine) builder.include(latLng)

        mGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100))

        srcMarker = mGoogleMap!!.addMarker(MarkerOptions().position(polyLine[0]).icon
        (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.ic_marker_bike))))

        mGoogleMap!!.addMarker(MarkerOptions().position(polyLine[polyLine.size - 1]).icon
        (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.ic_marker_stop))))

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

    override fun showErrorMessage(error: String) {
        mViewModel.showLoading.value = false
        ViewUtils.showToast(this, error, false)
    }

    //When ride accepted
    private fun whenAccepted() {
        mBinding.llBottomService.llServiceTime.visibility = View.GONE
        mBinding.llBottomService.fbCamera.visibility = View.GONE
        mBinding.llBottomService.llConfirm.tvCancel.visibility = View.VISIBLE
        mBinding.llBottomService.llConfirm.tvAllow.text = ARRIVED
        mBinding.llBottomService.llConfirm.tvCancel.text = CANCEL
        drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!), mViewModel.polyLineSrc.value!!)
    }

    //When ride arrived
    private fun whenArrived() {
        mBinding.llBottomService.llServiceTime.visibility = View.GONE
        edtXuperOtp.visibility = View.VISIBLE
        mBinding.llBottomService.fbCamera.visibility = View.VISIBLE
        mBinding.llBottomService.llConfirm.tvCancel.visibility = View.GONE
        mBinding.llBottomService.llConfirm.tvAllow.text = START
        writePreferences(PreferencesKey.CAN_SAVE_LOCATION, false)
    }

    private fun whenStarted() {
        mBinding.llBottomService.fbCamera.visibility = View.VISIBLE
        edtXuperOtp.visibility = View.GONE
        mBinding.llBottomService.llServiceTime.visibility = View.VISIBLE
        mBinding.llBottomService.llConfirm.tvCancel.visibility = View.GONE
        mBinding.llBottomService.llConfirm.tvAllow.text = COMPLETED
        writePreferences(PreferencesKey.CAN_SAVE_LOCATION, false)
    }

    //After Payment Successful
    private fun whenPayment(responseData: XuperCheckRequest.ResponseData) {
        mBinding.llBottomService.fbCamera.visibility = View.GONE
        val bundle = Bundle()
        val strCheckRequest = Gson().toJson(mViewModel.xuperCheckRequest.value)
        bundle.putString("strCheckReq", strCheckRequest)
        bundle.putBoolean("isFromCheckRequest", true)
        val ratingDialog = supportFragmentManager!!.findFragmentByTag("ratingDialog");
        if (ratingDialog != null && ratingDialog.isVisible()) {
            //DO STUFF
        } else {
            //Whatever
            if (!ratingPage.isShown()) {
                ratingPage.arguments = bundle
                ratingPage.show(supportFragmentManager, "ratingDialog")
            }
        }

    }

    override fun showPicturePreview(isFront: Boolean) {
        val dialogUploadPicture = DialogUploadPicture()
        val bundle = Bundle()
        bundle.putBoolean("isFront", isFront)
        dialogUploadPicture.arguments = bundle
        dialogUploadPicture.show(supportFragmentManager, "takepicture")
    }

    fun setUserImage(strUrl: String) {
        Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder))
                .load(strUrl)
                .into(mBinding.llBottomService.ivXuperUSer)
    }

    override fun updateService(view: View) {
        when (view.id) {
            R.id.tvAllow -> {
                when (mBinding.llBottomService.llConfirm.tvAllow.text) {

                    ARRIVED -> {
                        edtXuperOtp.visibility = View.VISIBLE
                        mViewModel.updateRequest(ARRIVED, null, false)
                    }

                    START -> when {
                        frontImgFile == null -> ViewUtils.showToast(this, resources.getString(R.string.empty_front_image), false)
                        mViewModel.otp.value.isNullOrEmpty() ->
                            ViewUtils.showToast(this, resources.getString(R.string.empty_otp), false)
                        else -> {
                            frontImgMultiPart = getImageMultiPart(frontImgFile!!, true)
                            mViewModel.updateRequest(PICKED_UP, frontImgMultiPart, true)
                        }
                    }

                    COMPLETED -> {
                        if (backImgFile == null) ViewUtils.showToast(this, resources.getString(R.string.empty_back_image), false) else {
                            backImgMultiPart = getImageMultiPart(backImgFile!!, false)
                            mViewModel.updateRequest(DROPPED, backImgMultiPart, false)
                        }
                    }
                }
            }

            R.id.tvCancel -> XUberCancelReasonFragment().show(supportFragmentManager, "XUberCancelReasonFragment")
        }
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        val time = Date().time - chronometer!!.base
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
        val formatedTime = (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
        Log.e("Chrono", "------$h---$m---$s")
        chronometer.text = formatedTime
    }

    @SuppressLint("SetTextI18n")
    fun startTheTimer() {
        if (!(mViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.started_at.isNullOrEmpty())) {
            val startedTime = mViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.started_at
            if (!startedTime.isNullOrEmpty()) {
                localServiceTime = CommonMethods.getLocalTime(startedTime, "yyyy-dd-MM HH:mm:ss")
                val timeinMilliSec = localServiceTime
                val timeinMilli = Date().time - (timeinMilliSec!!)
                val h = (timeinMilli / 3600000).toInt()
                val m = (timeinMilli - h * 3600000).toInt() / 60000
                val s = (timeinMilli - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
                val formattedTime = (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
                cmXuberServiceTime.base = localServiceTime!!
                cmXuberServiceTime.text = "00:00:00"
                cmXuberServiceTime.start()
                cmXuberServiceTime.onChronometerTickListener = this@XuberDashBoardActivity
            }
        } else {
            cmXuberServiceTime.base = SystemClock.elapsedRealtime()
            cmXuberServiceTime.text = "00:00:00"
            cmXuberServiceTime.start()
        }
    }

    override fun getFilePath(filePath: Uri) {
        if ((mViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.status.equals(ARRIVED)
                        && mBinding.llBottomService.llConfirm.tvAllow.text != COMPLETED
                        || mViewModel.xuperUdpateRequest.value!!.responseData!!.status.equals(ACCEPTED)))
            getImageFile(true, filePath)
        else if (mViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.status.equals(PICKED_UP)
                || mBinding.llBottomService.llConfirm.tvAllow.text == COMPLETED) getImageFile(false, filePath)
    }

    private fun getImageMultiPart(file: File, isFrontImage: Boolean): MultipartBody.Part {
        val fileBody: MultipartBody.Part
        val requestFile = RequestBody.create(MediaType.parse("*/*"), file)
        if (isFrontImage)
            fileBody = MultipartBody.Part.createFormData("before_picture", file.name, requestFile)
        else
            fileBody = MultipartBody.Part.createFormData("after_picture", file.name, requestFile)

        return fileBody
    }

    private fun getImageFile(isFront: Boolean, fileUri: Uri) {
        if (isFront) frontImgFile = File(fileUri.path)
        else backImgFile = File(fileUri.path)
    }

    override fun reasonForCancel(reason: String) {
        if (reason.isNotEmpty()) {
            val params = HashMap<String, String>()
            val id = mViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.id.toString()
            params[Constants.Common.ID] = id
            params[Constants.Common.SERVICEID] = "3"
            params[CANCEL] = reason
            mViewModel.cancelRequest(params)
        }
    }

    private fun showInfoWindow(context: Context, v: View, allowDescription: String?, allowImage: String?) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = layoutInflater.inflate(R.layout.dialog_info_window, null)
        var ivClose = popupView!!.ivClose
        var ivDesImage = popupView!!.ivInfo
        var tvDescription = popupView!!.tv_description
        ivClose.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //Your code here
                popupWindow!!.dismiss()
            }
        })


        if (!allowImage.isNullOrEmpty()) {
            Glide.with(this)
                    .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .error(R.drawable.ic_profile_placeholder))
                    .load(allowImage)
                    .into(ivDesImage)
        }



        tvDescription.setText(allowDescription)
        val displayFrame = Rect()
        v.getWindowVisibleDisplayFrame(displayFrame)
        val displayFrameWidth = displayFrame.right - displayFrame.left
        val loc = IntArray(2)
        v.getLocationInWindow(loc)
        //X and Y co-ordinate position to show the dropdown
        var xoff = 0
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
            xoff = displayFrameWidth - margin - popupWindow!!.getWidth() - loc[0]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow!!.setElevation(20f)
            }
            popupWindow!!.isOutsideTouchable = false
            popupWindow!!.showAtLocation(v, Gravity.RIGHT, xoff, y.toInt() + 200)
            popupWindow!!.setAnimationStyle(R.anim.popup_anim_in)
        }

        popupWindow!!.setOnDismissListener {
            if (popupWindow != null) popupWindow = null
        }
    }

    override fun showInfoWindow(view: View) {
        showInfoWindow(this, mBinding.llBottomService.ibInstruction, mViewModel.strDesc.value.toString(), mViewModel.descImage.value.toString())
    }


    override fun onClick(v: View?) {
        if ((mViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.status.equals(ARRIVED)
                        && mBinding.llBottomService.llConfirm.tvAllow.text != COMPLETED
                        || mViewModel.xuperUdpateRequest.value!!.responseData!!.status.equals(ACCEPTED)))
            isFront = true
        else if (mViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.status.equals(PICKED_UP)
                || mBinding.llBottomService.llConfirm.tvAllow.text == COMPLETED) isFront = false
        val dialogUploadPicture = DialogUploadPicture()
        val bundle = Bundle()
        bundle.putBoolean("isFront", isFront)
        dialogUploadPicture.arguments = bundle
        dialogUploadPicture.show(supportFragmentManager, "takepicture")
    }
}
