package com.xjek.taxiservice.views.bottomsheets

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseBottomSheet
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.LayoutTaxiBottomBinding
import com.xjek.taxiservice.views.invoice.InvoiceActivity
import com.xjek.taxiservice.views.verifyotp.VerifyOtpDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomStatuslayout : BaseBottomSheet<LayoutTaxiBottomBinding>(), BottomSheetNavigator, DialogInterface.OnDismissListener {



    private var mLayoutTaxiBottomBinding: LayoutTaxiBottomBinding? = null
    private var ctxt: AppCompatActivity? = null
    private lateinit var ivMapBin: ImageButton
    private lateinit var ivSteering: ImageButton
    private lateinit var ivFlag: ImageButton
    private lateinit var vlTripStarted: View
    private lateinit var vlTripFinished: View
    private  lateinit var  llButtonApprove:LinearLayout
    private  lateinit var  llButtonStartTrip:LinearLayout
    private  lateinit var   tvStartTrip:TextView



    override fun opentInvoice() {
        val intent= Intent(ctxt, InvoiceActivity::class.java)
        ctxt!!.startActivity(intent)
    }

    override fun initView(View: View?) {
        ivMapBin = view!!.findViewById(R.id.ib_location_pin) as ImageButton
        ivSteering = view!!.findViewById(R.id.ib_steering) as ImageButton
        ivFlag = view!!.findViewById(R.id.ib_flag) as ImageButton
        vlTripStarted = view!!.findViewById(R.id.vl_trip_started) as View
        vlTripFinished = view!!.findViewById(R.id.vl_trip_finished) as View
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctxt = context as AppCompatActivity
    }

    override fun getLayout(): Int {
        return R.layout.layout_taxi_bottom
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mLayoutTaxiBottomBinding = mViewDataBinding as LayoutTaxiBottomBinding
        val bottomSheetModule = BottomSheetModule()
        mLayoutTaxiBottomBinding!!.bottomsheetmodel = bottomSheetModule
    }


    override fun openOTPDialog() {
        //otpDialogFragment.onDismiss(DismisListner())
        val otpDialogFragment = VerifyOtpDialog.newInstance(this)
        otpDialogFragment.show(ctxt!!.supportFragmentManager, "dialog")
        //otpDialogFragment.onDismiss(DismisListner())
    }


    inner class DismisListner : DialogInterface {
        override fun dismiss() {
            startTrip()
        }

        override fun cancel() {
        }


    }

    override fun startTrip(isTrue: Boolean) {
        startTrip()
    }

    fun startTrip() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ivMapBin = view!!.findViewById(R.id.ib_location_pin) as ImageButton
            ivSteering = view!!.findViewById(R.id.ib_steering) as ImageButton
            ivFlag = view!!.findViewById(R.id.ib_flag) as ImageButton
            vlTripStarted = view!!.findViewById(R.id.vl_trip_started) as View
            vlTripFinished = view!!.findViewById(R.id.vl_trip_finished) as View
            llButtonApprove=view!!.findViewById(R.id.ll_button_approve)
            tvStartTrip=view!!.findViewById(R.id.tv_arrived)
            ivMapBin.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)
            ivSteering.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)
           // ivFlag.background = ContextCompat.getDrawable(ctxt!!, R.drawable.bg_status_complete)

            vlTripStarted.visibility = View.VISIBLE
            vlTripFinished.visibility = View.GONE


            llButtonApprove.visibility=View.GONE
            tvStartTrip.visibility=View.VISIBLE

        }
    }

    override fun closeBottomSheet() {
        dismiss()
    }
}
