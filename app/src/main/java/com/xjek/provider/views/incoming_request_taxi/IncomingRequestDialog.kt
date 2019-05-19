package com.xjek.provider.views.incoming_request_taxi

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.maps.model.LatLng
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.ViewUtils
import com.xjek.base.views.customviews.circularseekbar.CircularProgressBarModel
import com.xjek.base.views.customviews.circularseekbar.FullCircularProgressBar
import com.xjek.foodservice.ui.dashboard.FoodLiveTaskServiceFlow
import com.xjek.provider.R
import com.xjek.provider.databinding.DialogTaxiIncomingRequestBinding
import com.xjek.provider.models.CheckRequestModel
import com.xjek.xuberservice.xuberMainActivity.XuberDashBoardActivity
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

class IncomingRequestDialog : BaseDialogFragment<DialogTaxiIncomingRequestBinding>(), IncomingNavigator {

    private lateinit var dialogTaxiIncomingReqBinding: DialogTaxiIncomingRequestBinding
    private lateinit var mViewModel: IncomingRequestViewModel
    private lateinit var circularProgressBar: FullCircularProgressBar
    private lateinit var timerToTakeOrder: MyCountDownTimer

    private var shown: Boolean? = false
    private var totalSeconds: Int? = 0
    private var incomingRequestModel: CheckRequestModel? = null

