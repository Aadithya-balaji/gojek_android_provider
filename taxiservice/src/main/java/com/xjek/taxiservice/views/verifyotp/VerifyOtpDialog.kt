package com.xjek.taxiservice.views.verifyotp

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.data.Constants.RideStatus.PICKED_UP
import com.xjek.base.utils.ViewUtils
import com.xjek.taxiservice.BuildConfig
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.FragmentVerifyOtpBinding
import com.xjek.taxiservice.views.main.TaxiDashboardViewModel
import kotlinx.android.synthetic.main.fragment_verify_otp.*

class VerifyOtpDialog : BaseDialogFragment<FragmentVerifyOtpBinding>(), VerifyOTPNavigator {

    private var fragmentVerifyOtpBinding: FragmentVerifyOtpBinding? = null
    private lateinit var mTaxiMainDashboardViewModel: TaxiDashboardViewModel

    override fun getLayout(): Int = R.layout.fragment_verify_otp

    companion object {
        var otp: String? = null
        var requestId: String? = null
        fun newInstance(s: String, id: Int): VerifyOtpDialog {
            otp = s
            requestId = id.toString()
            return VerifyOtpDialog()
        }
    }

    override fun initView(mViewDataBinding: ViewDataBinding, view: View) {
        fragmentVerifyOtpBinding = mViewDataBinding as FragmentVerifyOtpBinding
        val verifyOTPModule = VerifyOTPModule()
        verifyOTPModule.navigator = this
        fragmentVerifyOtpBinding!!.otpviewmodel = verifyOTPModule
        mTaxiMainDashboardViewModel = ViewModelProviders.of(activity!!).get(TaxiDashboardViewModel::class.java)

//        if (BuildConfig.DEBUG) ed_otp.setText(otp)
        bt_start_trip.setOnClickListener {
            if (ed_otp.text.toString() == otp) {
                println("RRR :: otp = $otp")

                val params: HashMap<String, String> = HashMap()
                params["id"] = requestId!!
                params["status"] = PICKED_UP
                params["_method"] = "PATCH"
                mTaxiMainDashboardViewModel.taxiStatusUpdate(params)
                dismiss()
            } else ViewUtils.showToast(context!!, resources.getString(R.string.invalid_otp), false)
        }
    }
}
