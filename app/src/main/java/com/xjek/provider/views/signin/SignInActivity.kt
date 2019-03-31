package com.xjek.provider.views.signin

import android.util.Patterns
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity<ActivitySignInBinding>(), SignInViewModel.SignInListener {

    private lateinit var signInViewModel: SignInViewModel
    private lateinit var message: String

    override fun getLayoutId(): Int {
        return R.layout.activity_sign_in
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        signInViewModel = provideViewModel {
            SignInViewModel(this)
        }
        observeViewModel()
        val activitySignInBinding = mViewDataBinding as ActivitySignInBinding
        activitySignInBinding.lifecycleOwner = this
        activitySignInBinding.signInViewModel = signInViewModel
    }

    override fun performValidation() {
        hideKeyboard()
        if (isSignInDataValid()) {
            signInViewModel.postLogin()
        } else {
            showToast(message)
        }
    }

    override fun showError(error: String) {
        showToast(error)
    }

    private fun observeViewModel() {
        observeLiveData(signInViewModel.getLoginObservable()) {
            if (signInViewModel.getLoginResponseModel()!!.statusCode.equals("200"))
                showToast("Success")
        }
    }

    private fun isSignInDataValid(): Boolean {
        if (signInViewModel.email.value.isNullOrEmpty()) {
            message = resources.getString(R.string.email_empty)
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(signInViewModel.email.value).matches()) {
            message = resources.getString(R.string.email_invalid)
            return false
        } else if (signInViewModel.password.value.isNullOrEmpty()) {
            message = resources.getString(R.string.password_empty)
            return false
        }
        return true
    }
}
