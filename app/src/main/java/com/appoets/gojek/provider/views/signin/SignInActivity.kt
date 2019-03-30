package com.appoets.gojek.provider.views.signin

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseActivity
import com.appoets.base.extensions.observeLiveData
import com.appoets.base.extensions.provideViewModel
import com.appoets.base.utils.Logger
import com.appoets.base.utils.ViewUtils
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivitySignInBinding
import com.appoets.gojek.provider.utils.Constant
import com.appoets.gojek.provider.utils.Enums
import com.appoets.gojek.provider.views.change_password.ChangePasswordActivity
import com.appoets.gojek.provider.views.countrypicker.CountryCodeActivity
import com.appoets.gojek.provider.views.dashboard.DashBoardActivity
import com.appoets.gojek.provider.views.signup.SignupActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import java.util.*

class SignInActivity : BaseActivity<ActivitySignInBinding>(), SignInViewModel.SignInNavigator {

    private lateinit var activitySignInBinding: ActivitySignInBinding
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var callbackManager: CallbackManager
    private lateinit var message: String
    private var isFacebookLoginClicked = false

    override fun getLayoutId(): Int {
        return R.layout.activity_sign_in
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activitySignInBinding = mViewDataBinding as ActivitySignInBinding
        activitySignInBinding.lifecycleOwner = this
        signInViewModel = provideViewModel {
            SignInViewModel(this)
        }
        activitySignInBinding.signInViewModel = signInViewModel
        observeViewModel()
    }

    private fun observeViewModel() {
        observeLiveData(signInViewModel.getLoginObservable()) {
            ViewUtils.showToast(applicationContext, "Success", true)
            Constant.accessToken = it.responseData.accessToken
            val dashBoardIntent = Intent(applicationContext, DashBoardActivity::class.java)
            dashBoardIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(dashBoardIntent)
        }
    }

    private fun performValidation() {
        hideKeyboard()
        if (isSignInDataValid()) {
            signInViewModel.postLogin((activitySignInBinding.rgSignin.checkedRadioButtonId == R.id.rb_email))
        } else {
            ViewUtils.showToast(applicationContext, message, false)
        }
    }

    private fun isSignInDataValid(): Boolean {
        if (activitySignInBinding.rgSignin.checkedRadioButtonId == R.id.rb_email) {
            if (signInViewModel.email.value.isNullOrEmpty()) {
                message = resources.getString(R.string.email_empty)
                return false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(signInViewModel.email.value.toString().trim())
                            .matches()) {
                message = resources.getString(R.string.email_invalid)
                return false
            }
        } else {
            if (signInViewModel.countryCode.value.isNullOrEmpty()) {
                message = resources.getString(R.string.country_code_empty)
                return false
            } else if (signInViewModel.phoneNumber.value.isNullOrEmpty()) {
                message = resources.getString(R.string.phone_number_empty)
                return false
            }
        }
        if (signInViewModel.password.value.isNullOrEmpty()) {
            message = resources.getString(R.string.password_empty)
            return false
        }
        return true
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "GoogleSignInResult:Error_Code=" + e.statusCode)
        }
    }

    private fun handleCountryCodePickerResult(data: Intent) {
        val countryCode = data.getStringExtra("countryCode")
        signInViewModel.countryCode.value = countryCode
        val countryFlag = data.getIntExtra("countryFlag", -1)
        val leftDrawable = ContextCompat.getDrawable(this, countryFlag)
        if (leftDrawable != null) {
            val bitmap = (leftDrawable as BitmapDrawable).bitmap
            val drawable = BitmapDrawable(resources,
                    Bitmap.createScaledBitmap(bitmap, 64, 64, true))
            activitySignInBinding.countrycodeRegisterEt
                    .setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                            null, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (isFacebookLoginClicked)
            callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Enums.RC_GOOGLE_SIGN_IN -> {
                    // The Task returned from this call is always completed, no need to attach
                    // a listener.
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    handleGoogleSignInResult(task)
                }
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
                activitySignInBinding.tilEmail.visibility = View.GONE
                activitySignInBinding.llPhoneNumber.visibility = View.VISIBLE
            }
            R.id.rb_email -> {
                activitySignInBinding.llPhoneNumber.visibility = View.GONE
                activitySignInBinding.tilEmail.visibility = View.VISIBLE
            }
        }
    }

    override fun onCountryCodeClicked() {
        val intent = Intent(applicationContext, CountryCodeActivity::class.java)
        startActivityForResult(intent, Enums.RC_COUNTRY_CODE_PICKER)
    }

    override fun onForgotPasswordClicked() {
        val changePasswordIntent = Intent(applicationContext, ChangePasswordActivity::class.java)
        startActivity(changePasswordIntent)
    }

    override fun onSignUpClicked() {
        val signUpIntent = Intent(applicationContext, SignupActivity::class.java)
        startActivity(signUpIntent)
        finish()
    }

    override fun onSignInClicked() {
        performValidation()
    }

    override fun onGoogleSignInClicked() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val googleSignInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(googleSignInIntent, Enums.RC_GOOGLE_SIGN_IN)
    }

    override fun onFacebookLoginClicked() {
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            // TODO
        } else {
            isFacebookLoginClicked = true
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResult: LoginResult) {
                            Logger.i(TAG, loginResult.accessToken.token)
                        }

                        override fun onCancel() {
                            Logger.i(TAG, "FacebookLoginResult:Cancelled")
                        }

                        override fun onError(exception: FacebookException) {
                            Logger.e(TAG, "FacebookLoginResult:Error=" + exception.message)
                        }
                    })
        }
    }

    override fun showError(error: String) {
        ViewUtils.showToast(applicationContext, error, false)
    }

    companion object {
        private val TAG: String = SignInActivity::class.java.simpleName
    }
}
