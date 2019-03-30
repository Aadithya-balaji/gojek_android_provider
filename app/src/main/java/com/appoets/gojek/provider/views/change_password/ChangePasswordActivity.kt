package com.appoets.gojek.provider.views.change_password

import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseActivity
import com.appoets.base.extensions.observeLiveData
import com.appoets.base.extensions.provideViewModel
import com.appoets.base.utils.ViewUtils
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>(),
        ChangePasswordViewModel.ChangePasswordNavigator {

    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel
    private lateinit var message: String

    override fun getLayoutId(): Int {
        return R.layout.activity_change_password
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityChangePasswordBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            ChangePasswordViewModel(this)
        }
        binding.changePasswordViewModel = viewModel
        binding.toolbar.tvToolbarTitle.text =
                resources.getString(R.string.title_change_password)
        binding.tietConfirmPassword.setOnEditorActionListener(this::onEditorAction)
        observeViewModel()
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getChangePasswordObservable()) {
            ViewUtils.showToast(applicationContext, "Success", true)
            onBackClicked()
        }
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
        if (isPasswordDataValid()) {
            viewModel.postChangePassword()
        } else {
            ViewUtils.showToast(applicationContext, message, false)
        }
    }

    private fun isPasswordDataValid(): Boolean {
        if (viewModel.oldPassword.value.isNullOrEmpty()) {
            message = resources.getString(R.string.old_password_empty)
            return false
        } else if (viewModel.newPassword.value.isNullOrEmpty()) {
            message = resources.getString(R.string.new_password_empty)
            return false
        } else if (viewModel.oldPassword.value.equals(viewModel.newPassword.value)) {
            message = resources.getString(R.string.new_password_invalid)
            return false
        } else if (!viewModel.newPassword.value.equals(viewModel.confirmPassword.value)) {
            message = resources.getString(R.string.confirm_password_invalid)
            return false
        } else
            return true
    }

    override fun onBackClicked() {
        super.onBackPressed()
    }

    override fun onChangePasswordClicked() {
        performValidation()
    }

    override fun showError(error: String) {
        ViewUtils.showToast(applicationContext, error, false)
    }
}
