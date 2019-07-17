package com.gox.partner.views.signup

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.text.Html
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy
import com.bumptech.glide.request.RequestOptions
import com.facebook.*
import com.facebook.accountkit.PhoneNumber
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
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gox.base.BuildConfig.SALT_KEY
import com.gox.base.base.BaseActivity
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.writePreferences
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityRegisterBinding
import com.gox.partner.models.City
import com.gox.partner.models.CountryResponseData
import com.gox.partner.network.WebApiConstants
import com.gox.partner.utils.CommonMethods
import com.gox.partner.utils.Country
import com.gox.partner.utils.Enums
import com.gox.partner.utils.Enums.Companion.CITYLIST_REQUEST_CODE
import com.gox.partner.utils.Enums.Companion.COUNTRYLIST_REQUEST_CODE
import com.gox.partner.utils.Enums.Companion.FB_ACCOUNT_KIT_CODE
import com.gox.partner.utils.Enums.Companion.GOOGLE_REQ_CODE
import com.gox.partner.views.citylist.CityListActivity
import com.gox.partner.views.countrylist.CountryListActivity
import com.gox.partner.views.countrypicker.CountryCodeActivity
import com.gox.partner.views.dashboard.DashBoardActivity
import com.gox.partner.views.privacypolicy.PrivacyActivity
import com.gox.partner.views.sign_in.LoginActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import permissions.dispatcher.*
import java.io.File
import java.io.FileOutputStream
import java.io.Serializable
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

