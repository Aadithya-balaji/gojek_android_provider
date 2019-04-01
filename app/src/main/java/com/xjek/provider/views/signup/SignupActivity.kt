package com.xjek.provider.views.signup

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.databinding.ViewDataBinding
import com.facebook.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityRegisterBinding
import com.xjek.provider.views.countrypicker.CountryCodeActivity
import com.xjek.provider.views.document.DocumentActivity
import com.xjek.provider.views.signin.SignInActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.xjek.provider.utils.Enums
import com.xjek.provider.utils.Enums.Companion.FB_ACCOUNT_KIT_CODE
import java.util.*
import android.os.Bundle
import android.util.Log
import com.xjek.base.utils.ViewUtils
import org.json.JSONObject
import com.facebook.GraphResponse
import com.facebook.GraphRequest
import com.facebook.internal.CallbackManagerImpl
import com.facebook.login.LoginBehavior
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.common.api.Status
import com.xjek.base.BuildConfig
import com.xjek.provider.utils.Enums.Companion.GOOGLE
import com.xjek.provider.utils.Enums.Companion.GOOGLE_REQ_CODE


class SignupActivity : BaseActivity<ActivityRegisterBinding>(), SignupViewModel.SignupNavigator, View.OnClickListener {


    private lateinit var tlCountryCode: TextInputLayout
    private lateinit var mViewDataBinding: ActivityRegisterBinding
    private lateinit var edtPhoneNumber: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var edtCity: TextInputEditText
    private lateinit var edtState: TextInputEditText
    private lateinit var edtCountryCode: TextInputEditText
    private lateinit var edtCountry: TextInputEditText
    private lateinit var edtConfirmPassword: TextInputEditText
    private lateinit var edtFirstName: TextInputEditText
    private lateinit var edtLastName: TextInputEditText
    private var message: String = ""


    private var strPhoneCode: String? = ""
    private var strPhoneNumber: String? = ""
    private var strEmaiL: String? = ""
    private var strPwd: String? = ""
    private var strCity: String? = ""
    private var strCountry: String? = ""
    private var strState: String? = ""
    private var strConfirmPwd: String? = ""
    private var strFirstName: String? = ""
    private var strLastName: String? = ""
    private lateinit var signupViewmodel: SignupViewModel
    private lateinit var callbackManager: CallbackManager
    private var mGoogleApiClient: GoogleApiClient? = null

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityRegisterBinding
        signupViewmodel = SignupViewModel(this)
        this.mViewDataBinding.registermodel = signupViewmodel

        this.mViewDataBinding.lifecycleOwner=this@SignupActivity
        //initListener
        tlCountryCode = findViewById(R.id.tl_country_code)
        edtCountryCode = findViewById(R.id.edt_signup_code)
        edtFirstName = findViewById(R.id.edt_singup_firstname)
        edtLastName = findViewById(R.id.edt_singup_lastname)
        edtPhoneNumber = findViewById(R.id.edt_signup_phone)
        edtEmail = findViewById(R.id.edt_signup_mail)
        edtCountry = findViewById(R.id.edt_signup_country)
        edtState = findViewById(R.id.edt_signup_state)
        edtCity = findViewById(R.id.edt_signup_city)
        edtPassword = findViewById(R.id.edt_signup_password)
        edtConfirmPassword = findViewById(R.id.edt_signup_confirmpwd)
        callbackManager = CallbackManager.Factory.create()
        initListener()


        baseLiveDataLoading = signupViewmodel.loadingProgress


        initFacebooik()

        initGoogle()

        /* signupViewmodel.getCountryLiveData().observe(this@SignupActivity, Observer<CountryListResponse> {
             Log.d("_D", "country_code :" + it.responseData.get(0).country_code)
             val intent = Intent(this@SignupActivity, CountryListActivity::class.java)
             intent.putExtra("selectedfrom", "country")
             intent.putExtra("countrylistresponse", it as Serializable)
             startActivityForResult(intent, 100)
         })

         signupViewmodel.getStateLiveData().observe(this@SignupActivity, Observer<StateListResponse> {
             Log.d("_D", "state_name :" + it.responseData.get(0).state_name)
             val intent = Intent(this@SignupActivity, StateListActivity::class.java)
             intent.putExtra("selectedfrom", "state")
             intent.putExtra("statelistresponse", it as Serializable)
             startActivityForResult(intent, 101)
         })

         signupViewmodel.getCityLiveData().observe(this@SignupActivity, Observer<CityListResponse> {
             Log.d("_D", "state_name :" + it.responseData.get(0).city_name)
             val intent = Intent(this@SignupActivity, CityListActivity::class.java)
             intent.putExtra("selectedfrom", "city")
             intent.putExtra("citylistresponse", it as Serializable)
             startActivityForResult(intent, 102)
         })*/

