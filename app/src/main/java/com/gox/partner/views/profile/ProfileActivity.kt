package com.gox.partner.views.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.util.TypedValue
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.data.Constants.APP_REQUEST_CODE
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.ValidationUtils
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityEditProfileBinding
import com.gox.partner.models.City
import com.gox.partner.models.CountryListResponse
import com.gox.partner.models.CountryModel
import com.gox.partner.models.CountryResponseData
import com.gox.partner.utils.Constant
import com.gox.partner.utils.Country
import com.gox.partner.views.change_password.ChangePasswordActivity
import com.gox.partner.views.citylist.CityListActivity
import com.gox.partner.views.countrylist.CountryListActivity
import com.gox.partner.views.verifyotp.VerifyOTPActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.Serializable

class ProfileActivity : BaseActivity<ActivityEditProfileBinding>(), ProfileNavigator {

    private lateinit var mBinding: ActivityEditProfileBinding
    private lateinit var mViewModel: ProfileViewModel
    private lateinit var city: List<City>

    private var mCropImageUri: Uri? = null
    private var localPath: Uri? = null
    private var mMobileNumberFlag = 0
    private lateinit var countryDetail: List<CountryModel>

    override fun getLayoutId() = R.layout.activity_edit_profile

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mBinding = mViewDataBinding as ActivityEditProfileBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.profile)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener {
            finish()
        }
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        mViewDataBinding.profileviewmodel = mViewModel
        mViewModel.navigator = this
        mViewModel.getProfile()

        mViewModel.updateProfileResponse().observe(this, Observer {
            ViewUtils.showToast(this, it.message!!, true)
        })

        mViewModel.countryListResponse.observe(this, Observer<CountryListResponse> {
            Log.d("_D", "country_code :" + it.responseData[0].country_code)
            val intent = Intent(this, CountryListActivity::class.java)
            intent.putExtra("selectedfrom", "country")
            intent.putExtra("countrylistresponse", it as Serializable)
            startActivityForResult(intent, Constants.COUNTRY_LIST_REQUEST_CODE)
        })

        observeLiveData(mViewModel.showLoading) {
            loadingObservable.value = it
        }

        observeLiveData(mViewModel.errorResponse) {
            ViewUtils.showToast(this, it, false)
        }

        mViewModel.mProfileResponse.observe(this, Observer { response ->

            countryDetail = Country.getAllCountries().filter { countryModel ->
                countryModel.code == response.profileData.country.country_code
            }

            mViewModel.mUserName.set(response.profileData.first_name)
            mViewModel.mMobileNumber.set(response.profileData.mobile)
            mViewModel.mEmail.set(response.profileData.email)
            mViewModel.mCity.set(response.profileData.city.city_name)
            mViewModel.mCountry.set(response.profileData.country.country_name)
            mViewModel.mCountryId.set(response.profileData.country.id.toString())
            mViewModel.mCityId.set(response.profileData.city.id.toString())
            mViewModel.mCountryCode.set(" +" + response.profileData.country.country_phonecode)

            val drawableId = countryDetail[0].flag
            val dr = ContextCompat.getDrawable(this, drawableId)
            val bitmap = (dr as BitmapDrawable).bitmap

            var width: Int = 0
            var height: Int = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                width = dpsToPixels(this@ProfileActivity, 5)
                height = dpsToPixels(this@ProfileActivity, 5)
            } else {
                width = dpsToPixels(this@ProfileActivity, 15)
                height = dpsToPixels(this@ProfileActivity, 15)
            }

            val d = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, width, height, true))
            countrycode_register_et.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null)

            if (!response.profileData.picture.isNullOrEmpty())
                glideSetImageView(mViewDataBinding.profileImage, response.profileData.picture, R.drawable.ic_user_place_holder)

        })


        mViewModel.sendOTPResponse.observe(this, Observer {
            mViewModel.showLoading.value = false
            ViewUtils.showToast(this, getString(R.string.otp_success), true)
            Handler().postDelayed({
                val intent = Intent(this, VerifyOTPActivity::class.java)
                intent.putExtra("country_code",mViewModel.mCountryCode.get().toString().replace("+",""))
                intent.putExtra("mobile",mViewModel.mMobileNumber.get().toString())
                startActivityForResult(intent, APP_REQUEST_CODE)
            },1000)

        })

        setOnclickListeners()
    }

    private fun dpsToPixels(activity: Activity, dps: Int): Int {
        val r = activity.resources
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dps.toFloat(), r.displayMetrics).toInt()
    }


    override fun goToChangePasswordActivity() =
            openActivity(ChangePasswordActivity::class.java, true)

    private fun setOnclickListeners() {
        mBinding.profileLayout.setOnClickListener {
            checkPermission()
        }

        save_editprofile_btn.setOnClickListener {
            if (mViewModel.mProfileResponse.value!!.profileData.mobile == phonenumber_register_et.text.toString()) {
                mMobileNumberFlag = 1
                if (localPath?.path != null) {
                    val pictureFile = File(localPath?.path)
                    val requestFile = RequestBody.create(MediaType.parse("*/*"), pictureFile)
                    val fileBody = MultipartBody.Part.createFormData("picture", pictureFile.name, requestFile)
                    mViewModel.updateProfile(fileBody)
                } else mViewModel.updateProfile(null)
            } else {
                mMobileNumberFlag = 2
                verifyMobileNumber()
            }
        }
    }

    private fun verifyMobileNumber() {
       /* val phoneNumber = PhoneNumber(mViewModel.mProfileResponse.value!!.profileData.country_code,
                mViewModel.mMobileNumber.get().toString(), "")
        val intent = Intent(this, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE, AccountKitActivity.ResponseType.CODE)
        configurationBuilder.setInitialPhoneNumber(phoneNumber)
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build())
        startActivityForResult(intent, APP_REQUEST_CODE)*/
        mViewModel.showLoading.value = true
        mViewModel.sendOTP()
    }

    private fun checkPermission() = Dexter.withActivity(this)
            .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    CropImage.startPickImageActivity(this@ProfileActivity)
                }

                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                val imageUri = CropImage.getPickImageResultUri(this, data)
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    mCropImageUri = imageUri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                } else startCropImageActivity(imageUri)
            }

            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    glideSetImageView(mBinding.profileImage, result.uri, R.drawable.ic_user_place_holder)
