package com.xjek.taxiservice.views.verifyotp

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseDialogFragment
import com.xjek.taxiservice.BuildConfig
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.FragmentVerifyOtpBinding
import com.xjek.taxiservice.views.bottomsheets.BottomSheetNavigator
import com.xjek.taxiservice.views.main.ActivityTaxiModule
import kotlinx.android.synthetic.main.fragment_verify_otp.*

class VerifyOtpDialog : BaseDialogFragment<FragmentVerifyOtpBinding>(), VerifyOTPNavigator {

    private var fragmentVerifyOtpBinding: FragmentVerifyOtpBinding? = null
    private lateinit var mTaxiMainViewModel: ActivityTaxiModule

    override fun getLayout(): Int {
        return R.layout.fragment_verify_otp
    }

    companion object {
        var bottomSheetNavigator: BottomSheetNavigator? = null
        var otp: String? = null
        var requestId: String? = null
        fun newInstance(bottomSheetNavigator: BottomSheetNavigator, s: String, id: Int): VerifyOtpDialog {
            otp = s
            requestId = id.toString()
            Companion.bottomSheetNavigator = bottomSheetNavigator
            return VerifyOtpDialog()
        }
    }

    override fun initView(mViewDataBinding: ViewDataBinding, view: View) {
        fragmentVerifyOtpBinding = mViewDataBinding as FragmentVerifyOtpBinding
        val verifyOTPModule = VerifyOTPModule()
        verifyOTPModule.navigator = this
        fragmentVerifyOtpBinding!!.otpviewmodel = verifyOTPModule
        mTaxiMainViewModel = ViewModelProviders.of(activity!!).get(ActivityTaxiModule::class.java)

        if (BuildConfig.DEBUG) ed_otp.setText(otp)
    }

    override fun startDrip() {
        if (ed_otp.text.toString() == otp) {
            println("RRR :: otp = $otp")

            val params: HashMap<String, String> = HashMap()
            params["id"] = requestId!!
            params["status"] = "ARRIVED"
            params["_method"] = "PATCH"
            mTaxiMainViewModel.taxiStatusUpdate(params)
            dismiss()
        }
    }
}
