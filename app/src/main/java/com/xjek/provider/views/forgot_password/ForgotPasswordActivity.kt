package com.xjek.provider.views.forgot_password

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.utils.Enums
import com.xjek.provider.views.countrypicker.CountryCodeActivity
import com.xjek.provider.views.reset_password.ResetPasswordActivity

class ForgotPasswordActivity : BaseActivity<ActivityForgotPasswordBinding>(),
        ForgotPasswordViewModel.ForgotPasswordNavigator {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var viewModel: ForgotPasswordViewModel
    private var isEmailLogin: Boolean = false
    private lateinit var message: String

    override fun getLayoutId(): Int {
        return R.layout.activity_forgot_password
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityForgotPasswordBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            ForgotPasswordViewModel()
        }
        viewModel.navigator = this
        binding.forgotPasswordViewModel = viewModel

        binding.toolbar.tvToolbarTitle.text = resources.getString(R.string.title_forgot_password)

        binding.toolbar.ivToolbarBack.setOnClickListener(this::onBackClicked)
        binding.tietEmail.setOnEditorActionListener(this::onEditorAction)

        observeViewModel()
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getForgotPasswordObservable()) {
            loadingObservable.value = false
            message = if (!it.message.isNullOrBlank()) it.message else "Success"
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
            launchNewActivity(resetPasswordIntent, false)
        }
    }

    private fun onBackClicked(view: View) {
        super.onBackPressed()
    }

    private fun performValidation() {
        ViewUtils.hideSoftInputWindow(this)
        if (isResetDataValid()) {
            loadingObservable.value = true
            viewModel.postForgotPassword(isEmailLogin)
        } else {
            ViewUtils.showToast(applicationContext, message, false)
        }
    }

    private fun isResetDataValid(): Boolean {
        if (binding.rgReset.checkedRadioButtonId == R.id.rb_phone) {
            return when {
                viewModel.countryCode.value.isNullOrBlank() -> {
                    message = resources.getString(R.string.country_code_empty)
                    false
                }
                viewModel.phoneNumber.value.isNullOrBlank() -> {
                    message = resources.getString(R.string.phone_number_empty)
                    false
                }
                else -> true
            }
        } else {
            return if (viewModel.email.value.isNullOrBlank()) {
                message = resources.getString(R.string.email_empty)
                false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(viewModel.email.value!!.trim()).matches()) {
                message = resources.getString(R.string.email_invalid)
                false
            } else
                true
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
        viewModel.countryCode.value = countryCode
        val countryFlag = data.getIntExtra("countryFlag", -1)
        val leftDrawable = ContextCompat.getDrawable(this, countryFlag)
        if (leftDrawable != null) {
            val bitmap = (leftDrawable as BitmapDrawable).bitmap
            val drawable = BitmapDrawable(resources,
                    Bitmap.createScaledBitmap(bitmap, 64, 64, true))
            binding.tietCountryCode
                    .setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                            null, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Enums.RC_COUNTRY_CODE_PICKER -> {
                    if (data != null && data.extras != null) {
                        handleCountryCodePickerResult(data)
                    }
                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.rb_phone -> {
                isEmailLogin = false
                binding.tilEmail.visibility = View.GONE
                binding.llPhoneNumber.visibility = View.VISIBLE
            }
            R.id.rb_email -> {
                isEmailLogin = true
                binding.llPhoneNumber.visibility = View.GONE
                binding.tilEmail.visibility = View.VISIBLE
            }
        }
    }

    override fun onCountryCodeClicked() {
        val intent = Intent(applicationContext, CountryCodeActivity::class.java)
        startActivityForResult(intent, Enums.RC_COUNTRY_CODE_PICKER)
    }

    override fun onForgotPasswordClicked() {
        performValidation()
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}
