package com.xjek.provider.views.signup

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.xjek.provider.R
import com.facebook.*
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.facebook.internal.CallbackManagerImpl
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.databinding.ActivityRegisterBinding
import com.xjek.provider.utils.Enums
import com.xjek.provider.utils.Enums.Companion.CITYLIST_REQUEST_CODE
import com.xjek.provider.utils.Enums.Companion.COUNTRYLIST_REQUEST_CODE
import com.xjek.provider.utils.Enums.Companion.FB_ACCOUNT_KIT_CODE
import com.xjek.provider.utils.Enums.Companion.GOOGLE_REQ_CODE
import com.xjek.provider.views.citylist.CityListActivity
import com.xjek.provider.views.countrylist.CountryListActivity
import com.xjek.provider.views.countrypicker.CountryCodeActivity
import com.xjek.provider.views.document.DocumentActivity
import com.xjek.provider.views.signin.SignInActivity
import com.xjek.user.data.repositary.remote.model.City
import com.xjek.user.data.repositary.remote.model.CountryResponseData
import org.json.JSONObject
import java.io.Serializable
import java.util.*


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
    private lateinit var ivProfile: ImageView
    private var message: String = ""
    private lateinit var cityList: List<City>


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

        this.mViewDataBinding.lifecycleOwner = this@SignupActivity
        //initListener
        tlCountryCode = findViewById(R.id.tl_country_code)
        edtCountryCode = findViewById(R.id.edt_signup_code)
        edtFirstName = findViewById(R.id.edt_singup_firstname)
        edtLastName = findViewById(R.id.edt_singup_lastname)
        edtPhoneNumber = findViewById(R.id.edt_signup_phone)
        edtEmail = findViewById(R.id.edt_signup_mail)
        edtCountry = findViewById(R.id.edt_signup_country)
        edtCity = findViewById(R.id.edt_signup_city)
        edtPassword = findViewById(R.id.edt_signup_password)
        edtConfirmPassword = findViewById(R.id.edt_signup_confirmpwd)
        ivProfile = findViewById(R.id.profile_image)
        callbackManager = CallbackManager.Factory.create()
        edtCountry.isFocusableInTouchMode = false
        edtCity.isFocusableInTouchMode = false
        initListener()


        baseLiveDataLoading = signupViewmodel.loadingProgress


        initFacebooik()

        initGoogle()

        getApiResponse()
    }

    fun getApiResponse() {
        baseLiveDataLoading.value = false
        observeLiveData(signupViewmodel.getSignupLiveData()) {
            if (signupViewmodel.getSignupObserverValue()!!.statusCode.equals("200")) {
                showToast("Success")
            }
        }


        observeLiveData(signupViewmodel.getCountryLiveData()) {
            ViewUtils.showToast(applicationContext, "Success", true)
            val intent = Intent(this@SignupActivity, CountryListActivity::class.java)
            intent.putExtra("selectedfrom", "country")
            intent.putExtra("countrylistresponse", it as Serializable)
            startActivityForResult(intent, COUNTRYLIST_REQUEST_CODE)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                COUNTRYLIST_REQUEST_CODE -> {
                    val selectedCountry = data?.extras?.get("selected_list") as? CountryResponseData
                    Log.d("countrylist", selectedCountry?.country_name + "")
                    cityList = (selectedCountry?.city as? List<City>)!!
                    mViewDataBinding.edtSignupCountry.setText(selectedCountry?.country_name)
                    signupViewmodel.countryName.value = selectedCountry?.country_name
                    signupViewmodel.countryID.value = selectedCountry?.id.toString()
                }

                CITYLIST_REQUEST_CODE -> {
                    val selectedCity = data?.extras?.get("selected_list") as? City
                    Log.d("statelist", selectedCity?.city_name + "")
                    mViewDataBinding.edtSignupCity.setText(selectedCity?.city_name)
                    signupViewmodel.cityName.value = selectedCity?.city_name
                    signupViewmodel.cityID.value = selectedCity?.id.toString()
                }
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

                Enums.RC_COUNTRY_CODE_PICKER -> {
                    if (data != null && data.extras != null) {
                        handleCountryCodePickerResult(data)
                    }
                }


                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    ivProfile.setImageURI(result.uri)
                }


            }
        }

    }

    fun initListener() {
        // edtCountryCode.setOnClickListener(this)
        edtCountryCode.isFocusableInTouchMode = false
        edtCity.setOnClickListener(this)
    }

    //do registration
    override fun signup() {

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
                startActivityForResult(intent, Enums.RC_COUNTRY_CODE_PICKER)
            }
        }
    }

    fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.google_signin_server_client_id))
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    fun isValidCredential(): Boolean {
        if (TextUtils.isEmpty(signupViewmodel.firstName.value)) {
            message = resources.getString(R.string.empty_firstname)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.lastName.value)) {
            message = resources.getString(R.string.empty_lastname)
        } else if (TextUtils.isEmpty(signupViewmodel.countryCode.value)) {
            message = resources.getString(R.string.empty_phone)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.phoneNumber.value)) {
            message = resources.getString(R.string.empty_phone)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.email.value)) {
            message = resources.getString(R.string.email_empty)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.password.value)) {
            message = resources.getString(R.string.password_empty)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.confirmPassword.value)) {
            // message = resources.getString(R.string.password_not_match)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.countryName.value)) {
            message = resources.getString(R.string.empty_country)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.cityName.value)) {
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
                Log.e("FB_ID", "-----" + socialId)
                signupViewmodel.firstName.value = socialFirstName
                signupViewmodel.lastName.value = socialLastName
                signupViewmodel.email.value = socialEmail

                if (jsonObject.has("picture")) {
                    val profileImg = jsonObject.getJSONObject("picture").getJSONObject("data").getString("url");
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

        signupViewmodel.firstName.value = socialFirstName.toString()
        signupViewmodel.lastName.value = socialLastName.toString()
        signupViewmodel.email.value = email.toString()

        if (!signupViewmodel.firstName.value.isNullOrEmpty()) {
            edtFirstName.isEnabled = false
        } else if (!signupViewmodel.lastName.value.isNullOrEmpty()) {
            edtLastName.isEnabled = false
        } else if (!signupViewmodel.email.value.isNullOrEmpty()) {
            edtEmail.isEnabled = false
        }

        Log.e("firstName", "==" + socialFirstName)
        Log.e("email", "===" + email)
        Log.e("photo", "----" + profileImage)
    }


    override fun getCityList() {
        if (TextUtils.isEmpty(signupViewmodel.countryName.value)) {
            ViewUtils.showToast(this@SignupActivity, resources.getString(R.string.empty_country), false)
        } else {
            val intent = Intent(this@SignupActivity, CityListActivity::class.java)
            intent.putExtra("selectedfrom", "city")
            intent.putExtra("citylistresponse", cityList as Serializable)
            startActivityForResult(intent, CITYLIST_REQUEST_CODE)
        }
    }


    private fun handleCountryCodePickerResult(data: Intent) {
        val countryCode = data.getStringExtra("countryCode")
        signupViewmodel.countryCode.value = countryCode
        val countryFlag = data.getIntExtra("countryFlag", -1)
        val leftDrawable = ContextCompat.getDrawable(this, countryFlag)
        if (leftDrawable != null) {
            val bitmap = (leftDrawable as BitmapDrawable).bitmap
            val drawable = BitmapDrawable(resources,
                    Bitmap.createScaledBitmap(bitmap, 64, 64, true))
            mViewDataBinding.edtSignupCountry
                    .setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                            null, null)
        }
    }

    override fun getCountryCode() {
        val intent = Intent(this@SignupActivity, CountryCodeActivity::class.java)
        startActivityForResult(intent, Enums.RC_COUNTRY_CODE_PICKER)
    }


    override fun getImage() {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this)
    }

    override fun validate() {

        baseLiveDataLoading.value = true

        /*  signupViewmodel.firstName.value = edtFirstName.text.toString()
          signupViewmodel.lastName.value = edtLastName.text.toString()
          signupViewmodel.phoneNumber.value = edtPhoneNumber.text.toString()
          signupViewmodel.password.value = edtPassword.text.toString()
          signupViewmodel.cityName.value = edtCity.text.toString()
          signupViewmodel.countryName.value = edtCountry.text.toString()
          signupViewmodel.confirmPassword.value = edtConfirmPassword.text.toString()

          if (isValidCredential()) {
              baseLiveDataLoading.value = true
              signupViewmodel.postSignup()
          } else {
              ViewUtils.showToast(this@SignupActivity, message, false)
          }*/
    }


}
