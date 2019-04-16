package com.xjek.provider.views.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.RunTimePermission
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityEditProfileBinding
import com.xjek.provider.utils.Constant
import com.xjek.provider.utils.Enums
import com.xjek.provider.views.privacypolicy.PrivacyActivity
import com.xjek.xjek.ui.profile.ProfileNavigator
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ProfileActivity : BaseActivity<ActivityEditProfileBinding>(), ProfileNavigator {

    private lateinit var mViewDataBinding: ActivityEditProfileBinding
    private var mProfileViewModel: ProfileViewModel? = null
    private var mCropImageUri: Uri? = null
    private var localPath: Uri? = null
    private var message: String? = ""
    private var filePart: MultipartBody.Part? = null
    private var permissionUtils:RunTimePermission? = null
    companion object {
        var loadingProgress: MutableLiveData<Boolean>?=null

    }
    override fun getLayoutId(): Int = R.layout.activity_edit_profile
    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityEditProfileBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.title_profile)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }
        mProfileViewModel = ProfileViewModel()
        mProfileViewModel!!.getProfile()
        mProfileViewModel!!.navigator=this
        mViewDataBinding.profileviewmodel=mProfileViewModel
        mViewDataBinding.setLifecycleOwner(this)
        loadingProgress = loadingObservable as MutableLiveData<Boolean>
        mProfileViewModel!!.showLoading= loadingProgress as MutableLiveData<Boolean>
        getApiResponse()
        permissionUtils=getPermissioUtil()
        getProfile()
    }

    fun getProfile(){
        loadingProgress!!.value=true
        mProfileViewModel!!.getProfile()
    }

    private fun checkAllPermission(permission: Array<String>) {
        val blockedPermission = runtimePermission!!.checkHasPermission(this, permission)
        if (blockedPermission != null && blockedPermission.size > 0) {
            val isBlocked = runtimePermission!!.isPermissionBlocked(this, blockedPermission)
            if (isBlocked) {
                callPermissionSettings()
            } else {
                ActivityCompat.requestPermissions(this, permission, 150)
            }
        }

    }

    fun getApiResponse() {
        observeLiveData(mProfileViewModel!!.getProfileRespose()) {
            loadingProgress!!.value=false
            if (it.statusCode == "200") {
                val profileData = it.profileData
                if (profileData != null) {
                    if (!profileData.firstName.isNullOrEmpty()) mProfileViewModel!!.mFirstName.postValue(profileData.firstName)
                    if (!profileData.lastName.isNullOrEmpty()) mProfileViewModel!!.mLastName.postValue(profileData.lastName)
                    if (!profileData.email.isNullOrEmpty()) mProfileViewModel!!.mEmail.postValue(profileData.email)
                    if (!profileData.mobile.isNullOrEmpty()) mProfileViewModel!!.mMobileNumber.postValue(profileData.mobile)
                }
            }
        }
    }


    fun callPermissionSettings(){
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = Uri.fromParts("package", this.applicationContext.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 300)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Enums.FILE_REQ_CODE) {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    localPath = result.uri
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                val result = CropImage.getActivityResult(data)
                mViewDataBinding.profileImage.setImageURI(result.uri)
                val profileFile = File(result.uri.toString())
                if (profileFile != null && profileFile.exists()) {
                    filePart = MultipartBody.Part.createFormData("picture", profileFile.getName(), RequestBody.create(MediaType.parse("image*//*"), profileFile));
                    mProfileViewModel!!.filePath.value = filePart
                }
            }
        }

    }

    override fun pickImage() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            checkAllPermission(Constant.PERMISSIONS_FILE)
        } else {
            CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this)
        }
    }

    override fun saveProfile() {
        if (validateCredential()) {
            mProfileViewModel!!.updateProfile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionUtils!!.setFirstTimePermission(true)
        val permission = permissionUtils!!.onRequestPermissionsResult(permissions, grantResults)
        if (permission != null && permission!!.size > 0) run {
           callPermissionSettings()
        }
    }

    override fun showErrorMsg(error: String) {
        loadingProgress!!.value=false
        ViewUtils.showToast(this, error.toString(), false)
    }

    fun validateCredential(): Boolean {
        if (mProfileViewModel!!.mCity.value.isNullOrEmpty()) {
            message = resources.getString(R.string.empty_city)
            ViewUtils.showToast(this, message!!, false)
            return false
        } else {
            return true
        }
    }





}


