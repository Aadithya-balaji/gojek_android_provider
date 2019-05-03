package com.xjek.provider.views.incoming_request_taxi

import android.content.DialogInterface
import android.content.Intent
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
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.views.customviews.circularseekbar.CircularProgressBarModel
import com.xjek.base.views.customviews.circularseekbar.FullCircularProgressBar
import com.xjek.provider.R
import com.xjek.provider.databinding.DialogTaxiIncomingRequestBinding
import com.xjek.provider.models.CheckRequestModel
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity
import java.util.concurrent.TimeUnit

class IncomingRequestDialog : BaseDialogFragment<DialogTaxiIncomingRequestBinding>(), IncomingNavigator {

    private lateinit var dialogTaxiIncomingReqBinding: DialogTaxiIncomingRequestBinding
    private lateinit var incomingRequestViewModel: IncomingRequestViewModel
    private lateinit var circularProgressBar: FullCircularProgressBar
    private lateinit var timerToTakeOrder: MyCountDownTimer

    private var shown: Boolean? = false
    private var totalSeconds: Int? = 0
    private var incomingRequestModel: CheckRequestModel? = null

    override fun getLayout(): Int {
        return R.layout.dialog_taxi_incoming_request
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleArugment()
    }

    fun isShown(): Boolean {
        return shown!!
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogTaxiIncomingReqBinding = viewDataBinding as DialogTaxiIncomingRequestBinding
        incomingRequestViewModel = IncomingRequestViewModel()
        incomingRequestViewModel.navigator = this
        dialogTaxiIncomingReqBinding.requestmodel = incomingRequestViewModel
        dialogTaxiIncomingReqBinding.lifecycleOwner = this
        incomingRequestViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>
        if (incomingRequestModel != null) if (incomingRequestModel!!.responseData.requests.isNotEmpty()) {
            totalSeconds = Math.abs(incomingRequestModel!!.responseData.requests[0].time_left_to_respond)
            val minutes = totalSeconds!! / 60
            val seconds = totalSeconds!! % 60
            val time = String.format("%d:%d", minutes, seconds)
            initCircularSeekbar(0f, time)
            val totalMilliSeconds = totalSeconds!! * 1000
            val totalTimeInLong = totalMilliSeconds.toLong()
            timerToTakeOrder = MyCountDownTimer(totalTimeInLong, 1000L)
            timerToTakeOrder.start()

            incomingRequestViewModel.pickupLocation.value = incomingRequestModel!!.responseData.requests[0].request.s_address
            incomingRequestViewModel.serviceType.value = incomingRequestModel!!.responseData.requests[0].service.display_name
        }

        getApiResponse()
    }

    private fun getApiResponse() {

        observeLiveData(incomingRequestViewModel.acceptRequestLiveData) {
            loadingObservable.value = false
            if (incomingRequestViewModel.acceptRequestLiveData.value!!.statusCode.equals("200")) {
                timerToTakeOrder.cancel()
                if(incomingRequestModel!!.responseData.requests[0].admin_service_id==3) {
                    val intent = Intent(activity,XuberMainActivity::class.java)
                    activity!!.startActivity(intent)
                }else {
                    val intent = Intent(activity, Class.forName("com.xjek.taxiservice.views.main.TaxiDashboardActivity"))
                    activity!!.startActivity(intent)
                }
                dialog!!.dismiss()
            }
        }

        observeLiveData(incomingRequestViewModel.rejectRequestLiveData) {
            loadingObservable.value = false
            if (incomingRequestViewModel.rejectRequestLiveData.value!!.statusCode.equals("200")) {
                timerToTakeOrder.cancel()
                com.xjek.base.utils.ViewUtils.showToast(activity!!, incomingRequestViewModel.rejectRequestLiveData.value!!.message.toString(), false)
                dialog!!.dismiss()
            }
        }
    }

    override fun accept() {
        loadingObservable.value = true
        val params: HashMap<String, String> = HashMap()
        params["id"] = incomingRequestModel!!.responseData.requests[0].request.id.toString()
        params["service_id"] = incomingRequestModel!!.responseData.requests[0].service.id.toString()
        incomingRequestViewModel.acceptRequest(params)

    }

    override fun cancel() {
        loadingObservable.value = true
        val params: HashMap<String, String> = HashMap()
        params["id"] = incomingRequestModel!!.responseData.requests[0].request.id.toString()
        params["service_id"] = incomingRequestModel!!.responseData.requests[0].service.id.toString()
        incomingRequestViewModel.rejectRequest(params)
    }

    fun initCircularSeekbar(percentage: Float, time: String) {
        circularProgressBar = dialogTaxiIncomingReqBinding.ivRequestTime
        val circularProgressBarModel = CircularProgressBarModel()
        circularProgressBarModel.backgroundColor = ContextCompat.getColor(context!!, R.color.colorBasePrimary)
        circularProgressBarModel.color = ContextCompat.getColor(context!!, R.color.grey)
        circularProgressBarModel.strokeWidth = 15.0f
        circularProgressBarModel.backgroundStrokeWidth = 15.0f
        circularProgressBarModel.blur = 1
        circularProgressBarModel.titleText = time
        circularProgressBarModel.titleSize = 38
        circularProgressBarModel.subTitleText = ""
        circularProgressBarModel.targetSize = 100
        circularProgressBarModel.targetColor = ContextCompat.getColor(context!!, R.color.colorAccent)
        circularProgressBar.init(circularProgressBarModel)
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
            initCircularSeekbar(result.toFloat(), time)
        }
    }

    override fun showErrormessage(error: String) {
        loadingObservable.value = false
        com.xjek.base.utils.ViewUtils.showToast(activity!!, error, false)
    }
}