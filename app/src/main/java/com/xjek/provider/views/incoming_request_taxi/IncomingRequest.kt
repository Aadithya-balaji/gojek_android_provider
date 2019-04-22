package com.xjek.provider.views.incoming_request_taxi

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.ViewUtils
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
import com.xjek.provider.views.home.HomeFragment
import java.util.concurrent.TimeUnit

class IncomingRequest : BaseDialogFragment<DialogTaxiIncomingRequestBinding>(), IncomingNavigator {


    private lateinit var dialogTaxiIncomingReqBinding: DialogTaxiIncomingRequestBinding
    private lateinit var incomingRequestViewModel: IncomingRequestViewModel
    private lateinit var circularProgressBar: FullCircularProgressBar
    private var shown: Boolean? = false
    private var totalSeconds:Int?=0
    private  var incomingRequstModel:CheckRequestModel?=null
    private  lateinit var  timertoTakeOrder:MyCountDownTimer

    companion object {
        var loadingProgress: MutableLiveData<Boolean>? = null
    }


    override fun getLayout(): Int {
        return com.xjek.provider.R.layout.dialog_taxi_incoming_request
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
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogTaxiIncomingReqBinding = viewDataBinding as DialogTaxiIncomingRequestBinding
        incomingRequestViewModel = IncomingRequestViewModel()
        incomingRequestViewModel.navigator = this
        dialogTaxiIncomingReqBinding.requestmodel = incomingRequestViewModel
        dialogTaxiIncomingReqBinding.setLifecycleOwner(this)
        //incomingRequestViewModel.showLoading = loadingProgress as MutableLiveData<Boolean>
       // initCiruclarSeekbar(100f,"03:21")
        if(incomingRequstModel!=null){

            if( incomingRequstModel!!.responseData!!.requests!!.size>0) {
                totalSeconds = Math.abs( incomingRequstModel!!.responseData!!.requests!![0]!!.time_left_to_respond!!)
                totalSeconds=120
                val minutes = totalSeconds!! / 60
                val seconds = totalSeconds!! % 60
                val time = String.format("%d:%d", minutes, seconds)
                initCiruclarSeekbar(0f, time)
                val totalMiliSeconds=totalSeconds!!*1000
                val totalTimeInLong=totalMiliSeconds.toLong()
                 timertoTakeOrder=MyCountDownTimer(totalTimeInLong,1000L)
                 timertoTakeOrder.start()
            }
        }


        // Get Api Resposne
        getApiResponse()
    }

    fun getApiResponse(){

        //Accept Request model
        observeLiveData(incomingRequestViewModel.acceptRequestLiveData){
            loadingObservable.value=false
            if(incomingRequestViewModel.acceptRequestLiveData.value!!.statusCode.equals("200")){
                timertoTakeOrder.cancel()
                val intent = Intent(activity,Class.forName("com.xjek.taxiservice.views.main.ActivityTaxiMain"))
                activity!!.startActivity(intent)
                dialog!!.dismiss()
            }
        }

        //Cancel Request model
        observeLiveData(incomingRequestViewModel.rejectRequestLiveData){
           loadingObservable.value=false
            if(incomingRequestViewModel.rejectRequestLiveData.value!!.statusCode.equals("200")){
                timertoTakeOrder.cancel()
                com.xjek.base.utils.ViewUtils.showToast(activity!!,incomingRequestViewModel.rejectRequestLiveData.value!!.message.toString(),false)
                dialog!!.dismiss()
            }
        }
    }

    override fun accept() {
        loadingObservable.value=true
        val params: HashMap<String, String> = HashMap()
        params["id"] = incomingRequstModel!!.responseData!!.requests!![0]!!.request!!.id.toString()
        params["service_id"] = incomingRequstModel!!.responseData!!.provider_details!!.service!!.admin_service_id.toString()
        incomingRequestViewModel.acceptRequest(params)

    }

    override fun cancel() {
        loadingObservable.value=true
        val params: HashMap<String, String> = HashMap()
        params["id"] = incomingRequstModel!!.responseData!!.requests!![0]!!.request!!.id.toString()
        params["service_id"] = incomingRequstModel!!.responseData!!.provider_details!!.service!!.admin_service_id.toString()
        incomingRequestViewModel.rejectRequest(params)
    }

    fun initCiruclarSeekbar(percentage:Float,time:String) {
        circularProgressBar = dialogTaxiIncomingReqBinding.ivRequestTime as FullCircularProgressBar
        val circularProgressBarModel = CircularProgressBarModel()
        circularProgressBarModel.setColor(resources.getColor(com.xjek.provider.R.color.colorBasePrimary))
        circularProgressBarModel.setBackgroundColor(resources.getColor(com.xjek.provider.R.color.grey))
        circularProgressBarModel.setStrokeWidth(15.0f)
        circularProgressBarModel.setBackgroundStrokeWidth(15.0f)
        circularProgressBarModel.setBlur(1)
        circularProgressBarModel.setTitleText(time)
        circularProgressBarModel.setTitleSize(38)
        circularProgressBarModel.setSubTitleText("")
        //target set up
        circularProgressBarModel.setTargetSize(100)
        circularProgressBarModel.setTargetColor(resources.getColor(com.xjek.provider.R.color.colorAccent))
        //init with desired style
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


    fun getBundleArugment(){
        val requestModel = if (arguments != null && arguments!!.containsKey("requestModel")) arguments!!.getString("requestModel") else ""
        if(!requestModel.isNullOrEmpty())
        incomingRequstModel= Gson().fromJson(requestModel.toString(),CheckRequestModel::class.java)

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
            val time:String= ""+String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)))
            val interval = millisUntilFinished * 100
            val toPercent = interval / (totalSeconds!!*1000)
            val result = 100 - toPercent
            val longObject = result.toLong()
            Log.e("percentage","----"+longObject.toFloat())
            initCiruclarSeekbar(longObject.toFloat(),time);
        }

    }

    override fun showErrormessage(error: String) {
        loadingObservable.value=false
        com.xjek.base.utils.ViewUtils.showToast(activity!!,error,false)
    }

}