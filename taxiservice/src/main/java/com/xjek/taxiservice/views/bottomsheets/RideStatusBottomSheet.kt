package com.xjek.taxiservice.views.bottomsheets

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Chronometer
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.xjek.base.base.BaseBottomSheet
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.LayoutTaxiBottomBinding
import com.xjek.taxiservice.model.ResponseData
import com.xjek.taxiservice.views.invoice.InvoiceActivity
import com.xjek.taxiservice.views.main.ActivityTaxiModule
import com.xjek.taxiservice.views.verifyotp.VerifyOtpDialog
import kotlinx.android.synthetic.main.layout_taxi_bottom.*

class RideStatusBottomSheet : BaseBottomSheet<LayoutTaxiBottomBinding>(),
        BottomSheetNavigator,
        DialogInterface.OnDismissListener,
        View.OnClickListener,
        Chronometer.OnChronometerTickListener {

    private lateinit var mLayoutTaxiBottomBinding: LayoutTaxiBottomBinding
    private var ctxt: AppCompatActivity? = null

    private lateinit var mViewModel: RideStatusViewModel
    private lateinit var mTaxiMainViewModel: ActivityTaxiModule

    private lateinit var ivMapBin: ImageButton
    private lateinit var ivSteering: ImageButton
    private lateinit var ivFlag: ImageButton
    private lateinit var vlTripStarted: View
    private lateinit var vlTripFinished: View
    private lateinit var llButtonApprove: LinearLayout
    private lateinit var llButtonStartTrip: LinearLayout
    private lateinit var tvStartTrip: TextView
    private var isWaitingTime: Boolean? = false
    private var lastWaitingTime: Long? = 0
    private var mCheckStatusModel: ResponseData? = null

    override fun openInvoice() {
        val strCheckRequestModel = Gson().toJson(mCheckStatusModel)
        val intent = Intent(activity!!, InvoiceActivity::class.java)
        intent.putExtra("strRequestModel", strCheckRequestModel)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        activity!!.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mLayoutTaxiBottomBinding = mViewDataBinding as LayoutTaxiBottomBinding
        mViewModel = ViewModelProviders.of(this).get(RideStatusViewModel::class.java)
        mTaxiMainViewModel = ViewModelProviders.of(activity!!).get(ActivityTaxiModule::class.java)
        mViewModel.navigator = this
        mLayoutTaxiBottomBinding.bottomsheetmodel = mViewModel
        val rootView = mLayoutTaxiBottomBinding.root
        ivMapBin = rootView.findViewById(R.id.ib_location_pin) as ImageButton
        ivSteering = rootView.findViewById(R.id.ib_steering) as ImageButton
        ivFlag = rootView.findViewById(R.id.ib_flag) as ImageButton
        vlTripStarted = rootView.findViewById(R.id.vl_trip_started) as View
        vlTripFinished = rootView.findViewById(R.id.vl_trip_finished) as View
        mLayoutTaxiBottomBinding.cmWaiting.onChronometerTickListener = this
        mLayoutTaxiBottomBinding.btnWaiting.setOnClickListener(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctxt = context as AppCompatActivity
    }

    override fun getLayout(): Int = R.layout.layout_taxi_bottom

    override fun whenStatusStarted(checkStatusModel: ResponseData?) {
        println("RRR :: RideStatusBottomSheet.whenStatusStarted")
        llWaitingTimeContainer.visibility = View.GONE
        mCheckStatusModel = checkStatusModel!!
        tv_user_name.text = checkStatusModel.request.user!!.first_name + " " + checkStatusModel.request.user!!.last_name
        tv_user_address_one.text = checkStatusModel.request.s_address
        rate.rating = checkStatusModel.request.user!!.rating!!.toFloat()
        btn_arrived.setOnClickListener {
            val params: HashMap<String, String> = HashMap()
            params["id"] = checkStatusModel.request.id.toString()
            params["status"] = "ARRIVED"
            params["_method"] = "PATCH"
            mTaxiMainViewModel.taxiStatusUpdate(params)
        }
    }

    override fun whenArrivedStatus() {
    }

    override fun openOTPDialog() {
        val otpDialogFragment = VerifyOtpDialog.newInstance(
                this,
                mCheckStatusModel!!.request.otp!!,
                mCheckStatusModel!!.request.id!!
        )
        otpDialogFragment.show(ctxt!!.supportFragmentManager, "dialog")
    }

    @SuppressLint("ObsoleteSdkInt")
    fun startTrip() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivMapBin = view!!.findViewById(R.id.ib_location_pin) as ImageButton
            ivSteering = view!!.findViewById(R.id.ib_steering) as ImageButton
            ivFlag = view!!.findViewById(R.id.ib_flag) as ImageButton
            vlTripStarted = view!!.findViewById(R.id.vl_trip_started) as View
            vlTripFinished = view!!.findViewById(R.id.vl_trip_finished) as View
            llButtonApprove = view!!.findViewById(R.id.ll_button_approve)
            tvStartTrip = view!!.findViewById(R.id.tv_arrived)
            ivMapBin.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)
            ivSteering.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)
            // ivFlag.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)

            vlTripStarted.visibility = View.VISIBLE
            vlTripFinished.visibility = View.GONE

            llButtonApprove.visibility = View.GONE
            tvStartTrip.visibility = View.VISIBLE
        }
    }

    override fun whenStatusArrived(checkStatusModel: ResponseData?) {
        ivMapBin = view!!.findViewById(R.id.ib_location_pin) as ImageButton
        ivSteering = view!!.findViewById(R.id.ib_steering) as ImageButton
        ivFlag = view!!.findViewById(R.id.ib_flag) as ImageButton
        vlTripStarted = view!!.findViewById(R.id.vl_trip_started) as View
        vlTripFinished = view!!.findViewById(R.id.vl_trip_finished) as View
        llButtonApprove = view!!.findViewById(R.id.ll_button_approve)
        tvStartTrip = view!!.findViewById(R.id.tv_arrived)
        ivMapBin.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)

