package com.xjek.xuberservice.xuberMainActivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Chronometer
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
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
import com.xjek.base.data.Constants
import com.xjek.base.data.Constants.RideStatus.ACCEPTED
import com.xjek.base.data.Constants.RideStatus.ARRIVED
import com.xjek.base.data.Constants.RideStatus.COMPLETED
import com.xjek.base.data.Constants.RideStatus.DROPPED
import com.xjek.base.data.Constants.RideStatus.PICKED_UP
import com.xjek.base.data.Constants.XuperProvider.CANCEL
import com.xjek.base.data.Constants.XuperProvider.START
import com.xjek.base.location_service.BaseLocationService
import com.xjek.base.utils.CommonMethods
import com.xjek.base.utils.Constants.Companion.UTCTIME
import com.xjek.base.utils.ViewUtils
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.ActivityXubermainBinding
import com.xjek.xuberservice.interfaces.GetFilePathInterface
import com.xjek.xuberservice.interfaces.GetReasonsInterface
import com.xjek.xuberservice.invoice.DialogXuperInvoice
import com.xjek.xuberservice.model.CancelRequestModel
import com.xjek.xuberservice.model.UpdateRequest
import com.xjek.xuberservice.model.XuperCheckRequest
import com.xjek.xuberservice.rating.DialogXuperRating
import com.xjek.xuberservice.reasons.ReasonFragment
import com.xjek.xuberservice.uploadImage.DialogUploadPicture
import kotlinx.android.synthetic.main.bottom_service_status_sheet.*
import kotlinx.android.synthetic.main.bottom_service_status_sheet.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class XuberMainActivity : BaseActivity<ActivityXubermainBinding>(), XuberMainNavigator, OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener, Chronometer.OnChronometerTickListener, GetFilePathInterface, GetReasonsInterface {

    private lateinit var xuberMainViewModel: XuberMainViewModel
    private lateinit var fragmentMap: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private var mLastKnownLocation: Location? = null
    private val taxiCheckStatusTimer = Timer()
    private var frontImgFile: File? = null
    private var backImgFile: File? = null
    private var frontImgMutlipart: MultipartBody.Part? = null
    private var backImagMultiPart: MultipartBody.Part? = null
    private lateinit var activityXuberMainBinding: ActivityXubermainBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<FrameLayout>
    private var currentLatitude: Double? = 0.0
    private var currentLontidue: Double? = 0.0
    val invoicePage = DialogXuperInvoice()
    val ratingPage = DialogXuperRating()
    var popupWindow: PopupWindow? = null


    override fun getLayoutId(): Int = R.layout.activity_xubermain
    override fun initView(mViewDataBinding: ViewDataBinding?) {
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BaseLocationService.BROADCAST))
        activityXuberMainBinding = mViewDataBinding as ActivityXubermainBinding
        xuberMainViewModel = XuberMainViewModel()
        xuberMainViewModel.navigator = this
        activityXuberMainBinding.xuberViewModel = xuberMainViewModel
        activityXuberMainBinding.setLifecycleOwner(this)
        xuberMainViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>
        initialiseMap()
        getApiResponse()
        getBundleValues()
        val bundle=Bundle()
        invoicePage.arguments=bundle
        invoicePage.show(supportFragmentManager, "xuperinvoice")

    }

    fun getBundleValues() {
        currentLatitude = if (intent.hasExtra("lat")) intent.getDoubleExtra("lat", 0.0) else 0.0
        currentLontidue = if (intent.hasExtra("lon")) intent.getDoubleExtra("lon", 0.0) else 0.0
    }

    fun getApiResponse() {
        xuberMainViewModel.xuperCheckRequest.observe(this, object : androidx.lifecycle.Observer<XuperCheckRequest> {
            override fun onChanged(xuperCheckRequest: XuperCheckRequest?) {
                if (xuperCheckRequest!!.responseData!!.requests != null) {
                    val status = xuperCheckRequest?.let { it.responseData!!.requests!!.status }
                    var serviceLcoation = xuperCheckRequest?.let { it.responseData!!.requests!!.s_address }
                    activityXuberMainBinding.tvXuberPickupLocation.setText(serviceLcoation)
                    when (status) {
                        ACCEPTED -> {
                            whenAccepted()
                        }
                        ARRIVED -> {
                            whenArrvied()
                        }

                        PICKED_UP -> {
                            whenStarted()
                            startTheTimer()
                        }

                        DROPPED -> {
                            whenDropped(xuperCheckRequest.responseData!!)
                        }

                        COMPLETED -> {
                            whenPayment(xuperCheckRequest.responseData!!)
                        }
                    }

                }
            }
        })


        //Update Request
        xuberMainViewModel.xuperUdpateRequest.observe(this, object : androidx.lifecycle.Observer<UpdateRequest> {
            override fun onChanged(updateRequest: UpdateRequest?) {
                if (updateRequest!!.statusCode.equals("200")) {
                    when (updateRequest.responseData!!.status) {
                        ARRIVED -> {
                            whenArrvied()
                        }

                        PICKED_UP -> {
                            whenStarted()
                            startTheTimer()
                        }

                        DROPPED -> {
                            // xuberMainViewModel.xuperUdpateRequest

                        }

                        COMPLETED -> {

                        }
                    }

                }
            }

        })

        xuberMainViewModel.xuperCancelRequest.observe(this, object : androidx.lifecycle.Observer<CancelRequestModel> {
            override fun onChanged(t: CancelRequestModel?) {
                loadingObservable.value = false
                ViewUtils.showToast(this@XuberMainActivity, resources.getString(R.string.request_canceled), true)
                finish()
            }

        })
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
        xuberMainViewModel.latitude.value = currentLatitude
        xuberMainViewModel.longitude.value = currentLontidue
        updateMapLocation(LatLng(currentLatitude!!, currentLontidue!!))
        Log.e("currentloc", "----" + currentLatitude + "---" + currentLontidue)
        xuberMainViewModel.callXuperCheckRequest()
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
        // LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BaseLocationService.BROADCAST))
    }


    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver)
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRRR:: TaxiDashboardActivity")
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            if (location != null) {
                xuberMainViewModel.latitude.value = location.latitude
                xuberMainViewModel.longitude.value = location.longitude
                xuberMainViewModel.callXuperCheckRequest()
                updateMapLocation(LatLng(location.latitude, location.longitude))
            }
        }
    }

    override fun showErrorMessage(error: String) {
        xuberMainViewModel.showLoading.value = false
        ViewUtils.showToast(this, error, false)
    }

    //When ride accepted
    fun whenAccepted() {
        activityXuberMainBinding.llBottomService.llServiceTime.visibility = View.GONE
        activityXuberMainBinding.llBottomService.llConfirm.tvCancel.visibility = View.VISIBLE
        activityXuberMainBinding.llBottomService.llConfirm.tvAllow.setText(Constants.RideStatus.ARRIVED)
        activityXuberMainBinding.llBottomService.llConfirm.tvCancel.setText(CANCEL)
    }

    //When ride arrived
    fun whenArrvied() {
        activityXuberMainBinding.llBottomService.llServiceTime.visibility = View.GONE
        edtXuperOtp.visibility = View.VISIBLE
        activityXuberMainBinding.llBottomService.llConfirm.tvCancel.visibility = View.GONE
        activityXuberMainBinding.llBottomService.llConfirm.tvAllow.setText(START)
    }

    fun whenStarted() {
        edtXuperOtp.visibility = View.GONE
        activityXuberMainBinding.llBottomService.llConfirm.tvCancel.visibility = View.GONE
        activityXuberMainBinding.llBottomService.llConfirm.tvAllow.setText(Constants.RideStatus.COMPLETED)
    }

    //Completed Not Payment Successful
    fun whenDropped(responseData: XuperCheckRequest.ResponseData) {
        val bundle = Bundle()
        val strCheckRequest = Gson().toJson(xuberMainViewModel.xuperCheckRequest.value!!)
        bundle.putString("strCheckReq", strCheckRequest)
        bundle.putBoolean("fromCheckReq", true)
        invoicePage.arguments = bundle
        if (invoicePage.isShown() == false)
            invoicePage.show(supportFragmentManager, "xuperinvoice")
    }

    //After Payment Successfull
    fun whenPayment(responseData: XuperCheckRequest.ResponseData) {
        val bundle = Bundle()
        val strCheckRequest = Gson().toJson(xuberMainViewModel.xuperCheckRequest.value)
        bundle.putString("strCheckReq", strCheckRequest)
        bundle.putBoolean("isFromCheckRequest", true)
        if (ratingPage.isShown() == false) {
            ratingPage.arguments = bundle
            ratingPage.show(supportFragmentManager, "ratingPage")
        }
    }


    override fun showPicturePreview() {
        val dialogUploadPicture = DialogUploadPicture()
        dialogUploadPicture.show(supportFragmentManager, "takepicture")
    }

    fun setUserImage(strUrl: String) {
        Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder))
                .load(strUrl)
                .into(activityXuberMainBinding.llBottomService.ivXuperUSer)
    }

    override fun updateService(view: View) {
        when (view.id) {
            R.id.tvAllow -> {
                when (activityXuberMainBinding.llBottomService.llConfirm.tvAllow.text) {

                    ARRIVED -> {
                        edtXuperOtp.visibility = View.VISIBLE
                        xuberMainViewModel.updateRequest(Constants.RideStatus.ARRIVED, null, false)
                    }

                    START -> {
                        if (frontImgFile == null) {
                            ViewUtils.showToast(this@XuberMainActivity, resources.getString(R.string.empty_front_image), false)
                        } else if (xuberMainViewModel.otp.value.isNullOrEmpty()) {
                            ViewUtils.showToast(this@XuberMainActivity, resources.getString(R.string.empty_otp), false)
                        } else {
                            frontImgMutlipart = getImageMutiPart(frontImgFile!!, true)
                            xuberMainViewModel.updateRequest(Constants.RideStatus.PICKED_UP, frontImgMutlipart, true)
                        }
                    }

                    COMPLETED -> {
                        if (backImgFile == null) {
                            ViewUtils.showToast(this@XuberMainActivity, resources.getString(R.string.empty_back_image), false)
                        } else {
                            backImagMultiPart = getImageMutiPart(backImgFile!!, false)
                            xuberMainViewModel.updateRequest(Constants.RideStatus.DROPPED, backImagMultiPart, false)
                        }
                    }


                }
            }

            R.id.tvCancel -> {
                val reasonDialogFrag = ReasonFragment()
                reasonDialogFrag.show(supportFragmentManager, "reasonDialog")
            }
        }
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        val time = SystemClock.elapsedRealtime() - chronometer!!.getBase()
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
        val t = (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
        chronometer!!.setText(t)
    }

    fun startTheTimer() {
        val startedTime = xuberMainViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.started_at
        if (!startedTime.isNullOrEmpty()) {
            val serviceDate = CommonMethods.getLocalTime(startedTime!!, UTCTIME)
            val timeinMilliSec = serviceDate
            //  cm_service_time.base = SystemClock.elapsedRealtime() - (timeinMilliSec)
            val h = (timeinMilliSec / 3600000).toInt()
            val m = (timeinMilliSec - h * 3600000).toInt() / 60000
            val s = (timeinMilliSec - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
            val formatedTime = (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            cm_service_time.setText(formatedTime)
            //   cm_service_time.start()
        } else {
            cm_service_time.base = SystemClock.elapsedRealtime()
            cm_service_time.start()
        }
    }

    fun getImageMutiPart(file: File, isFrontImage: Boolean): MultipartBody.Part {
        var fileBody: MultipartBody.Part
        val pictureFile = file
        val requestFile = RequestBody.create(MediaType.parse("*/*"), pictureFile)
        if (isFrontImage)
            fileBody = MultipartBody.Part.createFormData("before_picture", pictureFile.name, requestFile)
        else
            fileBody = MultipartBody.Part.createFormData("after_picture", pictureFile.name, requestFile)

        return fileBody
    }


    fun getImageFile(isFront: Boolean, fileUri: Uri) {
        if (isFront == true)
            frontImgFile = File(fileUri.path)
        else
            backImgFile = File(fileUri.path)
    }

    override fun getFilePath(filePath: Uri) {
        if (xuberMainViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.status.equals(ARRIVED))
            getImageFile(true, filePath)
        else if (xuberMainViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.status.equals(PICKED_UP) || activityXuberMainBinding.llBottomService.llConfirm.tvAllow.text.equals(COMPLETED))
            getImageFile(false, filePath)
    }

    override fun reasonForCancel(reason: String) {
        if (!reason.isNullOrEmpty()) {
            val params = HashMap<String, String>()
            val id = xuberMainViewModel.xuperCheckRequest.value!!.responseData!!.requests!!.id.toString()
            params.put(Constants.Common.ID, id)
            params.put(Constants.Common.SERVICEID, "3")
            params.put(Constants.XuperProvider.CANCEL, reason)
            xuberMainViewModel.cancelRequest(params)
        }
    }

    fun showInfoWindow(context: Context, v: View) {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = layoutInflater.inflate(R.layout.dialog_info_window, null)
        //val drop_down_list = popupView.findViewById(R.id.drop_down_list) as ListView
        val displayFrame = Rect()
        v.getWindowVisibleDisplayFrame(displayFrame)
        val displayFrameWidth = displayFrame.right - displayFrame.left
        val loc = IntArray(2)
        v.getLocationInWindow(loc)
        //X and Y co-ordinate position to show the dropdown
        var xoff = 0
        val yoff = 0

        if (popupWindow == null) {
            popupWindow = PopupWindow(
                    popupView,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    true)
            popupWindow!!.setOutsideTouchable(true)
            popupWindow!!.setFocusable(true)
            popupWindow!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        if (!popupWindow!!.isShowing()) {
            /*val xOffSet = (22 * Resources.getSystem().displayMetrics.density).toInt()
            val yOffSet = (100 * Resources.getSystem().displayMetrics.density).toInt()*/
            val x = v.x
            val y = v.y

            popupView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_anim_in))
              popupWindow!!.showAtLocation(v, Gravity.TOP  , x.toInt(), y.toInt());
            // popupWindow.showAtLocation(v, Gravity.RIGHT | Gravity.BOTTOM, (int) x, (int) y);
           /* val margin = displayFrameWidth - (loc[0] + v.width)
            xoff = displayFrameWidth - margin - popupWindow!!.getWidth() - loc[0]
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                popupWindow!!.setElevation(20f)
            }
            popupWindow!!.showAsDropDown(v, xoff, yoff)
            popupWindow!!.setAnimationStyle(R.anim.popup_anim_in)*/
        }

        popupWindow!!.setOnDismissListener(PopupWindow.OnDismissListener {
            if (popupWindow != null) {
                popupWindow = null
            }
        })

    }

    override fun showInfoWindow(view: View) {
        showInfoWindow(this@XuberMainActivity,view)
    }

}