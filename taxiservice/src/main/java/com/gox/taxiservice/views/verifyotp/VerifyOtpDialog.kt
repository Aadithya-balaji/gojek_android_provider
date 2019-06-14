package com.gox.taxiservice.views.verifyotp

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants.RideStatus.PICKED_UP
import com.gox.base.utils.ViewUtils
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.FragmentVerifyOtpBinding
import com.gox.taxiservice.views.main.TaxiDashboardViewModel
import kotlinx.android.synthetic.main.fragment_verify_otp.*

class VerifyOtpDialog : BaseDialogFragment<FragmentVerifyOtpBinding>(), VerifyOTPNavigator {

    private var fragmentVerifyOtpBinding: FragmentVerifyOtpBinding? = null
    private lateinit var mTaxiMainDashboardViewModel: TaxiDashboardViewModel

    override fun getLayout() = R.layout.fragment_verify_otp

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
