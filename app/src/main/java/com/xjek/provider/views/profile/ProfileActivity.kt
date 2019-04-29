package com.xjek.provider.views.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
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
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.ValidationUtils
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityEditProfileBinding
import com.xjek.provider.model.City
import com.xjek.provider.model.CountryListResponse
import com.xjek.provider.model.CountryResponseData
import com.xjek.provider.models.ProfileResponse
import com.xjek.provider.utils.Constant
import com.xjek.provider.utils.Constant.APP_REQUEST_CODE
import com.xjek.provider.views.change_password.ChangePasswordActivity
import com.xjek.provider.views.citylist.CityListActivity
import com.xjek.provider.views.countrylist.CountryListActivity
import com.xjek.xjek.ui.profile.ProfileNavigator
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.Serializable


class ProfileActivity : BaseActivity<ActivityEditProfileBinding>(), ProfileNavigator {


    lateinit var mViewDataBinding: ActivityEditProfileBinding
    private lateinit var mProfileViewmodel: ProfileViewModel
    private var mCropImageUri: Uri? = null
    private var localPath: Uri? = null
    private var mProfileData: ProfileResponse? = null
    private var mMobileNumberFlag = 0
    override fun getLayoutId(): Int = R.layout.activity_edit_profile
    private lateinit var city: List<City>

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityEditProfileBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.profile)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener {
            finish()
        }
        mProfileViewmodel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        mViewDataBinding.profileviewmodel = mProfileViewmodel
        mProfileViewmodel.navigator = this
        mProfileViewmodel.getProfile()
        mProfileViewmodel.getProfileResponse().observe(this, Observer {
            mProfileData = it
            Glide.with(this@ProfileActivity)
                    .load(it.profileData?.picture)
                    .into(mViewDataBinding.profileImage)
        })
        mProfileViewmodel.updateProfileResponse().observe(this, Observer {
            ViewUtils.showToast(this, it.message!!,true)
        })

        mProfileViewmodel.countryListResponse.observe(this@ProfileActivity, Observer<CountryListResponse> {
            Log.d("_D", "country_code :" + it.responseData[0].country_code)

            val intent = Intent(this@ProfileActivity, CountryListActivity::class.java)
            intent.putExtra("selectedfrom", "country")
            intent.putExtra("countrylistresponse", it as Serializable)
            startActivityForResult(intent, Constant.COUNTRYLIST_REQUEST_CODE)
        })

        observeLiveData(mProfileViewmodel.loadingProgress){
            loadingObservable.value = it
        }

        observeLiveData(mProfileViewmodel.errorResponse){
            ViewUtils.showToast(ProfileActivity@this,it,false)
        }


        mProfileViewmodel.mProfileResponse.observe(this@ProfileActivity, Observer { response ->

            mProfileViewmodel.mUserName.set(response.profileData!!.firstName)
            mProfileViewmodel.mMobileNumber.set(response.profileData!!.mobile)
            mProfileViewmodel.mEmail.set(response.profileData!!.email)
            mProfileViewmodel.mCity.set(response.profileData!!.cityName?.cityName!!)
            mProfileViewmodel.mCountry.set(response.profileData!!.countryName!!.CountryName)
            mProfileViewmodel.mCountryCode.set(response.profileData!!.countryCode)
            mProfileViewmodel.mCountryId.set(response.profileData!!.countryName!!.id)
            mProfileViewmodel.mCityId.set(response.profileData!!.cityName?.id)
            mProfileViewmodel.mProfileImage.set(response.profileData!!.picture)

        })


        setOnclickListteners()
    }

    override fun goToChangePasswordActivity() {
        launchNewActivity(ChangePasswordActivity::class.java, true)
    }

    private fun setOnclickListteners() {
        mViewDataBinding.profileLayout.setOnClickListener {
            checkPermission()
        }
        mViewDataBinding.saveEditprofileBtn.setOnClickListener {

            if (mProfileData?.profileData?.mobile!!.toString().equals(mViewDataBinding.phonenumberRegisterEt.text.toString())) {
                mMobileNumberFlag = 1//same
                if (localPath?.path != null) {
                    val pictureFile = File(localPath?.path)
                    val requestFile = RequestBody.create(
                            MediaType.parse("*/*"),
                            pictureFile)

                    val fileBody = MultipartBody.Part.createFormData("picture", pictureFile.name, requestFile)
                    mProfileViewmodel?.updateProfile(fileBody)
                } else {
                    mProfileViewmodel?.updateProfile(null)
                }
            } else {
                mMobileNumberFlag = 2 // different mobile number
                verifyMobileNumber()
            }


        }
    }

    private fun verifyMobileNumber() {
        val intent = Intent(this@ProfileActivity, AccountKitActivity::class.java)
        val configurationBuilder = AccountKitConfiguration.AccountKitConfigurationBuilder(
                LoginType.PHONE,
                AccountKitActivity.ResponseType.CODE) // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build())
        startActivityForResult(intent, Constant.APP_REQUEST_CODE)
    }

    private fun checkPermission() {
        Dexter.withActivity(this@ProfileActivity)
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
                        ViewUtils.showToast(applicationContext, "Unable to perform this action",false)
                        //finish()
                    }

                }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                val imageUri = CropImage.getPickImageResultUri(this, data)

                // For API >= 23 we need to check specifically that we have permissions to read external storage.
                if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    mCropImageUri = imageUri
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                    }
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
        if (requestCode == Constant.COUNTRYLIST_REQUEST_CODE) {
            setCountry(data)
            mViewDataBinding?.cityRegisterEt.isEnabled = true
        }
        if (requestCode == Constant.CITYLIST_REQUEST_CODE) {
            setCity(data)
        }
        if (resultCode == APP_REQUEST_CODE) {
            accountKitOtpVerified(data)
        }

    }


    private fun setCountry(data: Intent?) {
        val selectedCountry = data?.extras?.get("selected_list") as? CountryResponseData
        Log.d("countrylist", selectedCountry?.country_name + "")
        city = (selectedCountry?.city as? List<City>)!!
        mViewDataBinding.countryRegisterEt.setText(selectedCountry.country_name)
        mProfileViewmodel?.mCountryId!!.set(selectedCountry.id.toString())
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
                mProfileViewmodel?.updateProfile(fileBody)
            } else {
                mProfileViewmodel?.updateProfile(null)
            }

        }
    }

    private fun setCity(data: Intent?) {
        val selectedCity = data?.extras?.get("selected_list") as? City
        Log.d("statelist", selectedCity?.city_name + "")
        mViewDataBinding.cityRegisterEt.setText(selectedCity?.city_name)
        mProfileViewmodel?.mCityId!!.set(selectedCity?.id.toString())
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
            ViewUtils.showToast(this@ProfileActivity, getString(R.string.error_firstname), false)
            mProfileViewmodel?.loadingProgress!!.value = false
            return false
        } else if (TextUtils.isEmpty(phonenumber) && ValidationUtils.isMinLength(phonenumber, 6)) {
            ViewUtils.showToast(this@ProfileActivity, getString(R.string.error_invalid_phonenumber), false)
            mProfileViewmodel?.loadingProgress!!.value = false
            return false
        } else if (TextUtils.isEmpty(email) && !ValidationUtils.isValidEmail(email)) {
            ViewUtils.showToast(this@ProfileActivity, getString(R.string.error_invalid_email_address), false)
            mProfileViewmodel?.loadingProgress!!.value = false
            return false
        } else if (TextUtils.isEmpty(country)) {
            ViewUtils.showToast(this@ProfileActivity, getString(R.string.error_select_country), false)
            mProfileViewmodel?.loadingProgress!!.value = false
            return false
        } else if (TextUtils.isEmpty(city)) {
            ViewUtils.showToast(this@ProfileActivity, getString(R.string.error_select_city), false)
            mProfileViewmodel?.loadingProgress!!.value = false
            return false
        }
        return true
    }

    override fun goToCityListActivity(countryId: ObservableField<String>) {
        if (TextUtils.isEmpty(countryId.toString())) {
            ViewUtils.showToast(this@ProfileActivity, getString(R.string.error_select_country), false)
        } else {

            val intent = Intent(this@ProfileActivity, CityListActivity::class.java)
            intent.putExtra("selectedfrom", "city")
            intent.putExtra("citylistresponse", city as Serializable)
            startActivityForResult(intent, Constant.CITYLIST_REQUEST_CODE)
        }
    }


}


