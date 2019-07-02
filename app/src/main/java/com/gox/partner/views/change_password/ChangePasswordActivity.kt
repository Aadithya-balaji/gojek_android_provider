package com.gox.partner.views.change_password

import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseActivity
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityChangePasswordBinding
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : BaseActivity<ActivityChangePasswordBinding>(),
        ChangePasswordViewModel.ChangePasswordNavigator {

    private lateinit var mBinding: ActivityChangePasswordBinding
    private lateinit var mViewModel: ChangePasswordViewModel
    private lateinit var message: String

    override fun getLayoutId() = R.layout.activity_change_password

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityChangePasswordBinding
        mBinding.lifecycleOwner = this
        mViewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java)
        mViewModel.navigator = this
        mViewDataBinding.changePasswordViewModel = mViewModel

        mViewModel = provideViewModel {
            ChangePasswordViewModel()
        }
        mBinding.toolbar.tvToolbarTitle.text = resources.getString(R.string.title_change_password)
        mBinding.toolbar.ivToolbarBack.setOnClickListener(this::onBackClicked)
        mBinding.tietConfirmPassword.setOnEditorActionListener(this::onEditorAction)
        observeViewModel()

        btProceed.setOnClickListener { performValidation() }
    }

    private fun observeViewModel() {
        observeLiveData(mViewModel.getChangePasswordObservable()) {
            loadingObservable.value = false
            message = if (!it.message.isBlank()) it.message else "Success"
            ViewUtils.showToast(applicationContext, message, true)
            onBackClicked(mBinding.toolbar.ivToolbarBack)
        }
    }

    private fun onBackClicked(view: View) = super.onBackPressed()

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
            mViewModel.postChangePassword()
        } else ViewUtils.showToast(applicationContext, message, false)
    }

    private fun isPasswordDataValid() = when {
        mViewModel.oldPassword.value.isNullOrBlank() -> {
            message = resources.getString(R.string.old_password_empty)
            false
        }
        mViewModel.newPassword.value.isNullOrBlank() -> {
            message = resources.getString(R.string.new_password_empty)
            false
        }
        mViewModel.oldPassword.value!!.trim() == mViewModel.newPassword.value!!.trim() -> {
            message = resources.getString(R.string.new_password_invalid)
            false
        }
        mViewModel.newPassword.value!!.trim() != mViewModel.confirmPassword.value?.trim() -> {
            message = resources.getString(R.string.confirm_password_invalid)
            false
        }
        else -> true
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}