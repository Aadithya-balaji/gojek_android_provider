package com.gox.xuberservice.uploadImage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.ViewDataBinding
import com.facebook.FacebookSdk.getApplicationContext
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants
import com.gox.base.utils.CommonMethods
import com.gox.base.utils.ViewUtils
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.DialogUploadImageBinding
import com.gox.xuberservice.interfaces.GetFilePathInterface
import com.theartofdev.edmodo.cropper.CropImage
import java.io.File

class UploadPictureDialog : BaseDialogFragment<DialogUploadImageBinding>(), UploadPictureDialogNavigator {

    private lateinit var mBinding: DialogUploadImageBinding
    private lateinit var mViewModel: UploadPictureDialogViewModel
    private lateinit var mediaUri: Uri
    private lateinit var mediaFile: File
    private lateinit var appCompatActivity: AppCompatActivity
    private lateinit var getFilePath: GetFilePathInterface

    private var localPath: Uri? = null
    private var isFront: Boolean = false

    override fun getLayout() = R.layout.dialog_upload_image

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appCompatActivity = context as AppCompatActivity
        getFilePath = context as GetFilePathInterface
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
    }

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as DialogUploadImageBinding
        mViewModel = UploadPictureDialogViewModel()
        mViewModel.navigator = this
        mBinding.lifecycleOwner = this
        mBinding.uploadImageModel = mViewModel
        getBundleValues()
        if (isFront) mBinding.tvServiceState.text = resources.getString(R.string.before_service)
        else mBinding.tvServiceState.text = resources.getString(R.string.after_service)

    }

    private fun getBundleValues() {
        isFront = if (arguments != null && arguments!!.containsKey("isFront"))
            arguments!!.getBoolean("isFront", true) else true
    }

    override fun takePicture() {
        if (getPermissionUtil().hasPermission(appCompatActivity, Constants.RequestPermission.PERMISSION_CAMERA)) {
            captureImage(202)
        } else getPermissionUtil().requestPermissions(appCompatActivity,
                Constants.RequestPermission.PERMISSION_CAMERA, Constants.RequestCode.PERMISSION_CODE_CAMERA)
    }

    override fun submit() {
        if (localPath != null) {
            getFilePath.getFilePath(localPath!!)
            dialog!!.dismiss()
        } else ViewUtils.showToast(appCompatActivity, resources.getString(R.string.empty_image), false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("ResultCode", "------$resultCode---$requestCode")
        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                202 -> {
                    CommonMethods.refreshGallery(activity!!, mediaFile)
                    val intent = CropImage.activity(mediaUri).getIntent(context!!)
                    startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
                }

                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val result = CropImage.getActivityResult(data)
                    if (resultCode == Activity.RESULT_OK) {
                        localPath = result.uri
                        mBinding.llCaptureImage.visibility = View.GONE
                        mBinding.ivServiceImg.setImageURI(localPath)
                        mBinding.ivServiceImg.visibility = View.VISIBLE
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                        ViewUtils.showNormalToast(activity!!, getText(R.string.crop_failed) as String)
                }
            }
        }
    }

    private fun captureImage(requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        mediaFile = CommonMethods.createImageFile(activity!!)
        mediaUri = FileProvider.getUriForFile(activity!!, getApplicationContext().packageName + ".provider", mediaFile)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mediaUri)
        startActivityForResult(intent, requestCode)
    }
}