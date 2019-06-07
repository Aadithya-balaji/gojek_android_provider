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
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
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
import com.gox.partner.views.sign_in.SignInActivity
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
class SignupActivity : BaseActivity<ActivityRegisterBinding>(),
        SignupViewModel.SignupNavigator,
        CompoundButton.OnCheckedChangeListener,
        View.OnClickListener,
        View.OnFocusChangeListener,
        TextWatcher {

    private lateinit var tlCountryCode: TextInputLayout
    private lateinit var mViewDataBinding: ActivityRegisterBinding
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
    private lateinit var signupViewmodel: SignupViewModel
    private lateinit var callbackManager: CallbackManager

    private var message: String = ""
    private var cityList: ArrayList<City> = ArrayList()
    private var isEmailFocus: Boolean? = false
    private var isPhoneFocus: Boolean? = false
    private var filePart: MultipartBody.Part? = null
    private var isConditionChecked: Boolean? = false

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
    private var mGoogleApiClient: GoogleApiClient? = null
    private var imageUrl: String? = null

    override fun getLayoutId() = R.layout.activity_register

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityRegisterBinding
        signupViewmodel = SignupViewModel(this)
        this.mViewDataBinding.registermodel = signupViewmodel
        this.mViewDataBinding.lifecycleOwner = this

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
        rbMale = findViewById(R.id.rbMale)
        rbFemale = findViewById(R.id.rbFemale)
        callbackManager = CallbackManager.Factory.create()
        edtCountry.isFocusableInTouchMode = false
        edtCity.isFocusableInTouchMode = false

        initListener()
        initFacebook()
        initGoogle()

        val resultIntent = Intent()
        resultIntent.putExtra("countryName", "India")
        resultIntent.putExtra("countryCode", "+91")
        resultIntent.putExtra("countryFlag", R.drawable.flag_in)
        handleCountryCodePickerResult(resultIntent)

        getApiResponse()
        observeLiveData(signupViewmodel.phoneNumber) {
            if (!TextUtils.isEmpty(signupViewmodel.countryCode.value.toString()))
                if (CommonMethods.validatePhone(signupViewmodel.phoneNumber.value.toString())) {
                    val params = HashMap<String, String>()
                    params["salt_key"] = SALT_KEY
                    params[WebApiConstants.ValidateUser.PHONE] = signupViewmodel.phoneNumber.value.toString()
                    params[WebApiConstants.ValidateUser.COUNTRYCODE] = signupViewmodel.countryCode.toString()
                    Log.e("phone ", "-------observe" + signupViewmodel.countryCode.value
                            + "--" + signupViewmodel.phoneNumber.value)
                    println("phone ${signupViewmodel.phoneNumber.value}")
                    signupViewmodel.validateUser(params)
                }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            terms_conditions_tv.text = Html.fromHtml(getString(R.string.i_accept_the_following_terms_and_conditions), Html.FROM_HTML_MODE_LEGACY)
        else terms_conditions_tv.text = Html.fromHtml(getString(R.string.i_accept_the_following_terms_and_conditions))
    }

    private fun getApiResponse() {
        loadingObservable.value = false
        observeLiveData(signupViewmodel.getSignupLiveData()) {
            if (signupViewmodel.getSignupObserverValue()!!.statusCode.equals("200")) verifyPhoneNumber()
        }

        observeLiveData(signupViewmodel.getCountryLiveData()) {
            val intent = Intent(this, CountryListActivity::class.java)
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
                    cityList.clear()
                    Log.d("countrylist", selectedCountry?.country_name + "")
                    mViewDataBinding.edtSignupCountry.setText(selectedCountry?.country_name)
                    signupViewmodel.countryName.value = selectedCountry?.country_name
                    signupViewmodel.countryID.value = selectedCountry?.id.toString()

                    selectedCountry?.city?.let {
                        cityList = it as ArrayList<City>
                    }
                }

                CITYLIST_REQUEST_CODE -> {
                    val selectedCity = data?.extras?.get("selected_list") as? City
                    Log.d("statelist", selectedCity?.city_name + "")
                    mViewDataBinding.edtSignupCity.setText(selectedCity?.city_name)
                    signupViewmodel.cityName.value = selectedCity?.city_name
                    signupViewmodel.cityID.value = selectedCity?.id.toString()
                }
                FB_ACCOUNT_KIT_CODE -> {
                    writePreferences(PreferencesKey.ACCESS_TOKEN, signupViewmodel.getSignupLiveData().value!!.responseData!!.accessToken)
                    val dashBoardIntent = Intent(this, DashBoardActivity::class.java)
                    dashBoardIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    launchNewActivity(dashBoardIntent, true)
                }

                GOOGLE_REQ_CODE -> {
                    val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                    Log.e("status code ", result.status.toString())
                    if (result.isSuccess) {
                        // Signed in successfully, show authenticated UI.
                        val acct = result.signInAccount
                        if (acct != null) handleGplusSignInResult(acct)
                    } else Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback { status -> Log.e("status", "logout $status") }
                }

                CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode() -> {
                    callbackManager.onActivityResult(requestCode, resultCode, data)
                }

                Enums.RC_COUNTRY_CODE_PICKER -> {
                    if (data != null && data.extras != null) handleCountryCodePickerResult(data)
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    ivProfile.setImageURI(result.uri)
                    val profileFile = File(result.uri.toString())
                    if (profileFile.exists()) {
                        filePart = MultipartBody.Part.createFormData("picture", profileFile.name,
                                RequestBody.create(MediaType.parse("image*//*"), profileFile))
                        signupViewmodel.fileName.value = filePart
                    }
                }
            }
        }
    }

    private fun initListener() {
        // edtCountryCode.setOnClickListener(this)
        edtCountryCode.isFocusableInTouchMode = false
        edtCity.setOnClickListener(this)
        rbFemale.setOnCheckedChangeListener(this)
        rbMale.setOnCheckedChangeListener(this)
        edtCountryCode.isFocusableInTouchMode = false
        edtCountry.isFocusableInTouchMode = false
        edtCity.isFocusableInTouchMode = false
        edtEmail.onFocusChangeListener = this
        edtPhoneNumber.onFocusChangeListener = this
        edtEmail.addTextChangedListener(this)
        edtPhoneNumber.addTextChangedListener(this)
        terms_conditions_tv.setOnClickListener(this)
    }

    override fun signup() {

    }

    override fun openSignin() {
        launchNewActivity(SignInActivity::class.java, false)
    }

    override fun gotoDocumentPage() {
//        val intent = Intent(this, DocumentActivity::class.java)
//        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.countrycode_register_et -> startActivityForResult(Intent(this,
                    CountryCodeActivity::class.java), Enums.RC_COUNTRY_CODE_PICKER)
            R.id.terms_conditions_tv -> startActivity(Intent(this@SignupActivity,
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
        if (TextUtils.isEmpty(signupViewmodel.firstName.value)) {
            message = resources.getString(R.string.empty_firstname)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.lastName.value)) {
            message = resources.getString(R.string.empty_lastname)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.countryCode.value)) {
            message = resources.getString(R.string.empty_country_code)
            return false
        } else if (rbFemale.isChecked == false && rbMale.isChecked == false) {
            message = resources.getString(R.string.empty_gender_type)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.phoneNumber.value)) {
            message = resources.getString(R.string.empty_phone)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.email.value)) {
            message = resources.getString(R.string.email_empty)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.password.value) && signupViewmodel.socialID.value.isNullOrEmpty()) {
            message = resources.getString(R.string.password_empty)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.countryName.value)) {
            message = resources.getString(R.string.empty_country)
            return false
        } else if (TextUtils.isEmpty(signupViewmodel.cityName.value)) {
            message = resources.getString(R.string.empty_city)
            return false
        } else if (mViewDataBinding.cbTermsCondition.isChecked == false) {
            message = resources.getString(R.string.unchecked_terms)
            return false
        } else if (rbFemale.isChecked == false && rbMale.isChecked == false) {
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
        val intent = Intent(this, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.CODE)
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
                    ViewUtils.showToast(this@SignupActivity, resources.getString(R.string.fb_session_expired), false)
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
                val token = accessToken.token
                val socialEmail = jsonObject.getString("email")
                val img_value = "http://graph.facebook.com/" + jsonObject.getString("id") + "/picture?type=large"
                Glide.with(this).load(img_value).into(mViewDataBinding.profileImage)
                Log.e("FB_ID", "-----$socialId")
                signupViewmodel.firstName.value = socialFirstName
                signupViewmodel.lastName.value = socialLastName
                signupViewmodel.email.value = socialEmail
                signupViewmodel.socialID.value = socialId
                signupViewmodel.loginby.value = "FACEBOOK"
                tlPassword.visibility = View.GONE

                //DownloadImage
                DownloadImage(this).execute(img_value)

                if (jsonObject.has("picture")) {
                    jsonObject.getJSONObject("picture").getJSONObject("data").getString("url")
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

    private fun handleGplusSignInResult(result: GoogleSignInAccount) {
        val socialFirstName = result.givenName
        val socialLastName = result.familyName
        val socialId = result.id
        val email = result.email
        var token = result.idToken
        val profileImage = result.photoUrl

        glideSetImageView(ivProfile, profileImage.toString(), R.drawable.dummy_profile_pic)

        signupViewmodel.firstName.value = socialFirstName.toString()
        signupViewmodel.lastName.value = socialLastName.toString()
        signupViewmodel.email.value = email.toString()
        signupViewmodel.socialID.value = socialId

        if (!signupViewmodel.firstName.value.isNullOrEmpty()) edtFirstName.isEnabled = false
        else if (!signupViewmodel.lastName.value.isNullOrEmpty()) edtLastName.isEnabled = false
        else if (!signupViewmodel.email.value.isNullOrEmpty()) edtEmail.isEnabled = false

        signupViewmodel.loginby.value = "GOOGLE"
        tlPassword.visibility = View.GONE

        DownloadImage(this).execute(URL(profileImage.toString()).toString())

        Log.e("firstName", "==$socialFirstName")
        Log.e("email", "===$email")
        Log.e("photo", "----$profileImage")
    }


    override fun getCityList() {
        if (TextUtils.isEmpty(signupViewmodel.countryName.value)) {
            ViewUtils.showToast(this, resources.getString(R.string.empty_country), false)
        } else {
            val intent = Intent(this, CityListActivity::class.java)
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
            val width: Int = resources.getDimension(R.dimen.flag_width).toInt()
            val height: Int = resources.getDimension(R.dimen.flag_height).toInt()
            val drawable = BitmapDrawable(resources,
                    Bitmap.createScaledBitmap(bitmap, width, height, true))
            mViewDataBinding.edtSignupCode
                    .setCompoundDrawablesWithIntrinsicBounds(drawable, null,
                            null, null)
        }
    }

    override fun getCountryCode() {
        val intent = Intent(this, CountryCodeActivity::class.java)
        startActivityForResult(intent, Enums.RC_COUNTRY_CODE_PICKER)
    }


    override fun validate() {

        signupViewmodel.firstName.value = edtFirstName.text.toString()
        signupViewmodel.lastName.value = edtLastName.text.toString()
        signupViewmodel.phoneNumber.value = edtPhoneNumber.text.toString()
        signupViewmodel.password.value = edtPassword.text.toString()
        signupViewmodel.cityName.value = edtCity.text.toString()
        signupViewmodel.countryName.value = edtCountry.text.toString()


        if (isValidCredential()) {
            loadingObservable.value = true
            val profileFile = File(imageUrl.toString())
            if (profileFile != null && profileFile.exists()) {
                Log.e("signup", "---------" + profileFile.path)
                filePart = MultipartBody.Part.createFormData("picture", profileFile.name, RequestBody.create(MediaType.parse("image*//*"), profileFile))
                signupViewmodel.fileName.value = filePart
            }
            signupViewmodel.postSignup()
        } else {
            ViewUtils.showToast(this, message, false)
        }
    }


    override fun facebookSignup() {

        getFacebookprofileWithPermissionCheck()
    }

    override fun getImage() {
        pickImageWithPermissionCheck()
    }

    override fun googleSignup() {
        getGoogleProfileWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun getFacebookprofile() {
//        tlPassword.visibility = View.GONE
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_VIEW_ONLY).logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
    }


    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun getGoogleProfile() {
//        tlPassword.visibility = View.GONE
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
            if (file != null && file.exists()) {
                return file.path
            } else {
                return ""
            }
        }

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        when (v!!.id) {
            R.id.edt_signup_mail -> {
                if (hasFocus) {
                    isEmailFocus = true
                    isPhoneFocus = false
                }
            }

            R.id.edt_signup_phone -> {
                isPhoneFocus = true
                isEmailFocus = false

            }

        }
    }


    override fun afterTextChanged(s: Editable?) {


    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        if (isEmailFocus == true) {
            /*if (CommonMethods.validateEmail(s.toString())) {
                val params = HashMap<String, String>()
                params.put(WebApiConstants.SALT_KEY, "MQ==")
                params.put(WebApiConstants.EMAIL, signupViewmodel.email.value.toString())
                signupViewmodel.validateUser(params)
                Log.e("email", "------" + signupViewmodel.email.value.toString())
            }*/
        } else if (isPhoneFocus == true) {
//            if (!TextUtils.isEmpty(signupViewmodel.countryCode.value.toString()))
//                if (CommonMethods.validatePhone(s.toString())) {
//                    val params = HashMap<String, String>()
//                    params.put(WebApiConstants.SALT_KEY, "MQ==")
//                    params.put(WebApiConstants.ValidateUser.PHONE, signupViewmodel.phoneNumber.value.toString())
//                    params.put(WebApiConstants.ValidateUser.COUNTRYCODE, signupViewmodel.countryCode.toString())
//
//                    Log.e("phone ", "-----" + signupViewmodel.countryCode.value
//                            + "--" + signupViewmodel.phoneNumber.value)
//
//                    println("phone ${signupViewmodel.phoneNumber.value}")
//                    signupViewmodel.validateUser(params)
//                }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id) {
            R.id.rbMale -> {
                if (isChecked == true) {
                    signupViewmodel.gender.value = "MALE"
                }

            }

            R.id.rbFemale -> {
                if (isChecked == true) {
                    signupViewmodel.gender.value = "FEMALE"

                }
            }

            R.id.cb_terms_condition -> {
                isConditionChecked = isChecked
            }
        }
    }
}
