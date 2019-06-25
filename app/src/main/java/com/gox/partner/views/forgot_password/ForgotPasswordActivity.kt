package com.gox.partner.views.forgot_password

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.telephony.TelephonyManager
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityForgotPasswordBinding
import com.gox.partner.network.WebApiConstants
import com.gox.partner.utils.Country
import com.gox.partner.utils.Enums
import com.gox.partner.views.countrypicker.CountryCodeActivity
import com.gox.partner.views.reset_password.ResetPasswordActivity

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>(),
        ForgotPasswordViewModel.ForgotPasswordNavigator {

    private lateinit var mBinding: ActivityForgotPasswordBinding
    private lateinit var mViewModel: ForgotPasswordViewModel

    private var isEmailLogin: Boolean = false
    private lateinit var message: String

    override fun getLayoutId() = R.layout.activity_forgot_password

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityForgotPasswordBinding
        mBinding.lifecycleOwner = this
        mViewModel = provideViewModel {
            ForgotPasswordViewModel()
        }
        mViewModel.navigator = this
        mBinding.forgotPasswordViewModel = mViewModel

        mBinding.toolbar.tvToolbarTitle.text = resources.getString(R.string.title_forgot_password)

        mBinding.toolbar.ivToolbarBack.setOnClickListener(this::onBackClicked)
        mBinding.tietEmail.setOnEditorActionListener(this::onEditorAction)

        observeViewModel()

        val tm = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val countryModel = Country.getCountryByISO(tm.networkCountryIso)

        val resultIntent = Intent()

        if (countryModel == null) {
            resultIntent.putExtra("countryName", "India")
            resultIntent.putExtra("countryCode", "+91")
            resultIntent.putExtra("countryFlag", R.drawable.flag_in)
        } else {
            resultIntent.putExtra("countryName", countryModel.name)
            resultIntent.putExtra("countryCode", countryModel.dialCode)
            resultIntent.putExtra("countryFlag", countryModel.flag)
        }
        handleCountryCodePickerResult(resultIntent)

    }

    private fun observeViewModel() {
        observeLiveData(mViewModel.getForgotPasswordObservable()) {
            loadingObservable.value = false
            message = if (!it.message.isBlank()) it.message else "Success"
            ViewUtils.showToast(applicationContext, message, true)
            val resetPasswordIntent = Intent(applicationContext, ResetPasswordActivity::class.java)
            resetPasswordIntent.putExtra(WebApiConstants.ResetPassword.ACCOUNT_TYPE, it
                    .responseData.accountType)
            resetPasswordIntent.putExtra(WebApiConstants.ResetPassword.COUNTRY_CODE, it
                    .responseData.countryCode)
            resetPasswordIntent.putExtra(WebApiConstants.ResetPassword.USERNAME, it
                    .responseData.username)
            resetPasswordIntent.putExtra(WebApiConstants.ResetPassword.OTP, it
                    .responseData.otp)
            openActivity(resetPasswordIntent, false)
        }
    }

    private fun onBackClicked(view: View) = super.onBackPressed()

    private fun performValidation() {
        ViewUtils.hideSoftInputWindow(this)
        if (isResetDataValid()) {
            loadingObservable.value = true
            mViewModel.postForgotPassword(isEmailLogin)
        } else ViewUtils.showToast(applicationContext, message, false)
    }

    private fun isResetDataValid(): Boolean {
        if (mBinding.rgReset.checkedRadioButtonId == R.id.rb_phone) {
            return when {
                mViewModel.countryCode.value.isNullOrBlank() -> {
                    message = resources.getString(R.string.country_code_empty)
                    false
                }
                mViewModel.phoneNumber.value.isNullOrBlank() -> {
                    message = resources.getString(R.string.phone_number_empty)
                    false
                }
                else -> true
            }
        } else {
            return if (mViewModel.email.value.isNullOrBlank()) {
                message = resources.getString(R.string.email_empty)
                false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(mViewModel.email.value!!.trim()).matches()) {
                message = resources.getString(R.string.email_invalid)
                false
            } else true
        }
    }

    private fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            performValidation()
            return true
        }
        return false
    }

    private fun handleCountryCodePickerResult(data: Intent) {
        val countryCode = data.getStringExtra("countryCode")
        mViewModel.countryCode.value = countryCode
        val countryFlag = data.getIntExtra("countryFlag", -1)
        val leftDrawable = ContextCompat.getDrawable(this, countryFlag)
        if (leftDrawable != null) {
            val bitmap = (leftDrawable as BitmapDrawable).bitmap
            val width: Int = resources.getDimension(R.dimen.flag_width).toInt()
            val height: Int = resources.getDimension(R.dimen.flag_height).toInt()
            val drawable = BitmapDrawable(resources,
                    Bitmap.createScaledBitmap(bitmap, width, height
                            , true))
            mBinding.tietCountryCode
                    .setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                            null, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            Enums.RC_COUNTRY_CODE_PICKER -> if (data != null && data.extras != null) handleCountryCodePickerResult(data)
        }
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.rb_phone -> {
                isEmailLogin = false
                mBinding.tilEmail.visibility = View.GONE
                mBinding.llPhoneNumber.visibility = View.VISIBLE
            }
            R.id.rb_email -> {
                isEmailLogin = true
                mBinding.llPhoneNumber.visibility = View.GONE
                mBinding.tilEmail.visibility = View.VISIBLE
            }
        }
    }

    override fun onCountryCodeClicked() =
            startActivityForResult(Intent(applicationContext, CountryCodeActivity::class.java), Enums.RC_COUNTRY_CODE_PICKER)

    override fun onForgotPasswordClicked() = performValidation()

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}
