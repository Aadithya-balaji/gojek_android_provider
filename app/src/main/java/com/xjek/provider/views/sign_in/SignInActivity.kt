package com.xjek.provider.views.sign_in

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.extensions.writePreferences
import com.xjek.base.utils.Logger
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.BuildConfig
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySignInBinding
import com.xjek.provider.utils.Enums
import com.xjek.provider.views.countrypicker.CountryCodeActivity
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.forgot_password.ForgotPasswordActivity
import com.xjek.provider.views.signup.SignupActivity
import java.util.*

class SignInActivity : BaseActivity<ActivitySignInBinding>(), SignInViewModel.SignInNavigator, ViewUtils.ViewCallBack {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: SignInViewModel
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var callbackManager: CallbackManager
    private lateinit var message: String
    private var isFacebookLoginClicked = false

    override fun getLayoutId() = R.layout.activity_sign_in

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySignInBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel { SignInViewModel() }
        viewModel.navigator = this
        binding.signInViewModel = viewModel

        observeViewModel()

        val resultIntent = Intent()
        resultIntent.putExtra("countryName", "India")
        resultIntent.putExtra("countryCode", "+91")
        resultIntent.putExtra("countryFlag", R.drawable.flag_in)
        handleCountryCodePickerResult(resultIntent)

        /*if (BuildConfig.DEBUG) {
            viewModel.email.value = "toni@yopmail.com"
            viewModel.password.value = "112233"
        }*/
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getLoginObservable()) {
            loadingObservable.value = false
            message = if (!it.message.isBlank()) it.message else "Success"
            ViewUtils.showToast(applicationContext, message, true)
            writePreferences(PreferencesKey.ACCESS_TOKEN, it.responseData.accessToken)
            writePreferences(PreferencesKey.IS_ONLINE, it.responseData.user.isOnline)
            val dashBoardIntent = Intent(applicationContext, DashBoardActivity::class.java)
            dashBoardIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            launchNewActivity(dashBoardIntent, false)
        }
    }

    private fun performValidation() {
        ViewUtils.hideSoftInputWindow(this)
        if (isSignInDataValid()) {
            loadingObservable.value = true
            viewModel.postLogin((binding.rgSignin.checkedRadioButtonId == R.id.rb_email))
        } else ViewUtils.showToast(applicationContext, message, false)
    }

    private fun isSignInDataValid(): Boolean {
        if (binding.rgSignin.checkedRadioButtonId == R.id.rb_email) {
            if (viewModel.email.value.isNullOrBlank()) {
                message = resources.getString(R.string.email_empty)
                return false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(viewModel.email.value!!.trim())
                            .matches()) {
                message = resources.getString(R.string.email_invalid)
                return false
            }
        } else {
            if (viewModel.countryCode.value.isNullOrBlank()) {
                message = resources.getString(R.string.country_code_empty)
                return false
            } else if (viewModel.phoneNumber.value.isNullOrBlank()) {
                message = resources.getString(R.string.phone_number_empty)
                return false
            }
        }
        if (viewModel.password.value.isNullOrBlank()) {
            message = resources.getString(R.string.password_empty)
            return false
        }
        return true
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null && account.id != null) {
                Logger.i(TAG, account.id!!)
                loadingObservable.value = true
                viewModel.postSocialLogin(true, account.id!!)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun handleCountryCodePickerResult(data: Intent) {
        val countryCode = data.getStringExtra("countryCode")
        viewModel.countryCode.value = countryCode
        val countryFlag = data.getIntExtra("countryFlag", -1)
        val leftDrawable = ContextCompat.getDrawable(this, countryFlag)
        if (leftDrawable != null) {
            val bitmap = (leftDrawable as BitmapDrawable).bitmap
            val width:Int = resources.getDimension(R.dimen._25sdp).toInt()
            val height:Int = resources.getDimension(R.dimen._25sdp).toInt()
            val drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true))
            binding.countrycodeRegisterEt.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (isFacebookLoginClicked)
            callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                Enums.RC_GOOGLE_SIGN_IN -> handleGoogleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
                Enums.RC_COUNTRY_CODE_PICKER -> if (data != null && data.extras != null) handleCountryCodePickerResult(data)
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup, checkedId: Int) {
        when (checkedId) {
            R.id.rb_phone -> {
                binding.tilEmail.visibility = View.GONE
                binding.llPhoneNumber.visibility = View.VISIBLE
            }
            R.id.rb_email -> {
                binding.llPhoneNumber.visibility = View.GONE
                binding.tilEmail.visibility = View.VISIBLE
                binding.tilEmail.requestFocus()
            }
        }
    }

    override fun onCountryCodeClicked() {
        startActivityForResult(Intent(applicationContext, CountryCodeActivity::class.java), Enums.RC_COUNTRY_CODE_PICKER)
    }

    override fun onForgotPasswordClicked() {
        launchNewActivity(ForgotPasswordActivity::class.java, false)
    }

    override fun onSignUpClicked() {
        launchNewActivity(SignupActivity::class.java, false)
    }

    override fun onSignInClicked() {
        performValidation()
    }

    override fun onGoogleSignInClicked() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(mGoogleSignInClient!!.signInIntent, Enums.RC_GOOGLE_SIGN_IN)
    }

    override fun onFacebookLoginClicked() {
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            Logger.i(TAG, accessToken.token)
            loadingObservable.value = true
            viewModel.postSocialLogin(false, accessToken.userId)
        } else {
            isFacebookLoginClicked = true
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"))
            LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult> {
                        override fun onSuccess(loginResult: LoginResult) {
                            Logger.i(TAG, loginResult.accessToken.token)
                            viewModel.postSocialLogin(false, loginResult.accessToken.userId)
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

    override fun showAlert(message: String) {
        loadingObservable.value = false
        ViewUtils.showAlert(this, message, resources.getString(R.string.action_sign_up),
                resources.getString(R.string.action_cancel), this)
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }

    override fun onPositiveButtonClick(dialog: DialogInterface) {
        if (mGoogleSignInClient != null)
            mGoogleSignInClient!!.revokeAccess()
        onSignUpClicked()
    }

    override fun onNegativeButtonClick(dialog: DialogInterface) {
        if (mGoogleSignInClient != null)
            mGoogleSignInClient!!.revokeAccess()
    }

    companion object {
        private val TAG: String = SignInActivity::class.java.simpleName
    }
}
