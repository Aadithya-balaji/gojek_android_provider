package com.xjek.provider.views.change_password

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R

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
            ChangePasswordViewModel()
        }
        // binding.changePasswordViewModel=viewModel
        binding.toolbar.tvToolbarTitle.text =
                resources.getString(R.string.title_change_password)
        binding.toolbar.ivToolbarBack.setOnClickListener(this::onBackClicked)
        binding.tietConfirmPassword.setOnEditorActionListener(this::onEditorAction)
        observeViewModel()
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getChangePasswordObservable()) {
            loadingObservable.value = false
            message = if (!it.message.isNullOrBlank()) it.message else "Success"
            ViewUtils.showToast(applicationContext, message, true)
            onBackClicked(binding.toolbar.ivToolbarBack)
        }
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
        ViewUtils.hideSoftInputWindow(this)
        if (isPasswordDataValid()) {
            loadingObservable.value = true
            viewModel.postChangePassword()
        } else {
            ViewUtils.showToast(applicationContext, message, false)
        }
    }

    private fun isPasswordDataValid(): Boolean {
        return when {
            viewModel.oldPassword.value.isNullOrBlank() -> {
                message = resources.getString(R.string.old_password_empty)
                false
            }
            viewModel.newPassword.value.isNullOrBlank() -> {
                message = resources.getString(R.string.new_password_empty)
                false
            }
            viewModel.oldPassword.value!!.trim() == viewModel.newPassword.value!!.trim() -> {
                message = resources.getString(R.string.new_password_invalid)
                false
            }
            viewModel.newPassword.value!!.trim() != viewModel.confirmPassword.value?.trim() -> {
                message = resources.getString(R.string.confirm_password_invalid)
                false
            }
            else -> true
        }
    }

    override fun onChangePasswordClicked() {
        performValidation()
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}
