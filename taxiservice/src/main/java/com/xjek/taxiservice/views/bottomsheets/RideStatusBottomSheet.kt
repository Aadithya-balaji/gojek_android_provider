package com.xjek.taxiservice.views.bottomsheets

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseBottomSheet
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.LayoutTaxiBottomBinding
import com.xjek.taxiservice.model.ResponseData
import com.xjek.taxiservice.views.main.ActivityTaxiModule
import com.xjek.taxiservice.views.verifyotp.VerifyOtpDialog
import kotlinx.android.synthetic.main.layout_taxi_bottom.*

class RideStatusBottomSheet : BaseBottomSheet<LayoutTaxiBottomBinding>(), BottomSheetNavigator, DialogInterface.OnDismissListener {

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
    private var mCheckStatusModel: ResponseData? = null

    override fun openInvoice() {

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

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctxt = context as AppCompatActivity
    }

    override fun getLayout(): Int = R.layout.layout_taxi_bottom

    override fun whenStatusStarted(checkStatusModel: ResponseData?) {
        mCheckStatusModel = checkStatusModel!!
        tv_user_name.text = checkStatusModel.request!!.user!!.first_name + " " + checkStatusModel.request!!.user!!.last_name
        tv_user_address_one.text = checkStatusModel.request!!.s_address
        rate.rating = checkStatusModel.request!!.user!!.rating!!.toFloat()
    }

    override fun whenArrivedStatus() {
        btn_arrived.visibility = View.GONE
        btn_picked_up.visibility = View.VISIBLE
    }

    override fun openOTPDialog() {
        val otpDialogFragment = VerifyOtpDialog.newInstance(
                this,
                mCheckStatusModel!!.request!!.otp!!,
                mCheckStatusModel!!.request!!.id!!
        )
        otpDialogFragment.show(ctxt!!.supportFragmentManager, "dialog")
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
        tv_user_name.text = checkStatusModel.request!!.user!!.first_name + " " + checkStatusModel.request!!.user!!.last_name
        tv_user_address_one.text = checkStatusModel.request!!.s_address
        rate.rating = checkStatusModel.request!!.user!!.rating!!.toFloat()
    }

    override fun closeBottomSheet() {
        dismiss()
    }
}