//        ivSteering.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)
//         ivFlag.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)

//        vlTripStarted.visibility = View.VISIBLE
//        vlTripFinished.visibility = View.GONE
//
//        llButtonApprove.visibility = View.GONE
//        tvStartTrip.visibility = View.VISIBLE

        mCheckStatusModel = checkStatusModel!!
        tv_user_name.text = checkStatusModel.request.user!!.first_name + " " + checkStatusModel.request.user!!.last_name
        tv_user_address_one.text = checkStatusModel.request.s_address
        rate.rating = checkStatusModel.request.user!!.rating!!.toFloat()
    }

    override fun closeBottomSheet() {
        dismiss()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btn_waiting -> {
                if (isWaitingTime == true) {
                    mLayoutTaxiBottomBinding.btnWaiting.backgroundTintList = ContextCompat.getColorStateList(ctxt!!, R.color.white)
                    mLayoutTaxiBottomBinding.btnWaiting.setTextColor(ContextCompat.getColor(ctxt!!, R.color.black))
                    isWaitingTime = false
                    lastWaitingTime = SystemClock.elapsedRealtime()
                    mLayoutTaxiBottomBinding.cmWaiting.stop()
                } else {
                    mLayoutTaxiBottomBinding.btnWaiting.backgroundTintList = ContextCompat.getColorStateList(ctxt!!, R.color.taxi_bg_yellow)
                    mLayoutTaxiBottomBinding.btnWaiting.setTextColor(ContextCompat.getColor(ctxt!!, R.color.white))
                    isWaitingTime = true
                    val temp: Long = 0
                    if (lastWaitingTime != temp)
                        mLayoutTaxiBottomBinding.cmWaiting.base = (mLayoutTaxiBottomBinding.cmWaiting.base + SystemClock.elapsedRealtime()) - lastWaitingTime!!
                    else
                        mLayoutTaxiBottomBinding.cmWaiting.base = SystemClock.elapsedRealtime()
                    mLayoutTaxiBottomBinding.cmWaiting.start()
                }
            }
        }
    }

    override fun onChronometerTick(chronometer: Chronometer?) {
        val time = SystemClock.elapsedRealtime() - chronometer!!.getBase()
        val h = (time / 3600000).toInt()
        val m = (time - h * 3600000).toInt() / 60000
        val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
        val t = (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
        chronometer.setText(t)
    }
}