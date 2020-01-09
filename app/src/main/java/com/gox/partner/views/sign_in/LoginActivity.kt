package com.gox.partner.views.sign_in

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Patterns
import android.util.TypedValue
import android.view.View
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
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.extensions.writePreferences
import com.gox.base.utils.Logger
import com.gox.base.utils.ViewUtils
import com.gox.partner.BuildConfig
import com.gox.partner.R
import com.gox.partner.databinding.ActivityLoginBinding
import com.gox.partner.utils.Country
import com.gox.partner.utils.Enums
import com.gox.partner.views.countrypicker.CountryCodeActivity
import com.gox.partner.views.dashboard.DashBoardActivity
import com.gox.partner.views.forgot_password.ForgotPasswordActivity
import com.gox.partner.views.signup.RegistrationActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginViewModel.LoginNavigator, ViewUtils.ViewCallBack {

    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mViewModel: LoginViewModel

    private lateinit var callbackManager: CallbackManager
    private lateinit var message: String

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var isFacebookLoginClicked = false
    private var isEmailLogin: Boolean = false

    override fun getLayoutId() = R.layout.activity_login

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityLoginBinding
        mBinding.lifecycleOwner = this
        mViewModel = provideViewModel { LoginViewModel() }
        mViewModel.navigator = this
        mBinding.loginViewModel = mViewModel

        observeViewModel()


        val resultIntent = Intent()

        println("RRR ::  FCMToken = ${BaseApplication.getCustomPreference!!.getString(PreferencesKey.DEVICE_TOKEN, "123")}")

        detectDefaultCountry(resultIntent)

        //    setDefaultCountry()

    }

    private fun detectDefaultCountry(resultIntent: Intent) {
        val tm = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val countryModel = Country.getCountryByISO(tm.networkCountryIso)
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
        observeLiveData(mViewModel.loginLiveData) {
            loadingObservable.value = false
            message = if (!it.message.isBlank()) it.message else "Success"
            ViewUtils.showToast(applicationContext, message, true)
            writePreferences(PreferencesKey.ACCESS_TOKEN, it.responseData.accessToken)
            writePreferences(PreferencesKey.IS_ONLINE, it.responseData.user.isOnline)
            val dashBoardIntent = Intent(applicationContext, DashBoardActivity::class.java)
            dashBoardIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            openActivity(dashBoardIntent, false)
        }
    }

    private fun performValidation() {
        ViewUtils.hideSoftInputWindow(this)
        if (isSignInDataValid()) {
            loadingObservable.value = true
            mViewModel.postLogin((isEmailLogin))
        } else ViewUtils.showToast(applicationContext, message, false)
    }


    fun setDefaultCountry(){
        val dr = ContextCompat.getDrawable(this, R.drawable.flag_india)
        val bitmap = (dr as BitmapDrawable).bitmap
        var width:Int=0
        var height:Int=0
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            width = dpsToPixels(this@LoginActivity, 28)
            height = dpsToPixels(this@LoginActivity, 8)
        }else{
            width = dpsToPixels(this@LoginActivity, 15)
            height = dpsToPixels(this@LoginActivity, 15)
        }
        val d = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true))
        mBinding.countrycodeRegisterEt.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)
    }


    private fun dpsToPixels(activity: Activity, dps: Int): Int    {
        val r = activity.resources
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }
    private fun isSignInDataValid(): Boolean {
        if (isEmailLogin) {
            if (mViewModel.email.value.isNullOrBlank()) {
                message = resources.getString(R.string.email_empty)
                return false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(mViewModel.email.value!!.trim())
                            .matches()) {
                message = resources.getString(R.string.email_invalid)
                return false
            }
        } else if (mViewModel.countryCode.value.isNullOrBlank()) {
            message = resources.getString(R.string.country_code_empty)
            return false
        } else if (mViewModel.phoneNumber.value.isNullOrBlank()) {
            message = resources.getString(R.string.phone_number_empty)
            return false
        }

        if (mViewModel.password.value.isNullOrBlank()) {
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
                mViewModel.postSocialLogin(true, account.id!!)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun handleCountryCodePickerResult(data: Intent) {
        val countryCode = data.getStringExtra("countryCode")
        mViewModel.countryCode.value = countryCode
        val countryFlag = data.getIntExtra("countryFlag", -1)
        val leftDrawable = ContextCompat.getDrawable(this, countryFlag)
        if (leftDrawable != null) {
            val bitmap = (leftDrawable as BitmapDrawable).bitmap
            var width:Int=0
            var height:Int=0
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                width = dpsToPixels(this@LoginActivity, 8)
                height = dpsToPixels(this@LoginActivity, 8)
            }else{
                width = dpsToPixels(this@LoginActivity, 15)
                height = dpsToPixels(this@LoginActivity, 15)
            }
            val drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true))
            mBinding.countrycodeRegisterEt.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
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

    override fun changeLoginViaPhone() {
        isEmailLogin = false
        mBinding.passwordRegisterEt.setText("")
        mBinding.tilEmail.visibility = View.GONE
        mBinding.llPhoneNumber.visibility = View.VISIBLE
        mBinding.phonenumberRegisterEt.requestFocus()
        mBinding.phoneSigninImgview.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
        mBinding.mailSinginImgview.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey))
        mBinding.phoneSigninImgview.setBackgroundResource((R.drawable.login_icon_selected_bg))
        mBinding.mailSinginImgview.setBackgroundResource((R.drawable.login_icon_normal_bg))
    }

    override fun changeLoginViaMail() {
        isEmailLogin = true
        mBinding.passwordRegisterEt.setText("")
        mBinding.llPhoneNumber.visibility = View.GONE
        mBinding.tilEmail.visibility = View.VISIBLE
        mBinding.emailidRegisterEt.requestFocus()
        mBinding.phoneSigninImgview.setColorFilter(ContextCompat.getColor(this, R.color.dark_grey))
        mBinding.mailSinginImgview.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent))
        mBinding.phoneSigninImgview.setBackgroundResource((R.drawable.login_icon_normal_bg))
        mBinding.mailSinginImgview.setBackgroundResource((R.drawable.login_icon_selected_bg))
    }

    override fun onCountryCodeClicked() = startActivityForResult(Intent(applicationContext,
            CountryCodeActivity::class.java), Enums.RC_COUNTRY_CODE_PICKER)

    override fun onForgotPasswordClicked() = openActivity(ForgotPasswordActivity::class.java, false)

    override fun onRegistrationClicked() = openActivity(RegistrationActivity::class.java, false)

    override fun onLoginClicked() = performValidation()

    override fun onGoogleLoginClicked() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.google_signin_server_client_id)).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        startActivityForResult(mGoogleSignInClient!!.signInIntent, Enums.RC_GOOGLE_SIGN_IN)
    }

    override fun onFacebookLoginClicked() {
        val accessToken = AccessToken.getCurrentAccessToken()
        if (accessToken != null && !accessToken.isExpired) {
            Logger.i(TAG, accessToken.token)
            loadingObservable.value = true
            mViewModel.postSocialLogin(false, accessToken.userId)
        } else {
            isFacebookLoginClicked = true
            callbackManager = CallbackManager.Factory.create()
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    Logger.i(TAG, loginResult.accessToken.token)
                    mViewModel.postSocialLogin(false, loginResult.accessToken.userId)
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
        ViewUtils.showAlert(this, message, resources.getString(R.string.action_sign_up), resources.getString(R.string.action_cancel), this)
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }

    override fun onPositiveButtonClick(dialog: DialogInterface) {
        if (mGoogleSignInClient != null)
            mGoogleSignInClient!!.revokeAccess()
        onRegistrationClicked()
    }

    override fun onNegativeButtonClick(dialog: DialogInterface) {
        if (mGoogleSignInClient != null)
            mGoogleSignInClient!!.revokeAccess()
    }

    companion object {
        private val TAG: String = LoginActivity::class.java.simpleName
    }
}