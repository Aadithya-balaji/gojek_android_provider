@file:Suppress("UNCHECKED_CAST")

package com.gox.partner.views.add_edit_document

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.DatePicker
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.DateTimeUtil
import com.gox.base.utils.ImageCropperUtils
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityAddEditDocumentBinding
import com.gox.partner.utils.Enums
import com.gox.partner.utils.Enums.Companion.PDF_EXTENSION
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_add_edit_document.*
import java.io.File
import java.util.*

class AddEditDocumentActivity : BaseActivity<ActivityAddEditDocumentBinding>(),
        DocumentUploadNavigator, DatePickerDialog.OnDateSetListener {

    private lateinit var mBinding: ActivityAddEditDocumentBinding
    private lateinit var mViewModel: AddEditDocumentViewModel
    private lateinit var calendar: Calendar

    private var requestCode: Int = -1
    private var frontFileDownloadID: Int? = 0
    private var backFileDownloadID: Int? = null

    override fun getLayoutId() = R.layout.activity_add_edit_document

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityAddEditDocumentBinding
        mBinding.lifecycleOwner = this
        mViewModel = provideViewModel {
            AddEditDocumentViewModel()
        }
        mViewModel.navigator = this

        setSupportActionBar(mBinding.tbApp)
        iv_toolbar_back.setOnClickListener { onBackPressed() }
        tv_toolbar_title.text = intent.getStringExtra(Constants.DOCUMENT_NAME)
        mBinding.viewModel = mViewModel

        calendar = Calendar.getInstance(Locale.getDefault())

        observeLiveData(mViewModel.showLoading) {
            loadingObservable.value = it
        }

        tv_toolbar_title.text = intent.getStringExtra(Constants.DOCUMENT_NAME)
        mViewModel.setData(intent.getParcelableExtra(Constants.DOCUMENT_TYPE))
        observeResponses()

        mViewModel.expiryDate.value = DateTimeUtil().constructDateString(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), "-")
    }

    private fun observeResponses() {

        observeLiveData(mViewModel.documentResponse) {
            // mViewModel.setData(it.responseData)
        }

        observeLiveData(mViewModel.addDocumentResponse) {
            ViewUtils.showToast(this, getString(R.string.docuemnt_added_success), true)
            it.responseData.expires_at = it.responseData.expires_at.plus(" 00:00:00")
            setResult(Activity.RESULT_OK, Intent().putExtra(Constants.DOCUMENT_TYPE, it.responseData))
            finish()
//            if (mViewModel.getData().size > (mViewModel.currentPosition + 1))
//                mViewModel.incrementPosition() else finish()
        }

        observeLiveData(mViewModel.errorResponse) { error ->
            run {
                ViewUtils.showToast(this, error, false)
                mViewModel.showEmpty.value = true
            }
        }

        observeLiveData(mViewModel.documentFrontImageURL) { url ->
            run {
                if (mViewModel.getFileType().equals(Enums.IMAGE_TYPE, true)) {
                    val circularProgressDrawable = getCircularProgressDrawable()
                    if (!url.isNullOrEmpty()) {
                        Glide.with(this)
                                .load(url)
                                .placeholder(circularProgressDrawable)
                                .into(ivFrontImage)
                        mViewModel.showFrontView.value = true
                    } else {
                        ivFrontImage.setImageDrawable(null)
                        mViewModel.showFrontView.value = false
                    }
                    mViewModel.isPDF.value = false
                } else {
                    mViewModel.isPDF.value = true
                    mViewModel.showFrontView.value = !url.isNullOrEmpty()
                }
                if (!url.isNullOrEmpty()) downloadFrontSideFile(url)
            }
        }

        observeLiveData(mViewModel.documentBackImageURL) { url ->
            run {
                if (mViewModel.getFileType().equals(Enums.IMAGE_TYPE, true)) {
                    val circularProgressDrawable = getCircularProgressDrawable()
                    if (!url.isNullOrEmpty()) {
                        Glide.with(this)
                                .load(url)
                                .placeholder(circularProgressDrawable)
                                .into(ivBackImage)
                        mViewModel.showBackView.value = true
                    } else {
                        ivBackImage.setImageDrawable(null)
                        mViewModel.showBackView.value = false
                    }
                    mViewModel.isPDF.value = false
                } else {
                    mViewModel.isPDF.value = true
                    mViewModel.showBackView.value = !url.isNullOrEmpty()
                }
                if (!url.isNullOrEmpty()) downloadBackSideFile(url)
            }
        }
    }

    private fun downloadBackSideFile(url: String?) {
        backFileDownloadID = PRDownloader.download(url, cacheDir.path, "back_image")
                .build().start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        mViewModel.documentBackImageFile.value =
                                File(cacheDir.path + File.separator + "back_image")
                    }

                    override fun onError(error: Error?) {
                    }
                })
    }

    private fun downloadFrontSideFile(url: String?) {
        frontFileDownloadID = PRDownloader.download(url, cacheDir.path, "front_image")
                .build().start(object : OnDownloadListener {
                    override fun onDownloadComplete() {
                        mViewModel.documentFrontImageFile.value =
                                File(cacheDir.path + File.separator + "front_image")
                    }

                    override fun onError(error: Error?) {
                    }
                })
    }

    private fun getCircularProgressDrawable(): CircularProgressDrawable {
        val cpd = CircularProgressDrawable(this)
        cpd.strokeWidth = 5f
        cpd.centerRadius = 30f
        cpd.start()
        return cpd
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        mViewModel.expiryDate.value = DateTimeUtil().constructDateString(year, month, dayOfMonth, "-")
    }

    override fun onDateChanged() {
        val datePickerDialog = /*DatePickerDialog(this, this, calendar.time.year,
                calendar.time.month, calendar.time.date)*/

                DatePickerDialog(this, this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    override fun selectFrontImage() = Dexter.withActivity(this)
            .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    requestCode = Enums.DOCUMENT_UPLOAD_FRONT

                    if (mViewModel.getFileType().equals(Enums.IMAGE_TYPE, true))
                        ImageCropperUtils.launchImageCropperActivity(this@AddEditDocumentActivity)
                    else {
                        FilePickerBuilder.instance
                                .setMaxCount(1)
                                .addFileSupport(getString(R.string.select_front_page), arrayOf(PDF_EXTENSION), R.drawable.ic_pdf)
                                .setActivityTheme(R.style.LibAppTheme)
                                .enableDocSupport(false)
                                .enableSelectAll(false)
                                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                .pickFile(this@AddEditDocumentActivity)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()

    override fun selectBackImage() = Dexter.withActivity(this)
            .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    requestCode = Enums.DOCUMENT_UPLOAD_BACK
                    if (mViewModel.getFileType().equals(Enums.IMAGE_TYPE, true))
                        ImageCropperUtils.launchImageCropperActivity(this@AddEditDocumentActivity) else {
                        FilePickerBuilder.instance
                                .setMaxCount(1)
                                .addFileSupport(getString(R.string.select_back_page), arrayOf(PDF_EXTENSION), R.drawable.ic_pdf)
                                .setActivityTheme(R.style.LibAppTheme)
                                .enableDocSupport(false)
                                .enableSelectAll(false)
                                .withOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                .pickFile(this@AddEditDocumentActivity)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                        permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                    token?.continuePermissionRequest()
                }
            }).check()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    when (this.requestCode) {

                        Enums.DOCUMENT_UPLOAD_FRONT -> {
                            val frontImageFile = File(result.uri.path)
                            Glide.with(this)
                                    .load(frontImageFile)
                                    .into(ivFrontImage)
                            mViewModel.documentFrontImageFile.value = frontImageFile
                            mViewModel.showFrontView.value = true
                        }

                        else -> {
                            val backImageFile = File(result.uri.path)
                            Glide.with(this)
                                    .load(backImageFile)
                                    .into(ivBackImage)
                            mViewModel.documentBackImageFile.value = backImageFile
                            mViewModel.showBackView.value = true
                        }
                    }
                }
            }

            FilePickerConst.REQUEST_CODE_DOC -> {
                if (resultCode == Activity.RESULT_OK && data != null) when (this.requestCode) {
                    Enums.DOCUMENT_UPLOAD_FRONT -> {
                        val selectedDoc = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)
                        val frontPageFile = File(selectedDoc!![0])
                        mViewModel.documentFrontImageFile.value = frontPageFile
                        mViewModel.documentFrontFileName.value = frontPageFile.name
                        mViewModel.showFrontView.value = true
                    }

                    else -> {
                        val selectedDoc = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)
                        val backPageFile = File(selectedDoc!![0])
                        mViewModel.documentBackImageFile.value = backPageFile
                        mViewModel.documentBackFileName.value = backPageFile.name
                        mViewModel.showBackView.value = true
                    }
                }
            }
        }
    }

    override fun submitDocument() =
            if (mViewModel.documentFrontImageFile.value == null
                    || !mViewModel.documentFrontImageFile.value!!.isFile) {
                ViewUtils.showToast(this, getString(R.string.please_select_front_page), false)
            } else if (mViewModel.showBackSide.value!!
                    && (mViewModel.documentBackImageFile.value == null
                            || !mViewModel.documentBackImageFile.value!!.isFile)) {
                ViewUtils.showToast(this, getString(R.string.please_select_back_page_of_document), false)
            } else if (mViewModel.showExpiry.value!! && mViewModel.expiryDate.value.isNullOrEmpty())
                ViewUtils.showToast(this, getString(R.string.please_select_expiry_date), false)
            else mViewModel.updateDocument()

    override fun onDestroy() {
        super.onDestroy()
        PRDownloader.cancelAll()
    }
}