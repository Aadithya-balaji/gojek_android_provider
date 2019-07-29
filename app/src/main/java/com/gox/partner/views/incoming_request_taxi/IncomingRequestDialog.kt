package com.gox.partner.views.incoming_request_taxi

import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
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
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.ViewUtils
import com.gox.base.views.customviews.circularseekbar.CircularProgressBarModel
import com.gox.base.views.customviews.circularseekbar.FullCircularProgressBar
import com.gox.foodservice.ui.dashboard.FoodieDashboardActivity
import com.gox.partner.R
import com.gox.partner.databinding.DialogTaxiIncomingRequestBinding
import com.gox.partner.models.CheckRequestModel
import com.gox.partner.models.Request
import com.gox.taxiservice.views.main.TaxiDashboardActivity
import com.gox.xuberservice.xuberMainActivity.XUberDashBoardActivity
import java.util.concurrent.TimeUnit

class IncomingRequestDialog : BaseDialogFragment<DialogTaxiIncomingRequestBinding>(), IncomingNavigator {

    private lateinit var mBinding: DialogTaxiIncomingRequestBinding
    private lateinit var mViewModel: IncomingRequestViewModel

    private lateinit var circularProgressBar: FullCircularProgressBar
    private lateinit var timerToTakeOrder: MyCountDownTimer
    private lateinit var request: Request

    private var shown: Boolean? = false
    private var totalSeconds: Int? = 0
    private var incomingRequestModel: CheckRequestModel? = null
    private lateinit var mPlayer: MediaPlayer

    override fun getLayout() = R.layout.dialog_taxi_incoming_request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleArgument()
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    fun isShown() = shown!!

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog!!.setCancelable(false)
    }

    override fun onResume() {
        super.onResume()
        val notificationManager = activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    override fun onPause() {
        super.onPause()
        try {
            timerToTakeOrder.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mPlayer.stop()
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as DialogTaxiIncomingRequestBinding
        mViewModel = IncomingRequestViewModel()
        mViewModel.navigator = this
        mBinding.requestmodel = mViewModel
        mBinding.lifecycleOwner = this
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>

        mPlayer = MediaPlayer.create(context, R.raw.alert_tone)

        if (incomingRequestModel != null) if (incomingRequestModel!!.responseData.requests.isNotEmpty()
        /*&& request.time_left_to_respond!! > 0*/) {
//            totalSeconds = Math.abs(request.time_left_to_respond!!)
            totalSeconds = 60
            val minutes = totalSeconds!! / 60
            val seconds = totalSeconds!! % 60
            val time = String.format("%d:%d", minutes, seconds)
            initCircularSeekBar(0f, time)
            val totalMilliSeconds = totalSeconds!! * 1000
            val totalTimeInLong = totalMilliSeconds.toLong()
            timerToTakeOrder = MyCountDownTimer(totalTimeInLong, 1000L)
            timerToTakeOrder.start()

            when (request.service.admin_service) {

                Constants.ModuleTypes.TRANSPORT -> {
                    mViewModel.pickupLocation.value = request.request.s_address

                    mViewModel.serviceType.value = request.service.display_name + " - " +
                            request.request.ride_type.ride_name + " - " +
                            request.request.ride.vehicle_name
                }
                Constants.ModuleTypes.SERVICE -> {
                    mViewModel.pickupLocation.value = request.request.s_address

                    mViewModel.serviceType.value = request.service.display_name + " - " +
                            request.request.service.service_category.service_category_name + " - " +
                            request.request.service.servicesub_category.service_subcategory_name + " - " +
                            request.request.service.service_name
                }
                Constants.ModuleTypes.ORDER -> {
                    mViewModel.pickupLocation.value = request.request.pickup.store_location
                    mViewModel.serviceType.value = request.service.display_name + " - " +
                            request.request.pickup.storetype.name + " - " +
                            request.request.pickup.store_name
                }
            }
        }
        getApiResponse()
        mPlayer.start()
    }

    private fun getApiResponse() {
        observeLiveData(mViewModel.acceptRequestLiveData) {
            loadingObservable.value = false
            if (mViewModel.acceptRequestLiveData.value!!.statusCode.equals("200")) {
                timerToTakeOrder.cancel()
                when {
                    request.admin_service.equals("SERVICE",true)  ->
                        activity!!.startActivity(Intent(activity, XUberDashBoardActivity::class.java))
                    request.admin_service.equals("ORDER",true)  ->
                        activity!!.startActivity(Intent(activity, FoodieDashboardActivity::class.java))
                    else -> activity!!.startActivity(Intent(activity, TaxiDashboardActivity::class.java))
                }
                mPlayer.stop()
                dialog!!.dismiss()
            }
        }
        observeLiveData(mViewModel.rejectRequestLiveData) {
            loadingObservable.value = false
            if (mViewModel.rejectRequestLiveData.value!!.statusCode.equals("200")) {
                timerToTakeOrder.cancel()
                ViewUtils.showToast(activity!!, mViewModel.rejectRequestLiveData.value!!.message.toString(), false)
                mPlayer.stop()
                dialog!!.dismiss()
            }
        }
    }

    override fun accept() {
        loadingObservable.value = true
        val params: HashMap<String, String> = HashMap()
        params["id"] = request.request.id.toString()
        params["admin_service"] = request.service.admin_service
        mViewModel.acceptRequest(params)
    }

    override fun cancel() {
        loadingObservable.value = true
        val params: HashMap<String, String> = HashMap()
        params["id"] = request.request.id.toString()
        params["admin_service"] = request.service.admin_service
        mViewModel.rejectRequest(params)
    }

    fun initCircularSeekBar(percentage: Float, time: String) {
        circularProgressBar = mBinding.ivRequestTime
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

    private fun getBundleArgument() {
        val requestModel = if (arguments != null && arguments!!.containsKey("requestModel"))
            arguments!!.getString("requestModel")
        else ""
        if (!requestModel.isNullOrEmpty()) {
            incomingRequestModel = Gson().fromJson(requestModel.toString(), CheckRequestModel::class.java)
            request = incomingRequestModel!!.responseData.requests[0]
        }
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
        mPlayer.stop()
    }

    inner class MyCountDownTimer(startTime: Long, interval: Long) : CountDownTimer(startTime, interval) {

        override fun onFinish() {
            try {
                mPlayer.stop()
                dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

    override fun onStop() {
        super.onStop()
        mPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPlayer.stop()
    }

    override fun showErrorMessage(error: String) {
        loadingObservable.value = false
        if (!error.isNullOrEmpty() && error.equals("null", true))
            ViewUtils.showToast(activity!!, error, false)
    }
}
