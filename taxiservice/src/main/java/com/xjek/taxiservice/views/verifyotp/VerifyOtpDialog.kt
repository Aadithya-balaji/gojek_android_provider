package com.xjek.taxiservice.views.verifyotp

import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseDialogFragment
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.FragmentVerifyOtpBinding
import com.xjek.taxiservice.views.main.TaxiDashboardViewModel

class VerifyOtpDialog : BaseDialogFragment<FragmentVerifyOtpBinding>(), VerifyOTPNavigator {

    private var fragmentVerifyOtpBinding: FragmentVerifyOtpBinding? = null
    private lateinit var mTaxiMainDashboardViewModel: TaxiDashboardViewModel

    override fun getLayout(): Int = R.layout.fragment_verify_otp

    companion object {
//        var rideStatusNavigator: RideStatusNavigator? = null
//        var otp: String? = null
//        var requestId: String? = null
//        fun newInstance(rideStatusNavigator: RideStatusNavigator, s: String, id: Int): VerifyOtpDialog {
//            otp = s
//            requestId = id.toString()
//            Companion.rideStatusNavigator = rideStatusNavigator
//            return VerifyOtpDialog()
//        }
    }

    override fun initView(mViewDataBinding: ViewDataBinding, view: View) {
//        fragmentVerifyOtpBinding = mViewDataBinding as FragmentVerifyOtpBinding
//        val verifyOTPModule = VerifyOTPModule()
//        verifyOTPModule.navigator = this
//        fragmentVerifyOtpBinding!!.otpviewmodel = verifyOTPModule
//        mTaxiMainDashboardViewModel = ViewModelProviders.of(activity!!).get(TaxiDashboardViewModel::class.java)
//
//        if (BuildConfig.DEBUG) ed_otp.setText(otp)
//        bt_start_trip.setOnClickListener {
//            if (ed_otp.text.toString() == otp) {
//                println("RRR :: otp = $otp")
//
//                val params: HashMap<String, String> = HashMap()
//                params["id"] = requestId!!
//                params["status"] = PICKED_UP
//                params["_method"] = "PATCH"
//                mTaxiMainDashboardViewModel.taxiStatusUpdate(params)
//                dismiss()
//            }
//        }
    }
}
