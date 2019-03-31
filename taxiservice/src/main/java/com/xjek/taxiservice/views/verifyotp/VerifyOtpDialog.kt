package com.xjek.taxiservice.views.verifyotp

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseDialogFragment
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.FragmentVerifyOtpBinding
import com.xjek.taxiservice.views.bottomsheets.BottomSheetNavigator

class VerifyOtpDialog() : BaseDialogFragment<FragmentVerifyOtpBinding>() , VerifyOTPNavigator {
    override fun startDrip() {
        dismiss()
        bottomSheetNavigator!!.startTrip(true)
    }

    private var fragmentVerifyOtpBinding: FragmentVerifyOtpBinding? = null

    override fun getLayout(): Int {
        return R.layout.fragment_verify_otp
    }

    companion object {
        var bottomSheetNavigator: BottomSheetNavigator?=null
        fun newInstance(bottomSheetNavigator: BottomSheetNavigator): VerifyOtpDialog {
          //  this.bottomSheetNavigator=bottomSheetNavigator
            Companion.bottomSheetNavigator =bottomSheetNavigator
            val fragment = VerifyOtpDialog()
            return fragment
        }
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        fragmentVerifyOtpBinding = mViewDataBinding as FragmentVerifyOtpBinding
        val verifyOTPModule = VerifyOTPModule()
        verifyOTPModule.navigator=this
        fragmentVerifyOtpBinding!!.otpviewmodel = verifyOTPModule
    }

}
