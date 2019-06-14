package com.gox.foodservice.ui.verifyotp

import android.view.View
import android.view.View.VISIBLE
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.ViewUtils
import com.gox.foodservice.R
import com.gox.foodservice.databinding.FragmentFoodieVerifyOtpBinding
import com.gox.foodservice.ui.dashboard.FoodieDashboardViewModel
import kotlinx.android.synthetic.main.fragment_foodie_verify_otp.*

class FoodieVerifyOtpDialog : BaseDialogFragment<FragmentFoodieVerifyOtpBinding>(), FoodieVerifyOTPNavigator {

    private lateinit var mBinding: FragmentFoodieVerifyOtpBinding
    private lateinit var mViewModel: FoodieDashboardViewModel

    override fun getLayout() = R.layout.fragment_foodie_verify_otp

    companion object {
        var otp: String? = null
        var requestId: String? = null
        var payable: Double? = 0.0
        fun newInstance(s: String, id: Int, pay: Double): FoodieVerifyOtpDialog {
            otp = s
            requestId = id.toString()
            payable = pay
            return FoodieVerifyOtpDialog()
        }
    }

    override fun initView(mViewDataBinding: ViewDataBinding, view: View) {
        mBinding = mViewDataBinding as FragmentFoodieVerifyOtpBinding
        val verifyOTPModule = FoodieVerifyOTPViewModel()
        verifyOTPModule.navigator = this
        mBinding.otpviewmodel = verifyOTPModule
        mViewModel = ViewModelProviders.of(activity!!).get(FoodieDashboardViewModel::class.java)

        when {
            payable!!.toDouble() > 0 -> payment_txt.visibility = VISIBLE
        }

        bt_start_trip.setOnClickListener {
            if (ed_otp.text.toString() == otp) {
                mViewModel.callFoodieDeliveryRequest()
                dismiss()
            } else ViewUtils.showToast(context!!, resources.getString(R.string.foodie_invalid_otp), false)
        }
    }
}
