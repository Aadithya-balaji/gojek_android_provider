package com.xjek.taxiservice.views.bottomsheets

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseBottomSheet
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.LayoutTaxiBottomBinding
import com.xjek.taxiservice.model.ResponseData
import com.xjek.taxiservice.views.invoice.InvoiceActivity
import com.xjek.taxiservice.views.verifyotp.VerifyOtpDialog
import kotlinx.android.synthetic.main.layout_taxi_bottom.*
import kotlinx.android.synthetic.main.layout_taxi_bottom.view.*

class RideStatusBottomSheet : BaseBottomSheet<LayoutTaxiBottomBinding>(), BottomSheetNavigator, DialogInterface.OnDismissListener {

    private lateinit var mLayoutTaxiBottomBinding: LayoutTaxiBottomBinding
    private var ctxt: AppCompatActivity? = null

    private lateinit var mViewModel: RideStatusViewModel

    private lateinit var ivMapBin: ImageButton
    private lateinit var ivSteering: ImageButton
    private lateinit var ivFlag: ImageButton
    private lateinit var vlTripStarted: View
    private lateinit var vlTripFinished: View

    private lateinit var llButtonApprove: LinearLayout
    private lateinit var llButtonStartTrip: LinearLayout
    private lateinit var tvStartTrip: TextView

    override fun openInvoice() {
        ctxt!!.startActivity(Intent(context, InvoiceActivity::class.java))
    }


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mLayoutTaxiBottomBinding = mViewDataBinding as LayoutTaxiBottomBinding
        mViewModel = ViewModelProviders.of(this).get(RideStatusViewModel::class.java)
        mViewModel.navigator = this
        mLayoutTaxiBottomBinding.bottomsheetmodel = mViewModel

        val rootView = mLayoutTaxiBottomBinding.root

        ivMapBin = rootView.findViewById(R.id.ib_location_pin) as ImageButton
        ivSteering = rootView.findViewById(R.id.ib_steering) as ImageButton
        ivFlag = rootView.findViewById(R.id.ib_flag) as ImageButton
        vlTripStarted = rootView.findViewById(R.id.vl_trip_started) as View
        vlTripFinished = rootView.findViewById(R.id.vl_trip_finished) as View

        rootView.btn_approve.setOnClickListener {
            println("RRR :: RideStatusBottomSheet.initView")
            Toast.makeText(context!!, "RRR ", Toast.LENGTH_SHORT).show() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctxt = context as AppCompatActivity
    }

    override fun getLayout(): Int {
        return R.layout.layout_taxi_bottom
    }

    @SuppressLint("SetTextI18n")
    override fun whenStatusStarted(checkStatusModel: ResponseData?) {
        tv_user_name.text = checkStatusModel!!.request!!.user!!.first_name + " " + checkStatusModel.request!!.user!!.last_name
        tv_user_address_one.text = checkStatusModel.request!!.s_address
        rate.rating = checkStatusModel.request!!.user!!.rating!!.toFloat()
    }

    override fun openOTPDialog() {
        val otpDialogFragment = VerifyOtpDialog.newInstance(this)
         otpDialogFragment.show(ctxt!!.supportFragmentManager, "dialog")
    }

    inner class DismissListener : DialogInterface {
        override fun dismiss() {
            startTrip()
        }

        override fun cancel() {
        }
    }

    override fun startTrip(isTrue: Boolean) {
        startTrip()
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

    override fun closeBottomSheet() {
        dismiss()
    }
}
