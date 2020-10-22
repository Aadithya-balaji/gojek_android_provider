package com.bee.courierservice.xuberMainActivity

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
import com.gox.base.data.Constants.ModuleTypes.DELIVERY
import com.gox.base.data.Constants.RideStatus.ACCEPTED
import com.gox.base.data.Constants.RideStatus.ARRIVED
import com.gox.base.data.Constants.RideStatus.COMPLETED
import com.gox.base.data.Constants.RideStatus.DROPPED
import com.gox.base.data.Constants.RideStatus.PICKED_UP
import com.gox.base.data.Constants.RideStatus.STARTED
import com.gox.base.data.Constants.XUberProvider.CANCEL
import com.gox.base.data.Constants.XUberProvider.START
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.writePreferences
import com.gox.base.location_service.BaseLocationService
import com.gox.base.socket.SocketListener
import com.gox.base.socket.SocketManager
import com.gox.base.utils.*
import com.gox.base.utils.LocationUtils.getCurrentAddress
import com.gox.base.utils.distanceCalc.DistanceProcessing
import com.gox.base.utils.distanceCalc.DistanceUtils
import com.gox.base.utils.polyline.DirectionUtils
import com.gox.base.utils.polyline.PolyLineListener
import com.gox.base.utils.polyline.PolylineUtil
import com.bee.courierservice.R
import com.bee.courierservice.databinding.ActivityCourierMainBinding
import com.bee.courierservice.extracharge.CourierExtraChargeDialog
import com.bee.courierservice.interfaces.GetExtraChargeInterface
import com.bee.courierservice.interfaces.GetFilePathInterface
import com.bee.courierservice.interfaces.GetReasonsInterface
import com.bee.courierservice.invoice.CourierInvoiceDialog
import com.bee.courierservice.model.CancelRequestModel
import com.bee.courierservice.model.UpdateRequest
import com.bee.courierservice.model.CourierCheckRequest
import com.bee.courierservice.rating.DialogCourierRating
import com.bee.courierservice.reasons.CourierCancelReasonFragment
import com.bee.courierservice.uploadImage.UploadPictureDialog
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.readPreferences
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_courier_main.*
import kotlinx.android.synthetic.main.c_bottom_service_status_sheet.*
import kotlinx.android.synthetic.main.c_dialog_info_window.view.*
import kotlinx.android.synthetic.main.c_layout_status_indicators.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import java.io.File
import java.util.*
import kotlin.concurrent.schedule


