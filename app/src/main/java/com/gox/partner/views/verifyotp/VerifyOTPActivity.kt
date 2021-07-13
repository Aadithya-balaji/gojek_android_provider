package com.gox.partner.views.verifyotp

import android.app.Activity
import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gox.partner.verifyotp.VerifyOTPNavigator
import com.gox.base.BuildConfig
import com.gox.base.base.BaseActivity
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityVerifyOtpBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class VerifyOTPActivity : BaseActivity<ActivityVerifyOtpBinding>(), VerifyOTPNavigator {

    private lateinit var mViewDataBinding: ActivityVerifyOtpBinding
    private lateinit var mViewModel: VerifyOTPViewModel

    private lateinit var country_code: String
    private lateinit var mobile_number: String

    override fun getLayoutId() = R.layout.activity_verify_otp

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityVerifyOtpBinding

        mViewModel = ViewModelProviders.of(this).get(VerifyOTPViewModel::class.java)
        mViewModel.navigator = this

        mViewDataBinding.verifyOTPModel = mViewModel
        mViewDataBinding.lifecycleOwner = this

        mViewDataBinding.titleToolbars.title_toolbar.title = getString(R.string.otp_verification)
        mViewDataBinding.titleToolbars.toolbar_back_img.setOnClickListener{
                onBackPressed()
            }


        val extras = intent.extras
        if (extras != null && extras.containsKey("country_code")) {
            extras.run {
                country_code = getString("country_code")!!
                mobile_number = getString("mobile")!!
                mViewModel.countryCode.value = country_code
                mViewModel.phoneNumber.value = mobile_number
            }
        }

        println("Value Get From SignUp ==>  $country_code and $mobile_number")

        apiResponseListeners()
    }

    private fun apiResponseListeners() {

        mViewModel.loadingProgress.observe(this, Observer {
            loadingObservable.value = it
        })

        mViewModel.verifyOTPResponse.observe(this, Observer {
            mViewModel.loadingProgress.value = false
            val intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        })

        mViewModel.sendOTPResponse.observe(this, Observer {
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(this, getString(R.string.otp_success), true)
        })

        mViewModel.errorResponse.observe(this, Observer {
            ViewUtils.showToast(this, it, false)
            mViewModel.loadingProgress.value = false
        })
    }


    override fun verifyOTP() {
        if (mViewDataBinding.pvOTP.value.isNullOrEmpty()) {
            ViewUtils.showToast(this, getString(R.string.error_enter_otp), false)
        } else if (mViewDataBinding.pvOTP.value.length <= 3) {
            ViewUtils.showToast(this, getString(R.string.error_enter_valid_otp), false)
        } else {
            val hashMap: HashMap<String, RequestBody> = HashMap()
            hashMap.put("country_code", country_code.toRequestBody("text/plain".toMediaTypeOrNull()))
            hashMap.put("mobile", mobile_number.toRequestBody("text/plain".toMediaTypeOrNull()))
            hashMap.put("otp", mViewDataBinding.pvOTP.value.toRequestBody("text/plain".toMediaTypeOrNull()))
            hashMap.put("salt_key", BuildConfig.SALT_KEY.toRequestBody("text/plain".toMediaTypeOrNull()))
            mViewModel.verifyOTPApiCall(hashMap)
            mViewModel.loadingProgress.value = true
        }

    }
}
