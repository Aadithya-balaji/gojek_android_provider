package com.gox.foodservice.ui.verifyotp

import android.view.View
import android.view.View.VISIBLE
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.ViewUtils
import com.gox.foodservice.R
import com.gox.foodservice.databinding.FoodieFragmentVerifyOtpBinding
import com.gox.foodservice.ui.dashboard.FoodLiveTaskServiceViewModel
import kotlinx.android.synthetic.main.foodie_fragment_verify_otp.*

class FoodieVerifyOtpDialog : BaseDialogFragment<FoodieFragmentVerifyOtpBinding>(), FoodieVerifyOTPNavigator {

    private var fragmentVerifyOtpBinding: FoodieFragmentVerifyOtpBinding? = null
    private lateinit var foodLiveTaskServiceViewModel: FoodLiveTaskServiceViewModel

    override fun getLayout() = R.layout.foodie_fragment_verify_otp

    companion object {
        var otp: String? = null
        var requestId: String? = null
        var payable: Int? = 0
        fun newInstance(s: String, id: Int, pay: Int): FoodieVerifyOtpDialog {
            otp = s
            requestId = id.toString()
            payable = pay
            return FoodieVerifyOtpDialog()
        }
    }

    override fun initView(mViewDataBinding: ViewDataBinding, view: View) {
        fragmentVerifyOtpBinding = mViewDataBinding as FoodieFragmentVerifyOtpBinding
        val verifyOTPModule = FoodieVerifyOTPViewModel()
        verifyOTPModule.navigator = this
        fragmentVerifyOtpBinding!!.otpviewmodel = verifyOTPModule
        foodLiveTaskServiceViewModel = ViewModelProviders.of(activity!!).get(FoodLiveTaskServiceViewModel::class.java)

        when {
            payable!!.toDouble() > 0 -> payment_txt.visibility = VISIBLE
        }

        bt_start_trip.setOnClickListener {
            if (ed_otp.text.toString() == otp) {
                foodLiveTaskServiceViewModel.callFoodieDeliveryRequest()
                dismiss()
            } else ViewUtils.showToast(context!!, resources.getString(R.string.foodie_invalid_otp), false)
        }
    }
}