class CourierDashBoardActivity : BaseActivity<ActivityCourierMainBinding>(),
        CourierDashBoardNavigator,
        OnMapReadyCallback,
        Chronometer.OnChronometerTickListener,
        GetFilePathInterface,
        GetReasonsInterface,
        PolyLineListener,
        View.OnClickListener {

    private lateinit var mViewModel: CourierDashboardViewModel
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mBinding: ActivityCourierMainBinding
    private lateinit var context: Context

    private val invoicePage = CourierInvoiceDialog()
    private val ratingPage = DialogCourierRating()

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
    private var currentLat: Double = 0.0
    private var currentlng: Double = 0.0
    private var providerMarker: Marker? = null
    var deliveryPosistion:Int = 0
    private var locationUpdate:Int = 0;

    override fun getLayoutId() = R.layout.activity_courier_main

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        currentStatus = ""
        mBinding = mViewDataBinding as ActivityCourierMainBinding
        mViewModel = CourierDashboardViewModel()
        context = this
        checkRequestTimer = Timer()
        mViewModel.navigator = this
        mBinding.xuberViewModel = mViewModel
        mBinding.lifecycleOwner = this
        mViewModel.latitude.value = 0.0
        mViewModel.longitude.value = 0.0

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
        mViewModel.callXUberCheckRequest()
        initialiseMap()
        getApiResponse()
        mBinding.llBottomService.bCourierStart.setOnClickListener {
            mViewModel.updateRequest(PICKED_UP, null, false, "", "",deliveryPosistion)
        }
        mBinding.llBottomService.bCourierArrived.setOnClickListener {
            mViewModel.updateRequest(ARRIVED, null, false, "", "",deliveryPosistion)
        }
        mBinding.llBottomService.bCourierCompleted.setOnClickListener {
            mViewModel.updateRequest(COMPLETED, null, false, "", "",deliveryPosistion)
        }
        mBinding.llBottomService.bCourierDroped.setOnClickListener {
            mViewModel.updateRequest(DROPPED, null, false, "", "",deliveryPosistion) }
    }

    fun getApiResponse() {
        mViewModel.xUberCheckRequest.observe(this, Observer<CourierCheckRequest> { xUberCheckRequest ->
            try {
                if (xUberCheckRequest!!.responseData!!.request != null) {
                              deliveryPosistion = 0
                    val mainstatus = xUberCheckRequest.responseData!!.request.status
                    if (mainstatus != mViewModel.mainCurrentStatus.value && !mainstatus.equals(COMPLETED,true)) {
                        val delivery = xUberCheckRequest.responseData!!.request!!.delivery
                        mViewModel.mainCurrentStatus.value = mainstatus

                            currentLat = xUberCheckRequest.responseData.request.s_latitude!!
                            currentlng = xUberCheckRequest.responseData.request.s_longitude!!
                            if (xUberCheckRequest.responseData.request.s_address.length > 2)
                                mBinding.tvXuberPickupLocation.text = xUberCheckRequest.responseData.request.s_address
                            else {
                                val lat = xUberCheckRequest.responseData.request.s_latitude
                                val lon = xUberCheckRequest.responseData.request.s_longitude
                                val latLng: LatLng?
                                latLng = LatLng(lat!!, lon!!)
                                val address = getCurrentAddress(this, latLng)
                                if (address.isNotEmpty()) mBinding.tvXuberPickupLocation.text = address[0].getAddressLine(0)
                            }


                            if (delivery.d_address!!.length > 2)
                                mBinding.llBottomService.tvDropLocation.text = delivery.d_address
                            else {
                                val lat = delivery.d_latitude
                                val lon = delivery.d_longitude
                                val latLng: LatLng?
                                latLng = LatLng(lat!!, lon!!)
                                val address = getCurrentAddress(this, latLng)
                                if (address.isNotEmpty()) mBinding.llBottomService.tvDropLocation.text = address[0].getAddressLine(0)
                            }

                            mViewModel.userName.value = xUberCheckRequest.responseData!!.request!!.user!!.first_name +
                                    " " + xUberCheckRequest.responseData.request!!.user!!.last_name!!
                            mBinding.llBottomService.receiverData.setText(delivery.name+"( "+delivery.mobile+" )")
                            mViewModel.descImage.value = delivery.picture.toString()
                            mViewModel.strDesc.value = delivery.instruction
                            mViewModel.weight.value = delivery.weight.toString()
                            mViewModel.height.value = delivery.height.toString()
                            mViewModel.width.value = delivery.breadth.toString()
                            mViewModel.length.value = delivery.length.toString()
                            mViewModel.userRating.value = String.format(resources.getString(R.string.xuper_rating_user),
                                    xUberCheckRequest.responseData.request.user!!.rating!!.toDouble())
                            if (!xUberCheckRequest.responseData.request.user.picture.isNullOrEmpty())
                                setUserImage(xUberCheckRequest.responseData.request.user.picture.toString())
                            else setUserImage("")
                            if (!roomConnected) {
                                reqID = xUberCheckRequest.responseData.request!!.id!!
                                PreferencesHelper.put(PreferencesKey.DELIVERY_REQ_ID, reqID)
                                if(reqID!=0){
                                    SocketManager.emit(Constants.RoomName.DELIVERY_ROOM_NAME, Constants.RoomId.getDeliveryRoom(reqID))
                                }
                            }
                            fab_xuber_menu_call.setOnClickListener {
                                val intent = Intent(Intent.ACTION_DIAL)
                                intent.data = Uri.parse("tel:${xUberCheckRequest.responseData.request.user.mobile}")
                                startActivity(intent)
                            }
                            writePreferences(Constants.Chat.ADMIN_SERVICE, DELIVERY)
                            writePreferences(Constants.Chat.USER_ID, xUberCheckRequest.responseData.request.user.id)
                            writePreferences(Constants.Chat.REQUEST_ID, xUberCheckRequest.responseData.request.id)
                            writePreferences(Constants.Chat.PROVIDER_ID, delivery.provider_id)
                            writePreferences(Constants.Chat.USER_NAME, xUberCheckRequest.responseData.request.user.first_name
                                    + " " + xUberCheckRequest.responseData.request.user.last_name)
                            writePreferences(Constants.Chat.PROVIDER_NAME, xUberCheckRequest.responseData.provider_details.first_name
                                    + " " + xUberCheckRequest.responseData.provider_details.last_name)
//                            drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!),
//                                    LatLng(delivery.d_latitude!!,delivery.d_longitude!!))
                        when (xUberCheckRequest!!.responseData!!.request.status) {
                            COMPLETED -> {
                                indicatorView(COMPLETED)
                            }
                            DROPPED -> {
                                indicatorView(COMPLETED)
                            }
                            PICKED_UP -> {
                                indicatorView(PICKED_UP)
                                llBottomService.visibility = View.VISIBLE
                            }
                            ARRIVED ->{
                                indicatorView(ARRIVED)
                                whenArrived(xUberCheckRequest)
                            }
                            STARTED -> {
                                indicatorView(STARTED)
                                whenAccepted(xUberCheckRequest!!.responseData!!.request.s_latitude!!,xUberCheckRequest!!.responseData!!.request.s_longitude!!)
                            }
                        }
                    }

                    if(xUberCheckRequest!!.responseData!!.request.status.equals(COMPLETED,true)) {
                        if (xUberCheckRequest!!.responseData!!.request.paid == 0) {
                            showInvoice(false)
                        } else {
                            whenPayment()
                        }
                    }

//                       deliveryPosistion = deliveryPosistion + 1
                       if (mainstatus == PICKED_UP) {
                           val status = xUberCheckRequest.let {xUberCheckRequest.responseData!!.request!!.delivery.status}
                           if(xUberCheckRequest.responseData!!.request!!.delivery != null) {
                               mBinding.llBottomService.receiverData.setText(xUberCheckRequest.responseData!!.request!!.delivery.name+"( "+xUberCheckRequest.responseData!!.request!!.delivery.mobile+" )")
                               if (xUberCheckRequest.responseData!!.request!!.delivery.d_address!!.length > 2)
                                   mBinding.llBottomService.tvDropLocation.text = xUberCheckRequest.responseData!!.request!!.delivery.d_address

                               mViewModel.descImage.value = xUberCheckRequest.responseData!!.request!!.delivery.picture.toString()
                               mViewModel.strDesc.value = xUberCheckRequest.responseData!!.request!!.delivery.instruction
                               mViewModel.weight.value = xUberCheckRequest.responseData!!.request!!.delivery.weight.toString()
                               mViewModel.height.value = xUberCheckRequest.responseData!!.request!!.delivery.height.toString()
                               mViewModel.width.value = xUberCheckRequest.responseData!!.request!!.delivery.breadth.toString()
                               mViewModel.length.value = xUberCheckRequest.responseData!!.request!!.delivery.length.toString()
//                               if(locationUpdate == 0) {
                                   drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!), LatLng(xUberCheckRequest.responseData!!.request!!.delivery.d_latitude!!, xUberCheckRequest.responseData!!.request!!.delivery.d_longitude!!))
//                                   locationUpdate = 1
//                               }
                           }
                           if (status != mViewModel.currentStatus.value) {
                               mViewModel.currentStatus.value = xUberCheckRequest.let { xUberCheckRequest.responseData!!.request!!.delivery.status }
                               mBinding.llBottomService.tvServiceType.setText("")
                               when (status) {
                                   STARTED -> {
                                       whenStarted()
                                       llBottomService.visibility = View.VISIBLE
                                       indicatorView(PICKED_UP)
                                   }
                                   DROPPED -> {
                                       indicatorView(COMPLETED)
                                       llBottomService.visibility = View.VISIBLE
                                       whenDropped_c(xUberCheckRequest)
                            }
                        }
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

        mViewModel.xUberUpdateRequest.observe(this, Observer<CourierCheckRequest> { courierCheckRequest ->
            if (courierCheckRequest!!.statusCode.equals("200")) {
                loadingObservable.value = false
            }
        })

        mViewModel.xUberCancelRequest.observe(this, Observer<CancelRequestModel> {
            loadingObservable.value = false
            ViewUtils.showToast(this, resources.getString(R.string.request_canceled), true)
            finish()
        })

        SocketManager.onEvent(Constants.RoomName.STATUS, Emitter.Listener {
            if(it[0].toString().contains(Constants.ModuleTypes.DELIVERY)){
                roomConnected = true
            }
        })

        SocketManager.onEvent(Constants.RoomName.DELIVERY_REQ, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK delivery request " + it[0])
            if(it[0].toString().contains("payment_mode")){
                val data = it[0] as JSONObject
                paymentMode = data.getString("payment_mode")
                mViewModel.currentStatus.value = ""
            }
            mViewModel.callXUberCheckRequest()
        })

        SocketManager.setOnSocketRefreshListener(object : SocketListener.ConnectionRefreshCallBack {
            override fun onRefresh() {
                SocketManager.emit(Constants.RoomName.DELIVERY_ROOM_NAME, Constants.RoomId.getDeliveryRoom(reqID))
            }
        })
    }

    private fun initialiseMap() {
        fragmentMap = supportFragmentManager.findFragmentById(R.id.fragmentMap) as SupportMapFragment
        fragmentMap.getMapAsync(this)
    }

    private fun indicatorView(status: String) {
        when(status){
            COMPLETED->{
                mBinding.llBottomService.indicatorCourier.indicator_line_one.setBackgroundColor(resources.getColor(R.color.black))
                mBinding.llBottomService.indicatorCourier.indicator_line_two.setBackgroundColor(resources.getColor(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_location_pin.setBackgroundTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_steering.setBackgroundTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_flag.setBackgroundTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_flag.setImageTintList(resources.getColorStateList(R.color.white))
                mBinding.llBottomService.indicatorCourier.ib_location_pin.setImageTintList(resources.getColorStateList(R.color.white))
                mBinding.llBottomService.indicatorCourier.ib_steering.setImageTintList(resources.getColorStateList(R.color.white))
            }
            PICKED_UP->{
                mBinding.llBottomService.indicatorCourier.indicator_line_one.setBackgroundColor(resources.getColor(R.color.black))
                mBinding.llBottomService.indicatorCourier.indicator_line_two.setBackgroundColor(resources.getColor(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.ib_location_pin.setBackgroundTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_steering.setBackgroundTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_flag.setBackgroundTintList(resources.getColorStateList(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.ib_flag.setImageTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_location_pin.setImageTintList(resources.getColorStateList(R.color.white))
                mBinding.llBottomService.indicatorCourier.ib_steering.setImageTintList(resources.getColorStateList(R.color.white))
            }
            ARRIVED->{
                mBinding.llBottomService.indicatorCourier.indicator_line_one.setBackgroundColor(resources.getColor(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.indicator_line_two.setBackgroundColor(resources.getColor(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.ib_location_pin.setBackgroundTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_steering.setBackgroundTintList(resources.getColorStateList(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.ib_flag.setBackgroundTintList(resources.getColorStateList(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.ib_flag.setImageTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_location_pin.setImageTintList(resources.getColorStateList(R.color.white))
                mBinding.llBottomService.indicatorCourier.ib_steering.setImageTintList(resources.getColorStateList(R.color.black))
            }
            STARTED->{
                mBinding.llBottomService.indicatorCourier.indicator_line_one.setBackgroundColor(resources.getColor(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.indicator_line_two.setBackgroundColor(resources.getColor(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.ib_location_pin.setBackgroundTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_steering.setBackgroundTintList(resources.getColorStateList(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.ib_flag.setBackgroundTintList(resources.getColorStateList(R.color.xuper_extra_amt_bg))
                mBinding.llBottomService.indicatorCourier.ib_flag.setImageTintList(resources.getColorStateList(R.color.black))
                mBinding.llBottomService.indicatorCourier.ib_location_pin.setImageTintList(resources.getColorStateList(R.color.white))
                mBinding.llBottomService.indicatorCourier.ib_steering.setImageTintList(resources.getColorStateList(R.color.black))            }
        }

    }

    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = true) {
        providerMarker?.remove()
        try {
            providerMarker = mGoogleMap?.addMarker(MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap
            (bitmapFromVector(BaseApplication.getBaseApplicationContext, R.drawable.ic_marker_provider))))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (!isAnimateMap) mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM))
        else mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, Constants.DEFAULT_ZOOM))
    }

    private fun whenDropped_c(xUberCheckRequest:CourierCheckRequest){
        if (xUberCheckRequest!!.responseData!!.request.payment_by.equals("RECEIVER",true) && xUberCheckRequest!!.responseData!!.request.paid == 0) {
            showInvoice(false)
        }else{
        mBinding.llBottomService.bCourierArrived.visibility = View.GONE
        mBinding.llBottomService.bCourierStart.visibility = View.GONE
        mBinding.llBottomService.bCourierCompleted.visibility = View.VISIBLE
        mBinding.llBottomService.bCourierDroped.visibility = View.GONE
        }
    }

    private fun whenDropped(isCheckRequest: Boolean) {
        fab_xuber_menu.visibility = View.GONE
        val bundle = Bundle()
        var currentPaymentMode = ""

        if (isCheckRequest) {
            val strCheckRequest = Gson().toJson(mViewModel.xUberCheckRequest.value!!)
            bundle.putString("strCheckReq", strCheckRequest)
            bundle.putBoolean("fromCheckReq", true)
//            cmcourierserviceTime.stop()
            currentPaymentMode = mViewModel.xUberCheckRequest.value?.responseData?.request?.payment_mode!!
            if(paymentMode.equals(""))
                paymentMode = currentPaymentMode
        } else {
            val strUpdateRequest = Gson().toJson(mViewModel.xUberUpdateRequest.value!!)
            bundle.putString("strUpdateReq", strUpdateRequest)
            bundle.putBoolean("fromCheckReq", false)
//            cmcourierserviceTime.stop()
            if(paymentMode.equals(""))
                paymentMode = currentPaymentMode
        }

        if (!paymentMode.equals(currentPaymentMode)) {
            paymentMode = currentPaymentMode
//            showInvoice(bundle, true)
        }
//        else showInvoice(bundle, false)

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

    private fun showInvoice(update:Boolean) {
        llBottomService.visibility = View.GONE
        val bundle = Bundle()
        val strCheckRequest = Gson().toJson(mViewModel.xUberCheckRequest.value)
        bundle.putString("strCheckReq", strCheckRequest)
        bundle.putBoolean("isFromCheckRequest", true)
        invoicePage.arguments = bundle
        if (update)
            invoicePage.dismiss()
        if (!invoicePage.isShown()) invoicePage.show(supportFragmentManager, "courierinvoice")
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
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRRR:: XuberDashboardActivity")
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            val isGpsEnabled = intent.getBooleanExtra("ISGPS_EXITS", false)

            if (isGpsEnabled) updateMap(location)
            else if (!isLocationDialogShown) {
                isLocationDialogShown = true
                CommonMethods.checkGps(context)
            }
        }
    }

//    private fun updateMap(location: Location) {
//        mViewModel.latitude.value = location.latitude
//        mViewModel.longitude.value = location.longitude
//
////        if (checkStatusApiCounter++ % 2 == 0) mViewModel.callXUberCheckRequest()
//
//        if (roomConnected) {
//            val locationObj = JSONObject()
//            locationObj.put("latitude", location.latitude)
//            locationObj.put("longitude", location.longitude)
//            locationObj.put("room", Constants.RoomId.getDeliveryRoom(reqID))
//                   SocketManager.emit("send_location", locationObj)
//                    Log.e("SOCKET", "SOCKET_SK Location update delivery called")
//        }
//
//        if (BaseApplication.getCustomPreference!!.getBoolean(PreferencesKey.SERVICE_OTP, false)) {
//            if (startLatLng.latitude != 0.0) endLatLng = startLatLng
//            startLatLng = LatLng(location.latitude, location.longitude)
//
//            if (endLatLng.latitude != 0.0 && polyLine.size > 0) try {
//                CarMarkerAnimUtil().carAnim(srcMarker!!, endLatLng, startLatLng)
//                polyLineRerouting(endLatLng, polyLine)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }

//    private fun polyLineRerouting(point: LatLng, polyLine: ArrayList<LatLng>) {
//        val index = polyUtil.locationIndexOnEdgeOrPath(point, polyLine, false, true, 50.0)
//        if (index >= 0) {
//            polyLine.subList(0, index + 1).clear()
////            polyLine.add(0, point)
//            mPolyline!!.remove()
//            val options = PolylineOptions()
//            options.addAll(polyLine)
//            mPolyline = mGoogleMap!!.addPolyline(options.width(5f).color
//            (ContextCompat.getColor(baseContext, R.color.colorBlack)))
//            println("RRR mPolyline = " + polyLine.size)
//        } else {
//            canDrawPolyLine = true
//            drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!), mViewModel.polyLineDest.value!!)
//        }
//    }

    private fun drawRoute(src: LatLng, dest: LatLng) {
        mViewModel.tempSrc.value = src
        mViewModel.tempDest.value = dest
        if (canDrawPolyLine) {
            canDrawPolyLine = false
            Handler().postDelayed({ canDrawPolyLine = true }, 10000)
            PolylineUtil(this).execute(DirectionUtils().getDirectionsUrl(src, dest,
                    getText(R.string.google_map_key).toString()))
        }
        mViewModel.polyLineSrc.value = src
        mViewModel.polyLineDest.value = dest
    }


    private fun updateMap(location: Location) {
        mViewModel.latitude.value = location.latitude
        mViewModel.longitude.value = location.longitude
        if (mViewModel.latitude.value!! == 0.0 && mViewModel.longitude.value!! == 0.0)
            updateCurrentLocation()
        println("RRRR :: TaxiDashboardActivity " + mViewModel.latitude.value + " :: " + mViewModel.longitude.value)

        if (roomConnected) {
            val locationObj = JSONObject()
            locationObj.put("latitude", location.latitude)
            locationObj.put("longitude", location.longitude)
            locationObj.put("provider_id", mViewModel.xUberCheckRequest.value!!.responseData.provider_details.id)
            locationObj.put("room", Constants.RoomId.getDeliveryRoom(reqID))
            SocketManager.emit("send_location", locationObj)
            Log.e("SOCKET", "SOCKET_SK Location update called "+Gson().toJson(locationObj))
        }

//        if (!BuildConfig.isSocketEnabled) if (checkStatusApiCounter++ % 3 == 0) mViewModel.callTaxiCheckStatusAPI()

        if (startLatLng.latitude != 0.0) endLatLng = startLatLng
        startLatLng = LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!)

        println("RRRR :: TaxiDashboardActivity LatLng(location = " +
                "${LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!)}")

        println("RRRR :: TaxiDashboardActivity LatLng(location = " +
                "${polyLine.size}")

        if (mViewModel.latitude.value!! != 0.0 && endLatLng.latitude != 0.0 && polyLine.size > 0) {
            try {
                CarMarkerAnimUtil().carAnimWithBearing(srcMarker!!, endLatLng, startLatLng)
                polyLineRerouting(endLatLng, polyLine)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else if (mViewModel.latitude.value!! != 0.0 && polyLine.size == 0) try {

            drawRoute(LatLng(mViewModel.latitude.value?:0.0, mViewModel.longitude.value?:0.0),
                    mViewModel.polyLineDest.value?:LatLng(0.0,0.0))

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

            CarMarkerAnimUtil().carAnim(srcMarker!!, polyLine[0], polyLine[1])

            mGoogleMap!!.addMarker(MarkerOptions().position(polyLine[polyLine.size - 1]).icon
            (BitmapDescriptorFactory.fromBitmap(bitmapFromVector(baseContext, R.drawable.ic_marker_stop))))

        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Map", "------------" + e.message.toString())
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

        println("RRR whenFail = $statusCode")
        when (statusCode) {
//            "NOT_FOUND" -> showLog(getString(R.string.NoRoadMapAvailable))
//            "ZERO_RESULTS" -> showLog(getString(R.string.NoRoadMapAvailable))
            "MAX_WAYPOINTS_EXCEEDED" -> showLog(getString(R.string.WayPointLlimitExceeded))
            "MAX_ROUTE_LENGTH_EXCEEDED" -> showLog(getString(R.string.RoadMapLimitExceeded))
            "INVALID_REQUEST" -> showLog(getString(R.string.InvalidInputs))
            "OVER_DAILY_LIMIT" -> showLog(getString(R.string.MayBeInvalidAPIBillingPendingMethodDeprecated))
            "OVER_QUERY_LIMIT" -> showLog(getString(R.string.TooManyRequestlimitExceeded))
            "REQUEST_DENIED" -> showLog(getString(R.string.DirectionsServiceNotEnabled))
            "UNKNOWN_ERROR" -> showLog(getString(R.string.ServerError))
            else -> {
                when(statusCode){
                    "NOT_FOUND" -> {}
                    "ZERO_RESULTS" -> {}
                    else ->showLog(statusCode)
                }
            }
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

    private fun whenAccepted(lat:Double,long:Double) {
        mBinding.llBottomService.fbCamera.visibility = View.GONE
        mBinding.llBottomService.tvCancel.visibility = View.VISIBLE
        mBinding.llBottomService.bCourierStart.visibility = View.GONE
        mBinding.llBottomService.bCourierArrived.visibility = View.VISIBLE
        mBinding.llBottomService.bCourierCompleted.visibility = View.GONE
        mBinding.llBottomService.bCourierDroped.visibility = View.GONE
        mBinding.llBottomService.tvAllow.text = ARRIVED
        mBinding.llBottomService.tvCancel.text = CANCEL
        drawRoute(LatLng(mViewModel.latitude.value!!, mViewModel.longitude.value!!), LatLng(lat,long))
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, true)
    }
    private fun whenArrived(xUberCheckRequest:CourierCheckRequest) {
        if (xUberCheckRequest!!.responseData!!.request.payment_by.equals("SENDER",true) && xUberCheckRequest!!.responseData!!.request.paid == 0) {
            showInvoice(false)
        }else{
            mBinding.llBottomService.fbCamera.visibility = View.GONE
            mBinding.llBottomService.tvCancel.visibility = View.VISIBLE
            mBinding.llBottomService.bCourierStart.visibility = View.VISIBLE
            mBinding.llBottomService.bCourierArrived.visibility = View.GONE
            mBinding.llBottomService.bCourierCompleted.visibility = View.GONE
            mBinding.llBottomService.bCourierDroped.visibility = View.GONE
            mBinding.llBottomService.tvAllow.text = ARRIVED
            mBinding.llBottomService.tvCancel.text = CANCEL
            writePreferences(PreferencesKey.CAN_SEND_LOCATION, true)
        }

    }


    private fun whenStarted() {
        fab_xuber_menu.visibility = View.GONE
        mGoogleMap!!.clear()
        edtXuperOtp.visibility = View.GONE
        mBinding.llBottomService.tvCancel.visibility = View.GONE
        mBinding.llBottomService.tvAllow.text = COMPLETED
        mBinding.llBottomService.bCourierArrived.visibility = View.GONE
        mBinding.llBottomService.bCourierStart.visibility = View.GONE
        mBinding.llBottomService.bCourierCompleted.visibility = View.GONE
        mBinding.llBottomService.bCourierDroped.visibility = View.VISIBLE
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
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
            R.id.tvAllow ->{

            }


            R.id.tvCancel -> CourierCancelReasonFragment().show(supportFragmentManager, "XUberCancelReasonFragment")
        }
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


    override fun getFilePath(filePath: Uri) {
        val currentStatus = mViewModel.currentStatus.value?:""
        val isFront = (currentStatus.equals(ARRIVED,true) || currentStatus.equals(ACCEPTED,true))
            getImageFile(isFront, filePath)
        if(!isFront)
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

    }

    private fun showInfoWindow(context: Context, v: View, allowDescription: String?, allowImage: String?) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        popupView = layoutInflater.inflate(R.layout.c_dialog_info_window, null)
        val ivClose = popupView!!.ivClose
        val ivDesImage = popupView!!.ivInfo
        val tvDescription = popupView!!.tv_description
        ivClose.setOnClickListener { popupWindow!!.dismiss() }

        popupView!!.package_details.visibility = View.VISIBLE
        popupView!!.line_package.visibility = View.VISIBLE

        popupView!!.package_weight.text = mViewModel.weight.value + " kg"
        popupView!!.package_height.text = mViewModel.height.value + " cm"
        popupView!!.package_width.text = mViewModel.width.value + " cm"
        popupView!!.package_length.text = mViewModel.length.value + " cm"


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
        val currentStatus = mViewModel.currentStatus.value?:""
        if (currentStatus.isNotEmpty()) {
            isFront = (currentStatus.equals(ARRIVED,true) || currentStatus.equals(ACCEPTED,true))
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