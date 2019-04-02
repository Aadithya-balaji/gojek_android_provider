package com.xjek.provider.views.reset_password

import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityResetPasswordBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.network.WebApiConstants
import com.xjek.provider.views.signin.SignInActivity

class ResetPasswordActivity : BaseActivity<ActivityResetPasswordBinding>(),
        ResetPasswordViewModel.ResetPasswordNavigator {

    private lateinit var binding: ActivityResetPasswordBinding
    private lateinit var viewModel: ResetPasswordViewModel
    private lateinit var message: String

    override fun getLayoutId(): Int {
        return R.layout.activity_reset_password
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityResetPasswordBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            ResetPasswordViewModel()
        }
        viewModel.navigator = this
        binding.resetPasswordViewModel = viewModel

        binding.toolbar.tvToolbarTitle.text = resources.getString(R.string.title_reset_password)
        binding.tvResetLabel.text = resources.getString(R.string.enter_otp)

        binding.toolbar.ivToolbarBack.setOnClickListener(this::onBackClicked)
        binding.tietOtp.setOnEditorActionListener(this::onEditorAction)
        binding.tietConfirmPassword.setOnEditorActionListener(this::onEditorAction)

        binding.llPassword.visibility = View.GONE
        binding.mbReset.visibility = View.GONE
        binding.tilOtp.visibility = View.VISIBLE
        binding.ibOtp.visibility = View.VISIBLE

        observeViewModel()

        handleIntentData()
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getResetPasswordObservable()) {
            ViewUtils.showToast(applicationContext, "Success", true)
            val signInIntent = Intent(applicationContext, SignInActivity::class.java)
            signInIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(signInIntent)
        }
    }

    private fun handleIntentData() {
        viewModel.accountType = intent.getStringExtra(WebApiConstants.ResetPassword.ACCOUNT_TYPE)
        viewModel.username = intent.getStringExtra(WebApiConstants.ResetPassword.USERNAME)
        viewModel.receivedOtp = intent.getStringExtra(WebApiConstants.ResetPassword.OTP)
        viewModel.otp.value = intent.getStringExtra(WebApiConstants.ResetPassword.OTP)
    }

    private fun onBackClicked(view: View) {
        super.onBackPressed()
    }

    private fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            performValidation()
            return true
        }
        return false
    }

    private fun performValidation() {
        hideKeyboard()
        if (binding.ibOtp.visibility == View.VISIBLE) {
            if (isOtpDataValid()) {
                binding.tvResetLabel.text = resources.getString(R.string.create_new_password)
                binding.tilOtp.visibility = View.GONE
                binding.ibOtp.visibility = View.GONE
                binding.llPassword.visibility = View.VISIBLE
                binding.mbReset.visibility = View.VISIBLE
            } else {
                ViewUtils.showToast(applicationContext, message, false)
            }
        } else if (binding.mbReset.visibility == View.VISIBLE) {
            if (isPasswordDataValid()) {
                viewModel.postResetPassword(true)
            } else {
                ViewUtils.showToast(applicationContext, message, false)
            }
        }
    }

    private fun isOtpDataValid(): Boolean {
        return when {
            viewModel.otp.value.isNullOrBlank() -> {
                message = resources.getString(R.string.error_otp_empty)
                false
            }
            viewModel.otp.value!!.trim() != viewModel.receivedOtp -> {
                message = resources.getString(R.string.error_otp_invalid)
                false
            }
            else -> true
        }
    }

    private fun isPasswordDataValid(): Boolean {
        return when {
            viewModel.newPassword.value.isNullOrBlank() -> {
                message = resources.getString(R.string.new_password_empty)
                false
            }
            viewModel.newPassword.value!!.trim() != viewModel.confirmPassword.value!!.trim() -> {
                message = resources.getString(R.string.confirm_password_invalid)
                false
            }
            else -> true
        }
    }

    override fun onOtpClicked() {
        performValidation()
    }

    override fun onResetPasswordClicked() {
        performValidation()
    }

    override fun showError(error: String) {
        ViewUtils.showToast(applicationContext, message, false)
    }
}