    override fun getLayout(): Int = R.layout.dialog_taxi_incoming_request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleArugment()
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    fun isShown(): Boolean {
        return shown!!
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onPause() {
        super.onPause()
        try {
            timerToTakeOrder.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogTaxiIncomingReqBinding = viewDataBinding as DialogTaxiIncomingRequestBinding
        mViewModel = IncomingRequestViewModel()
        mViewModel.navigator = this
        dialogTaxiIncomingReqBinding.requestmodel = mViewModel
        dialogTaxiIncomingReqBinding.lifecycleOwner = this
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>
        if (incomingRequestModel != null) if (incomingRequestModel!!.responseData.requests.isNotEmpty()
                && incomingRequestModel!!.responseData.requests[0].time_left_to_respond!! > 0) {
            totalSeconds = Math.abs(incomingRequestModel!!.responseData.requests[0].time_left_to_respond!!)
            val minutes = totalSeconds!! / 60
            val seconds = totalSeconds!! % 60
            val time = String.format("%d:%d", minutes, seconds)
            initCircularSeekBar(0f, time)
            val totalMilliSeconds = totalSeconds!! * 1000
            val totalTimeInLong = totalMilliSeconds.toLong()
            timerToTakeOrder = MyCountDownTimer(totalTimeInLong, 1000L)
            timerToTakeOrder.start()

            if (mViewModel.pickupLocation.value != null && mViewModel.pickupLocation.value!!.length > 2)
                mViewModel.pickupLocation.value = incomingRequestModel!!.responseData.requests[0].request.s_address
            else {
                val lat = incomingRequestModel!!.responseData.requests[0].request.s_latitude
                val lon = incomingRequestModel!!.responseData.requests[0].request.s_longitude
                var latLng: LatLng? = null
                latLng = LatLng(lat!!, lon!!)
                val address = getCurrentAddress(context!!, latLng)
                if (address.isNotEmpty()) mViewModel.pickupLocation.value = address[0].getAddressLine(0)
            }
            mViewModel.serviceType.value = incomingRequestModel!!.responseData.requests[0].service.display_name
        }

        getApiResponse()
    }

    private fun getCurrentAddress(context: Context, currentLocation: LatLng): List<Address> {
        var addresses: List<Address> = ArrayList()
        val geocoder: Geocoder
        try {
            if (Geocoder.isPresent()) {
                geocoder = Geocoder(context, Locale.getDefault())
                addresses = geocoder.getFromLocation(currentLocation.lat, currentLocation.lng, 1)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return addresses
    }

    private fun getApiResponse() {
        observeLiveData(mViewModel.acceptRequestLiveData) {
            loadingObservable.value = false
            if (mViewModel.acceptRequestLiveData.value!!.statusCode.equals("200")) {
                timerToTakeOrder.cancel()
                when {
                    incomingRequestModel!!.responseData.requests[0].admin_service_id == 3 ->
                        activity!!.startActivity(Intent(activity, XuberDashBoardActivity::class.java))
                    incomingRequestModel!!.responseData.requests[0].admin_service_id == 2 ->
                        activity!!.startActivity(Intent(activity, FoodLiveTaskServiceFlow::class.java))
                    else -> activity!!.startActivity(Intent(activity,
                            Class.forName("com.xjek.taxiservice.views.main.TaxiDashboardActivity")))
                }
                dialog!!.dismiss()
            }
        }
        observeLiveData(mViewModel.rejectRequestLiveData) {
            loadingObservable.value = false
            if (mViewModel.rejectRequestLiveData.value!!.statusCode.equals("200")) {
                timerToTakeOrder.cancel()
                ViewUtils.showToast(activity!!, mViewModel.rejectRequestLiveData.value!!.message.toString(), false)
                dialog!!.dismiss()
            }
        }
    }

    override fun accept() {
        loadingObservable.value = true
        val params: HashMap<String, String> = HashMap()
        params["id"] = incomingRequestModel!!.responseData.requests[0].request.id.toString()
        params["service_id"] = incomingRequestModel!!.responseData.requests[0].service.id.toString()
        mViewModel.acceptRequest(params)
    }

    override fun cancel() {
        loadingObservable.value = true
        val params: HashMap<String, String> = HashMap()
        params["id"] = incomingRequestModel!!.responseData.requests[0].request.id.toString()
        params["service_id"] = incomingRequestModel!!.responseData.requests[0].service.id.toString()
        mViewModel.rejectRequest(params)
    }

    fun initCircularSeekBar(percentage: Float, time: String) {
        circularProgressBar = dialogTaxiIncomingReqBinding.ivRequestTime
        val cpbModel = CircularProgressBarModel()
        cpbModel.backgroundColor = ContextCompat.getColor(context!!, R.color.colorBasePrimary)
        cpbModel.color = ContextCompat.getColor(context!!, R.color.grey)
        cpbModel.strokeWidth = 15.0f
        cpbModel.backgroundStrokeWidth = 15.0f
        cpbModel.blur = 1
        cpbModel.titleText = time
        cpbModel.titleSize = 38
        cpbModel.subTitleText = ""
        cpbModel.targetSize = 100
        cpbModel.targetColor = ContextCompat.getColor(context!!, R.color.colorAccent)
        circularProgressBar.init(cpbModel)
        circularProgressBar.setProgress(percentage)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (shown == false) {
                this.shown = true
                super.show(manager, tag)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun getBundleArugment() {
        val requestModel = if (arguments != null && arguments!!.containsKey("requestModel")) arguments!!.getString("requestModel") else ""
        if (!requestModel.isNullOrEmpty())
            incomingRequestModel = Gson().fromJson(requestModel.toString(), CheckRequestModel::class.java)
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (shown == false) {
            this.shown = true
            return super.show(transaction, tag)
        }
        return -1
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        this.shown = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        this.shown = false
    }

    inner class MyCountDownTimer(startTime: Long, interval: Long) : CountDownTimer(startTime, interval) {

        override fun onFinish() {
            dismiss()
        }

        override fun onTick(millisUntilFinished: Long) {
            val time: String = "" + String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
            val interval = millisUntilFinished * 100
            val toPercent = interval / (totalSeconds!! * 1000)
            val result = 100 - toPercent
            Log.e("percentage", "----" + result.toFloat())
            initCircularSeekBar(result.toFloat(), time)
        }
    }

    override fun showErrormessage(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(activity!!, error, false)
    }
}