@RuntimePermissions
class RegistrationActivity : BaseActivity<ActivityRegisterBinding>(),
        RegistrationViewModel.RegistrationNavigator,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener,
        View.OnFocusChangeListener {

    private lateinit var mBinding: ActivityRegisterBinding
    private lateinit var mViewModel: RegistrationViewModel

    private lateinit var tlCountryCode: TextInputLayout
    private lateinit var edtPhoneNumber: TextInputEditText
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtPassword: TextInputEditText
    private lateinit var edtCity: TextInputEditText
    private lateinit var edtCountryCode: TextInputEditText
    private lateinit var edtCountry: TextInputEditText
    private lateinit var edtFirstName: TextInputEditText
    private lateinit var edtLastName: TextInputEditText
    private lateinit var ivProfile: ImageView
    private lateinit var tlPassword: TextInputLayout
    private lateinit var rbMale: MaterialRadioButton
    private lateinit var rbFemale: MaterialRadioButton
    private lateinit var callbackManager: CallbackManager

    private var message: String = ""
    private var cityList: ArrayList<City> = ArrayList()
    private var isEmailFocus: Boolean? = false
    private var isPhoneFocus: Boolean? = false
    private var filePart: MultipartBody.Part? = null
    private var isConditionChecked: Boolean? = false

    private var mGoogleApiClient: GoogleApiClient? = null
    private var imageUrl: String? = null
    private var countryCode: String? = null
    private var isoCode: String? = null

    override fun getLayoutId() = R.layout.activity_register

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        profile_image
        this.mBinding = mViewDataBinding as ActivityRegisterBinding
        mViewModel = RegistrationViewModel(this)
        this.mBinding.registermodel = mViewModel
        this.mBinding.lifecycleOwner = this

        //      initListener
        tlCountryCode = findViewById(R.id.tl_country_code)
        edtCountryCode = findViewById(R.id.edt_signup_code)
        edtFirstName = findViewById(R.id.edt_singup_firstname)
        edtLastName = findViewById(R.id.edt_singup_lastname)
        edtPhoneNumber = findViewById(R.id.edt_signup_phone)
        edtEmail = findViewById(R.id.edt_signup_mail)
        edtCountry = findViewById(R.id.edt_signup_country)
        edtCity = findViewById(R.id.edt_signup_city)
        edtPassword = findViewById(R.id.edt_signup_password)
        ivProfile = findViewById(R.id.profile_image)
        tlPassword = findViewById(R.id.til_signup_pwd)
        rbMale = this.findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        callbackManager = CallbackManager.Factory.create()
        edtCountry.isFocusableInTouchMode = false
        edtCity.isFocusableInTouchMode = false

        initListener()
        initFacebook()
        initGoogle()

        val tm = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val countryModel = Country.getCountryByISO(tm.networkCountryIso)

        val resultIntent = Intent()

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

        getApiResponse()
        observeLiveData(mViewModel.phoneNumber) {
            if (!TextUtils.isEmpty(mViewModel.countryCode.value.toString()))
                if (CommonMethods.validatePhone(mViewModel.phoneNumber.value.toString())) {
                    val params = HashMap<String, String>()
                    params["salt_key"] = SALT_KEY
                    params[WebApiConstants.ValidateUser.PHONE] = mViewModel.phoneNumber.value.toString()
                    params[WebApiConstants.ValidateUser.COUNTRY_CODE] = mViewModel.countryCode.toString()
                    println("phone ${mViewModel.phoneNumber.value}")
                    mViewModel.validateUser(params)
                }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            terms_conditions_tv.text = Html.fromHtml(getString(R.string.i_accept_the_following_terms_and_conditions),
                    Html.FROM_HTML_MODE_LEGACY)
        else terms_conditions_tv.text = Html.fromHtml(getString(R.string.i_accept_the_following_terms_and_conditions))
    }

    private fun getApiResponse() {
        loadingObservable.value = false
        observeLiveData(mViewModel.getRegistrationLiveData()) {
            //            if (mViewModel.getRegistrationObserverValue()!!.statusCode.equals("200")) verifyPhoneNumber()
            if (mViewModel.getRegistrationObserverValue()!!.statusCode.equals("200")) {
                redirectToHome()
            }
        }

        observeLiveData(mViewModel.getCountryLiveData()) {
            val intent = Intent(this, CountryListActivity::class.java)
            intent.putExtra("selectedfrom", "country")
            intent.putExtra("countrylistresponse", it as Serializable)
            startActivityForResult(intent, COUNTRYLIST_REQUEST_CODE)
        }
    }

    private fun redirectToHome() {
        writePreferences(PreferencesKey.ACCESS_TOKEN, mViewModel.getRegistrationLiveData()
                .value!!.responseData!!.accessToken)
        val dashBoardIntent = Intent(this, DashBoardActivity::class.java)
        dashBoardIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        openActivity(dashBoardIntent, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_REQ_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            Log.e("status code ", result.status.toString())
            if (result.isSuccess) {
                val acct = result.signInAccount
                if (acct != null) handleGPlusSignInResult(acct)
            } else Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                    .setResultCallback { status -> Log.e("status", "logout $status") }
        }

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                COUNTRYLIST_REQUEST_CODE -> {
                    val selectedCountry = data?.extras?.get("selected_list") as? CountryResponseData
                    cityList.clear()
                    mBinding.edtSignupCountry.setText(selectedCountry?.country_name)
                    mViewModel.countryName.value = selectedCountry?.country_name
                    mViewModel.countryID.value = selectedCountry?.id.toString()

                    selectedCountry?.city?.let {
                        cityList = it as ArrayList<City>
                    }
                }

                CITYLIST_REQUEST_CODE -> {
                    val selectedCity = data?.extras?.get("selected_list") as? City
                    mBinding.edtSignupCity.setText(selectedCity?.city_name)
                    mViewModel.cityName.value = selectedCity?.city_name
                    mViewModel.cityID.value = selectedCity?.id.toString()
                }

                FB_ACCOUNT_KIT_CODE -> {
                    /*writePreferences(PreferencesKey.ACCESS_TOKEN, mViewModel.getRegistrationLiveData()
                            .value!!.responseData!!.accessToken)
                    val dashBoardIntent = Intent(this, DashBoardActivity::class.java)
                    dashBoardIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    openActivity(dashBoardIntent, true)*/
                    mViewModel.postSignUp()
                }

                GOOGLE_REQ_CODE -> {
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                    if (result.isSuccess) {
                        // Signed in successfully, show authenticated UI.
                        val acct = result.signInAccount
                        if (acct != null) handleGPlusSignInResult(acct)
                    } else Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                            .setResultCallback { status -> Log.e("status", "logout $status") }
                }

                CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode() -> {
                    callbackManager.onActivityResult(requestCode, resultCode, data)
                }

                Enums.RC_COUNTRY_CODE_PICKER -> {
                    if (data != null && data.extras != null) handleCountryCodePickerResult(data)
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    glideSetImageView(ivProfile, result.uri, R.drawable.ic_profile_placeholder)
                    val profileFile = File(result.uri.toString())
                    if (profileFile.exists()) {
                        filePart = MultipartBody.Part.createFormData("picture", profileFile.name,
                                RequestBody.create(MediaType.parse("image*//*"), profileFile))
                        mViewModel.fileName.value = filePart
                    }
                }
            }
        }
    }

    private fun initListener() {
        edtCountryCode.isFocusableInTouchMode = false
        edtCity.setOnClickListener(this)
        rbFemale.setOnCheckedChangeListener(this)
        rbMale.setOnCheckedChangeListener(this)
        edtCountryCode.isFocusableInTouchMode = false
        edtCountry.isFocusableInTouchMode = false
        edtCity.isFocusableInTouchMode = false
        edtEmail.onFocusChangeListener = this
        edtPhoneNumber.onFocusChangeListener = this
        terms_conditions_tv.setOnClickListener(this)
    }

    private fun dpsToPixels(activity: Activity, dps: Int): Int {
        val r = activity.resources
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }

    override fun openSignIn() {
        openActivity(LoginActivity::class.java, false)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.countrycode_register_et -> startActivityForResult(Intent(this,
                    CountryCodeActivity::class.java), Enums.RC_COUNTRY_CODE_PICKER)
            R.id.terms_conditions_tv -> startActivity(Intent(this@RegistrationActivity,
                    PrivacyActivity::class.java).putExtra("title", "terms"))
        }
    }

    private fun initGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(resources.getString(R.string.google_signin_server_client_id))
                .requestEmail()
                .build()
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    private fun isValidCredential(): Boolean {
        if (TextUtils.isEmpty(mViewModel.firstName.value)) {
            message = resources.getString(R.string.empty_firstname)
            return false
        } else if (TextUtils.isEmpty(mViewModel.lastName.value)) {
            message = resources.getString(R.string.empty_lastname)
            return false
        } else if (TextUtils.isEmpty(mViewModel.countryCode.value)) {
            message = resources.getString(R.string.empty_country_code)
            return false
        } else if (!rbFemale.isChecked && !rbMale.isChecked) {
            message = resources.getString(R.string.empty_gender_type)
            return false
        } else if (TextUtils.isEmpty(mViewModel.phoneNumber.value)) {
            message = resources.getString(R.string.empty_phone)
            return false
        } else if (TextUtils.isEmpty(mViewModel.email.value)) {
            message = resources.getString(R.string.email_empty)
            return false
        } else if (TextUtils.isEmpty(mViewModel.password.value) && mViewModel.socialID.value.isNullOrEmpty()) {
            message = resources.getString(R.string.password_empty)
            return false
        } else if (TextUtils.isEmpty(mViewModel.countryName.value)) {
            message = resources.getString(R.string.empty_country)
            return false
        } else if (TextUtils.isEmpty(mViewModel.cityName.value)) {
            message = resources.getString(R.string.empty_city)
            return false
        } else if (!mBinding.cbTermsCondition.isChecked) {
            message = resources.getString(R.string.unchecked_terms)
            return false
        } else if (!rbFemale.isChecked && !rbMale.isChecked) {
            message = resources.getString(R.string.empty_gender_type)
            return false
        }

        return true
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(this, error, false)
    }

    override fun verifyPhoneNumber() {
        val phoneNumber = PhoneNumber(countryCode!!, mViewModel.phoneNumber.value!!, isoCode)
        val intent = Intent(this, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.CODE)
        configurationBuilder.setInitialPhoneNumber(phoneNumber)
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build())
        startActivityForResult(intent, FB_ACCOUNT_KIT_CODE)
    }

    private fun initFacebook() {
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
                    ViewUtils.showToast(this@RegistrationActivity,
                            resources.getString(R.string.fb_session_expired), false)
            }
        })
    }

    private fun getFacebookUserProfile(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { _, response ->
            val jsonObject: JSONObject = response.jsonObject
            try {
                val socialFirstName = jsonObject.getString("first_name")
                val socialLastName = jsonObject.getString("last_name")
                val socialId = jsonObject.getString("id")
                val socialEmail = jsonObject.getString("email")
                val imgValue = "http://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large"
                glideSetImageView(ivProfile, imgValue, R.drawable.ic_profile_placeholder)
                Log.e("FB_ID", "-----$socialId")
                mViewModel.firstName.value = socialFirstName
                mViewModel.lastName.value = socialLastName
                mViewModel.email.value = socialEmail
                mViewModel.socialID.value = socialId
                mViewModel.loginby.value = "FACEBOOK"
                tlPassword.visibility = View.GONE

                DownloadImage(this).execute(imgValue)

                if (jsonObject.has("picture"))
                    jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val parameters = Bundle()
        parameters.putString("fields", "id, name, first_name,last_name, email, picture.type(large)")
        request.parameters = parameters
        request.executeAsync()
    }

    private fun handleGPlusSignInResult(result: GoogleSignInAccount) {
        val socialFirstName = result.givenName
        val socialLastName = result.familyName
        val socialId = result.id
        val email = result.email
        val profileImage = result.photoUrl

        glideSetImageView(ivProfile, profileImage.toString(), R.drawable.ic_profile_placeholder)

        mViewModel.firstName.value = socialFirstName.toString()
        mViewModel.lastName.value = socialLastName.toString()
        mViewModel.email.value = email.toString()
        mViewModel.socialID.value = socialId

        if (!mViewModel.firstName.value.isNullOrEmpty()) edtFirstName.isEnabled = false
        else if (!mViewModel.lastName.value.isNullOrEmpty()) edtLastName.isEnabled = false
        else if (!mViewModel.email.value.isNullOrEmpty()) edtEmail.isEnabled = false

        mViewModel.loginby.value = "GOOGLE"
        tlPassword.visibility = View.GONE

        DownloadImage(this).execute(URL(profileImage.toString()).toString())
    }

    override fun getCityList() = if (TextUtils.isEmpty(mViewModel.countryName.value))
        ViewUtils.showToast(this, resources.getString(R.string.empty_country), false)
    else {
        val intent = Intent(this, CityListActivity::class.java)
        intent.putExtra("selectedfrom", "city")
        intent.putExtra("citylistresponse", cityList as Serializable)
        startActivityForResult(intent, CITYLIST_REQUEST_CODE)
    }

    private fun handleCountryCodePickerResult(data: Intent) {

        countryCode = data.getStringExtra("countryCode")
        isoCode = data.getStringExtra("countryIsoCode")
        countryCode = countryCode!!.removePrefix("+")

        mViewModel.countryCode.value = countryCode
        val countryFlag = data.getIntExtra("countryFlag", -1)
        val leftDrawable = ContextCompat.getDrawable(this, countryFlag)
        if (leftDrawable != null) {
            val bitmap = (leftDrawable as BitmapDrawable).bitmap
            var width: Int = 0
            var height: Int = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                width = dpsToPixels(this@RegistrationActivity, 8)
                height = dpsToPixels(this@RegistrationActivity, 8)
            } else {
                width = dpsToPixels(this@RegistrationActivity, 15)
                height = dpsToPixels(this@RegistrationActivity, 15)
            }
            val drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true))
            mBinding.edtSignupCode.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    override fun getCountryCode() {
        startActivityForResult(Intent(this, CountryCodeActivity::class.java), Enums.RC_COUNTRY_CODE_PICKER)
    }

    override fun validate() {
        mViewModel.firstName.value = edtFirstName.text.toString()
        mViewModel.lastName.value = edtLastName.text.toString()
        mViewModel.phoneNumber.value = edtPhoneNumber.text.toString()
        mViewModel.password.value = edtPassword.text.toString()
        mViewModel.cityName.value = edtCity.text.toString()
        mViewModel.countryName.value = edtCountry.text.toString()

        if (isValidCredential()) {
            loadingObservable.value = true
            val profileFile = File(imageUrl.toString())
            if (profileFile != null && profileFile.exists()) {
                Log.e("signUp", "---------" + profileFile.path)
                filePart = MultipartBody.Part.createFormData("picture",
                        profileFile.name, RequestBody.create(MediaType.parse("image*//*"), profileFile))
                mViewModel.fileName.value = filePart
            }
//            mViewModel.postSignUp()
            verifyPhoneNumber()
        } else ViewUtils.showToast(this, message, false)
    }

    override fun facebookSignUp() {
        getFacebookProfileWithPermissionCheck()
    }

    override fun getImage() {
        pickImageWithPermissionCheck()
    }

    override fun googleSignUp() {
        getGoogleProfileWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun getFacebookProfile() {
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY)
                .logInWithReadPermissions(this, listOf("public_profile", "email"))
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun getGoogleProfile() {
        val intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(intent, GOOGLE_REQ_CODE)
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun pickImage() {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this)
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onLocationPermissionDenied() {
        ViewUtils.showToast(this, getString(R.string.file_pemission_denied), false)
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onShowRationale(request: PermissionRequest) {
        ViewUtils.showToast(this, getString(R.string.rationale_storage_permission_denied), false)
    }

    inner class DownloadImage(context: Context) : AsyncTask<String, Void, String>() {
        var mContext: Context = context
        override fun onPreExecute() {
            super.onPreExecute()
            loadingObservable.value = true
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            loadingObservable.value = false
            imageUrl = result
        }

        override fun doInBackground(vararg params: String?): String {
            val imgUrl = params[0]

            val requestOptions = RequestOptions().override(100)
                    .downsample(DownsampleStrategy.CENTER_INSIDE)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
            val file = CommonMethods.getDefaultFileName(mContext)
            mContext.let {
                val bitmap = Glide.with(it)
                        .asBitmap()
                        .load(imgUrl)
                        .apply(requestOptions)
                        .submit()
                        .get()

                try {
                    val out = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
                    out.flush()
                    out.close()
                    Log.i("Seiggailion", "Image saved.")
                } catch (e: Exception) {
                    Log.i("Seiggailion", e.message)
                }
            }
            return if (file != null && file.exists())
                file.path else ""
        }

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v!!.id) {
            R.id.edt_signup_mail -> if (hasFocus) {
                isEmailFocus = true
                isPhoneFocus = false
            }

            R.id.edt_signup_phone -> {
                isPhoneFocus = true
                isEmailFocus = false
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id) {
            R.id.rbMale -> if (isChecked) mViewModel.gender.value = "MALE"

            R.id.rbFemale -> if (isChecked) mViewModel.gender.value = "FEMALE"

            R.id.cb_terms_condition -> isConditionChecked = isChecked
        }
    }
}
