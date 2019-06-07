package com.gox.partner.views.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.accountkit.AccountKitLoginResult
import com.facebook.accountkit.ui.AccountKitActivity
import com.facebook.accountkit.ui.AccountKitConfiguration
import com.facebook.accountkit.ui.LoginType
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
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
import com.gox.partner.models.CountryResponseData
import com.gox.partner.utils.Country
import com.gox.partner.views.change_password.ChangePasswordActivity
import com.gox.partner.views.citylist.CityListActivity
import com.gox.partner.views.countrylist.CountryListActivity
import com.gox.xjek.ui.profile.ProfileNavigator
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.Serializable

class ProfileActivity : BaseActivity<ActivityEditProfileBinding>(), ProfileNavigator {

    private lateinit var mViewDataBinding: ActivityEditProfileBinding
    private lateinit var mViewModel: ProfileViewModel
    private lateinit var city: List<City>

    private var mCropImageUri: Uri? = null
    private var localPath: Uri? = null
    private var mMobileNumberFlag = 0

    override fun getLayoutId() = R.layout.activity_edit_profile

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityEditProfileBinding
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
            startActivityForResult(intent, Constants.COUNTRYLIST_REQUEST_CODE)
        })

        observeLiveData(mViewModel.loadingProgress) {
            loadingObservable.value = it
        }

        observeLiveData(mViewModel.errorResponse) {
            ViewUtils.showToast(this, it, false)
        }

        mViewModel.mProfileResponse.observe(this, Observer { response ->
            mViewModel.mUserName.set(response.profileData.first_name)
            mViewModel.mMobileNumber.set(response.profileData.mobile)
            mViewModel.mEmail.set(response.profileData.email)
            mViewModel.mCity.set(response.profileData.city.city_name)
            mViewModel.mCountry.set(response.profileData.country.country_name)
            mViewModel.mCountryId.set(response.profileData.country.id.toString())
            mViewModel.mCityId.set(response.profileData.city.id.toString())
            mViewModel.mProfileImage.set(response.profileData.picture)

            if (response.profileData.picture != null)
                glideSetImageView(mViewDataBinding.profileImage, response.profileData.picture, R.drawable.dummy_profile_pic)

            handleCountryCodePickerResult(response.profileData.country.country_name)
        })

        setOnclickListeners()
    }

    private fun handleCountryCodePickerResult(country: String?) {
        var countryFlag = 0

        for (countryList in Country.getAllCountries())
            if (country!!.contains(countryList.name)) {
                countryFlag = countryList.flag
                mViewModel.mCountryCode.set(countryList.dialCode)
            }

        val leftDrawable = ContextCompat.getDrawable(this, countryFlag)
        if (leftDrawable != null) {
            val bitmap = (leftDrawable as BitmapDrawable).bitmap
            val drawable = BitmapDrawable(resources, Bitmap.createScaledBitmap(bitmap, 50, 50, true))
            countrycode_register_et.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        }
    }

    override fun goToChangePasswordActivity() {
        launchNewActivity(ChangePasswordActivity::class.java, true)
    }

    private fun setOnclickListeners() {
        mViewDataBinding.profileLayout.setOnClickListener {
            checkPermission()
        }

        save_editprofile_btn.setOnClickListener {
            if (mViewModel.mProfileResponse.value!!.profileData.mobile == phonenumber_register_et.text.toString()) {
                mMobileNumberFlag = 1   //  same
                if (localPath?.path != null) {
                    val pictureFile = File(localPath?.path)
                    val requestFile = RequestBody.create(MediaType.parse("*/*"), pictureFile)
                    val fileBody = MultipartBody.Part.createFormData("picture", pictureFile.name, requestFile)
                    mViewModel.updateProfile(fileBody)
                } else mViewModel.updateProfile(null)
            } else {
                mMobileNumberFlag = 2   // different mobile number
                verifyMobileNumber()
            }
        }
    }

    private fun verifyMobileNumber() {
        val intent = Intent(this, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE, AccountKitActivity.ResponseType.CODE)
        // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build())
        startActivityForResult(intent, APP_REQUEST_CODE)
    }

    private fun checkPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        CropImage.startPickImageActivity(this@ProfileActivity)
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                        //close activity
                        token?.continuePermissionRequest()
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        //close activity
                        ViewUtils.showToast(applicationContext, "Unable to perform this action", false)
                        //finish()
                    }

                }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                val imageUri = CropImage.getPickImageResultUri(this, data)

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = imageUri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                } else {
                    // no permissions required or already grunted, can start crop image activity
                    startCropImageActivity(imageUri)
                }
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                mViewDataBinding.profileImage.setImageURI(result.uri)
                localPath = result.uri
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: ", Toast.LENGTH_LONG).show()
            }
        }
        if (requestCode == Constants.COUNTRYLIST_REQUEST_CODE && data!=null) {
            setCountry(data)
            mViewDataBinding.cityRegisterEt.isEnabled = true
            mViewModel.loadingProgress.value = false
        }else{
            mViewModel.loadingProgress.value = false
        }
        if (requestCode == Constants.CITYLIST_REQUEST_CODE && data!=null) {
            setCity(data)
            mViewModel.loadingProgress.value = false
        }else{
            mViewModel.loadingProgress.value = false
        }
        if (resultCode == APP_REQUEST_CODE && data!=null) {
            accountKitOtpVerified(data)
            mViewModel.loadingProgress.value = false
        }else{
            mViewModel.loadingProgress.value = false
        }
    }

    private fun setCountry(data: Intent?) {
        val selectedCountry = data?.extras?.get("selected_list") as? CountryResponseData
        Log.d("countrylist", selectedCountry?.country_name + "")
        city = selectedCountry?.city!!
        mViewDataBinding.countryRegisterEt.setText(selectedCountry.country_name)
        mViewModel.mCountryId.set(selectedCountry.id.toString())
    }

    private fun accountKitOtpVerified(data: Intent?) {
        val loginResult: AccountKitLoginResult = data!!.getParcelableExtra(AccountKitLoginResult.RESULT_KEY)
        if (loginResult.getError() != null) {
            Log.d("_D_fbaccountkit", loginResult.getError().toString())
        } else if (loginResult.wasCancelled()) {
            Log.d("_D_fbaccountkit", "Fb login cancelled")
        } else {
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
    }

    private fun setCity(data: Intent?) {
        val selectedCity = data?.extras?.get("selected_list") as? City
        Log.d("statelist", selectedCity?.city_name + "")
        mViewDataBinding.cityRegisterEt.setText(selectedCity?.city_name)
        mViewModel.mCityId.set(selectedCity?.id.toString())
    }

    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
                .setFixAspectRatio(true)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setMultiTouchEnabled(true)
                .start(this)
    }

    override fun profileUpdateValidation(email: String, phonenumber: String, firstname: String
                                         , country: String, city: String): Boolean {

        if (TextUtils.isEmpty(firstname)) {
            ViewUtils.showToast(this, getString(R.string.error_firstname), false)
            mViewModel.loadingProgress.value = false
            return false
        } else if (TextUtils.isEmpty(phonenumber) && ValidationUtils.isMinLength(phonenumber, 6)) {
            ViewUtils.showToast(this, getString(R.string.error_invalid_phonenumber), false)
            mViewModel.loadingProgress.value = false
            return false
        } else if (TextUtils.isEmpty(email) && !ValidationUtils.isValidEmail(email)) {
            ViewUtils.showToast(this, getString(R.string.error_invalid_email_address), false)
            mViewModel.loadingProgress.value = false
            return false
        } else if (TextUtils.isEmpty(country)) {
            ViewUtils.showToast(this, getString(R.string.error_select_country), false)
            mViewModel.loadingProgress.value = false
            return false
        } else if (TextUtils.isEmpty(city)) {
            ViewUtils.showToast(this, getString(R.string.error_select_city), false)
            mViewModel.loadingProgress.value = false
            return false
        }
        return true
    }

    override fun goToCityListActivity(countryId: ObservableField<String>) {
        if (TextUtils.isEmpty(countryId.toString())) {
            ViewUtils.showToast(this, getString(R.string.error_select_country), false)
        } else {
            val intent = Intent(this, CityListActivity::class.java)
            intent.putExtra("selectedfrom", "city")
            intent.putExtra("citylistresponse", city as Serializable)
            startActivityForResult(intent, Constants.CITYLIST_REQUEST_CODE)
        }
    }
}