//                    mBinding.profileImage.setImageURI(result.uri)
                    localPath = result.uri
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                    ViewUtils.showNormalToast(this, getText(R.string.cropping_fail) as String)
            }

            if (requestCode == Constants.COUNTRY_LIST_REQUEST_CODE && data != null) {
                setCountry(data)
                mBinding.cityRegisterEt.isEnabled = true
                mViewModel.showLoading.value = false
            } else mViewModel.showLoading.value = false

            if (requestCode == Constants.CITY_LIST_REQUEST_CODE && data != null) {
                setCity(data)
                mViewModel.showLoading.value = false
            } else mViewModel.showLoading.value = false

            if (requestCode == APP_REQUEST_CODE && data != null) {
                accountKitOtpVerified(data)
                mViewModel.showLoading.value = false
            } else mViewModel.showLoading.value = false
        }
    }

    private fun setCountry(data: Intent?) {
        val selectedCountry = data?.extras?.get("selected_list") as? CountryResponseData
        city = selectedCountry?.city!!
        mBinding.countryRegisterEt.setText(selectedCountry.country_name)
        mViewModel.mCountryId.set(selectedCountry.id.toString())
    }

    private fun accountKitOtpVerified(data: Intent?) {
       /* val loginResult: AccountKitLoginResult = data!!.getParcelableExtra(AccountKitLoginResult.RESULT_KEY)
        if (loginResult.error != null) {
            Log.d("_D_fbaccountkit", loginResult.error.toString())
        } else if (loginResult.wasCancelled()) {
            Log.d("_D_fbaccountkit", "Fb login cancelled")
        } else {

        }*/

        mMobileNumberFlag = 1
        if (localPath?.path != null) {
            val pictureFile = File(localPath?.path)
            val requestFile = RequestBody.create(
                    MediaType.parse("*/*"),
                    pictureFile)

            val fileBody = MultipartBody.Part.createFormData("picture", pictureFile.name, requestFile)
            mViewModel.updateProfile(fileBody)
        } else mViewModel.updateProfile(null)
    }

    private fun setCity(data: Intent?) {
        val selectedCity = data?.extras?.get("selected_list") as? City
        mBinding.cityRegisterEt.setText(selectedCity?.city_name)
        mViewModel.mCityId.set(selectedCity?.id.toString())
    }

    private fun startCropImageActivity(imageUri: Uri) = CropImage.activity(imageUri)
            .setFixAspectRatio(true)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setMultiTouchEnabled(true)
            .start(this)

    override fun profileUpdateValidation(email: String, phoneNumber: String, firstName: String
                                         , country: String, city: String): Boolean {
        if (TextUtils.isEmpty(firstName)) {
            ViewUtils.showToast(this, getString(R.string.error_firstname), false)
            mViewModel.showLoading.value = false
            return false
        } else if (TextUtils.isEmpty(phoneNumber) && ValidationUtils.isMinLength(phoneNumber, 6)) {
            ViewUtils.showToast(this, getString(R.string.error_invalid_phonenumber), false)
            mViewModel.showLoading.value = false
            return false
        } else if (TextUtils.isEmpty(email) && !ValidationUtils.isValidEmail(email)) {
            ViewUtils.showToast(this, getString(R.string.error_invalid_email_address), false)
            mViewModel.showLoading.value = false
            return false
        } else if (TextUtils.isEmpty(country)) {
            ViewUtils.showToast(this, getString(R.string.error_select_country), false)
            mViewModel.showLoading.value = false
            return false
        } else if (TextUtils.isEmpty(city)) {
            ViewUtils.showToast(this, getString(R.string.error_select_city), false)
            mViewModel.showLoading.value = false
            return false
        }
        return true
    }

    override fun goToCityListActivity(countryId: ObservableField<String>) =
            if (TextUtils.isEmpty(countryId.toString()))
                ViewUtils.showToast(this, getString(R.string.error_select_country), false)
            else {
                val intent = Intent(this, CityListActivity::class.java)
                intent.putExtra("selectedfrom", "city")
                intent.putExtra("citylistresponse", city as Serializable)
                startActivityForResult(intent, Constants.CITY_LIST_REQUEST_CODE)
            }
}