        getApiResponse()
    }

    fun getApiResponse() {
        baseLiveDataLoading.value = false
        observeLiveData(signupViewmodel.getSignupLiveData()) {
            if (signupViewmodel.getSignupObserverValue()!!.statusCode.equals("200")) {
                showToast("Success")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                FB_ACCOUNT_KIT_CODE -> {
                    showToast("Verified")
                }

                GOOGLE_REQ_CODE -> {
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                    Log.e("status code ", result.status.toString())
                    if (result.isSuccess) {
                        // Signed in successfully, show authenticated UI.
                        val acct = result.signInAccount
                        if (acct != null) {
                            handleGplusSignInResult(acct)
                        }
                    } else {
                        // Signed out, show unauthenticated UI.
                        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { status -> Log.e("status", "logout $status") }
                    }
                }

                CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode() -> {
                    //Facebook Login
                    callbackManager.onActivityResult(requestCode, resultCode, data)
                }


            }
        }

    }

    fun initListener() {
        edtCountryCode.setOnClickListener(this)
    }

    //do registration
    override fun signup() {
        strPhoneNumber = edtPhoneNumber.text.toString()
        strPwd = edtPassword.text.toString()
        strCity = edtCity.text.toString()
        strCountry = edtCountry.text.toString()
        strCity = edtCity.text.toString()
        strState = edtState.text.toString()
        strConfirmPwd = edtConfirmPassword.text.toString()
        strFirstName = edtFirstName.text.toString()
        strLastName = edtLastName.text.toString()

        if (isValidCredential()) {
            baseLiveDataLoading.value = true
            signupViewmodel.postSignup()
        } else {
            ViewUtils.showToast(this@SignupActivity, message, false)
        }
    }

    // move to signin page
    override fun openSignin() {
        openNewActivity(this@SignupActivity, SignInActivity::class.java, true)
    }

    override fun gotoDocumentPage() {
        val intent = Intent(this@SignupActivity, DocumentActivity::class.java)
        startActivity(intent)
    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.countrycode_register_et -> {
                val intent = Intent(this@SignupActivity, CountryCodeActivity::class.java)
                startActivityForResult(intent, 111)
            }
        }
    }

    fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.google_signin_server_client_id)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    fun isValidCredential(): Boolean {
        if (TextUtils.isEmpty(strFirstName)) {
            message = resources.getString(R.string.empty_firstname)
            return false
        } else if (TextUtils.isEmpty(strLastName)) {
            message = resources.getString(R.string.empty_lastname)
        } else if (TextUtils.isEmpty(strPhoneCode)) {
            message = resources.getString(R.string.empty_phone)
            return false
        } else if (TextUtils.isEmpty(strPhoneNumber)) {
            message = resources.getString(R.string.empty_phone)
            return false
        } else if (TextUtils.isEmpty(strEmaiL)) {
            message = resources.getString(R.string.email_empty)
            return false
        } else if (TextUtils.isEmpty(strPwd)) {
            message = resources.getString(R.string.password_empty)
            return false
        } else if (TextUtils.isEmpty(strConfirmPwd)) {
            // message = resources.getString(R.string.password_not_match)
            return false
        } else if (TextUtils.isEmpty(strCountry)) {
            message = resources.getString(R.string.empty_country)
            return false
        } else if (TextUtils.isEmpty(strState)) {
            message = resources.getString(R.string.empty_state)
            return false
        } else if (TextUtils.isEmpty(strCity)) {
            message = resources.getString(R.string.empty_city)
            return false
        }

        return true
    }

    override fun showError(error: String) {
        signupViewmodel.loadingProgress.value = false
        ViewUtils.showToast(this@SignupActivity, error, false)
    }


    override fun verifyPhoneNumber() {
        val intent = Intent(this@SignupActivity, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.CODE)
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build())
        startActivityForResult(intent, FB_ACCOUNT_KIT_CODE)
    }


    override fun facebookSignup() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY).logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
    }


    fun initFacebooik() {
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                if (AccessToken.getCurrentAccessToken() != null) {
                    Log.e("AccessToken", "--------" + loginResult.accessToken)
                    getFacebookUserProfile(loginResult.accessToken)
                }
            }

            override fun onCancel() {}

            override fun onError(exception: FacebookException) {
                exception.printStackTrace()
                val s = exception.message
                if (exception is FacebookAuthorizationException) {
                    if (AccessToken.getCurrentAccessToken() != null)
                        LoginManager.getInstance().logOut()
                } else if (s!!.contains("GraphQLHttpFailureDomain"))
                    ViewUtils.showToast(this@SignupActivity, resources.getString(R.string.fb_session_expired), false)
            }
        })
    }


    private fun getFacebookUserProfile(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { `object`, response ->
            var jsonObject = JSONObject()
            jsonObject = response.jsonObject
            try {
                val socialFirstName = jsonObject.getString("first_name")
                val socialLastName = jsonObject.getString("last_name")
                val socialId = jsonObject.getString("id")
                val token = accessToken.token
                val socialEmail = jsonObject.getString("email")


                 signupViewmodel.firstName.value=socialFirstName
                 signupViewmodel.lastName.value=socialLastName
                 signupViewmodel.email.value=socialEmail


                if (jsonObject.has("picture")) {
                    val profileImg = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
                    Log.e("Profile","------"+profileImg)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id, name, first_name,last_name, email, picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun googleSignup() {
        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(intent, GOOGLE_REQ_CODE)
    }


    fun handleGplusSignInResult(result: GoogleSignInAccount) {
        var socialFirstName = result.getGivenName()
        var socialLastName = result.getFamilyName()
        var socialId = result.getId()
        var email = result.getEmail()
        var token = result.getIdToken()
        var profileImage = result.photoUrl

        signupViewmodel.firstName.value=socialFirstName.toString()
        signupViewmodel.lastName.value=socialLastName.toString()
        signupViewmodel.email.value=email.toString()

        Log.e("firstName", "==" + socialFirstName)
        Log.e("email", "===" + email)
        Log.e("photo", "----" + profileImage)
    }
}
