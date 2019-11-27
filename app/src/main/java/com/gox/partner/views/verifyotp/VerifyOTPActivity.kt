package com.gox.partner.views.verifyotp

import android.app.Activity
import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gox.app.ui.verifyotp.VerifyOTPNavigator
import com.gox.app.ui.verifyotp.VerifyOTPViewModel
import com.gox.base.BuildConfig
import com.gox.base.base.BaseActivity
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityVerifyOtpBinding
import okhttp3.MediaType
import okhttp3.RequestBody

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

        mViewDataBinding.run {
            titleToolbar.navigationIcon = getDrawable(R.drawable.back_arrow)
            titleToolbar.title = getString(R.string.otp_verification)
            titleToolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }
        setSupportActionBar(mViewDataBinding.titleToolbar)

        val extras = intent.extras
        if (extras != null && extras.containsKey("country_code")) {
            extras.run {
                country_code = getString("country_code")
                mobile_number = getString("mobile")
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
            hashMap.put("country_code", RequestBody.create(MediaType.parse("text/plain"), country_code))
            hashMap.put("mobile", RequestBody.create(MediaType.parse("text/plain"), mobile_number))
            hashMap.put("otp", RequestBody.create(MediaType.parse("text/plain"), mViewDataBinding.pvOTP.value))
            hashMap.put("salt_key", RequestBody.create(MediaType.parse("text/plain"), BuildConfig.SALT_KEY))
            mViewModel.verifyOTPApiCall(hashMap)
            mViewModel.loadingProgress.value = true
        }

    }
}
