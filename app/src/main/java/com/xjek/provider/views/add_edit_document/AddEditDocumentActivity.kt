package com.xjek.provider.views.add_edit_document

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.DatePicker
import androidx.databinding.ViewDataBinding
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.DateTimeUtil
import com.xjek.base.utils.ImageCropperUtils
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityAddEditDocumentBinding
import com.xjek.provider.utils.Constant
import com.xjek.provider.utils.Enums
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import kotlinx.android.synthetic.main.activity_add_edit_document.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import java.io.File
import java.util.*


class AddEditDocumentActivity : BaseActivity<ActivityAddEditDocumentBinding>(),
        DocumentUploadNavigator, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityAddEditDocumentBinding
    private lateinit var viewModelAddEdit: AddEditDocumentViewModel

    private lateinit var calendar: Calendar

    private var requestCode: Int = -1
    

    override fun getLayoutId(): Int = R.layout.activity_add_edit_document

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityAddEditDocumentBinding
        binding.lifecycleOwner = this
        viewModelAddEdit = provideViewModel {
            AddEditDocumentViewModel()
        }
        viewModelAddEdit.navigator = this

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text = intent.getStringExtra(Constant.DOCUMENT_NAME)
        binding.viewModel = viewModelAddEdit

        // setDocumentData(intent.extras!!)

        calendar = Calendar.getInstance(Locale.getDefault())
//        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//        viewModelAddEdit.expiryDate.value = simpleDateFormat.format(calendar.time)

        observeLiveData(viewModelAddEdit.showLoading) {
            loadingObservable.value = it
        }

        viewModelAddEdit.getDocumentList(intent.getStringExtra(Constant.DOCUMENT_TYPE))

        observeResponses()

    }

    private fun observeResponses() {

        observeLiveData(viewModelAddEdit.documentResponse) { data ->
            run {
                viewModelAddEdit.setData(data.responseData)
            }
        }

        observeLiveData(viewModelAddEdit.addDocumentResponse) {
            ViewUtils.showToast(this, getString(R.string.docuemnt_added_success), true)
            viewModelAddEdit.incrementPosition()
        }

        observeLiveData(viewModelAddEdit.errorResponse) { error ->
            run {
                ViewUtils.showToast(this, error, false)
                viewModelAddEdit.showEmpty.value = true
            }
        }

        observeLiveData(viewModelAddEdit.documentFrontImageURL) { url ->
            run {

                if (viewModelAddEdit.getFileType().equals(Enums.IMAGE_TYPE, true)) {
                    val circularProgressDrawable = getCircularProgressDrawable()
                    if (!url.isNullOrEmpty()) {
                        Glide.with(this@AddEditDocumentActivity)
                                .load(url)
                                .placeholder(circularProgressDrawable)
                                .into(ivFrontImage)
                        viewModelAddEdit.showFrontView.value = true
                    } else {
                        ivFrontImage.setImageDrawable(null)
                    }

                } else {
                    viewModelAddEdit.isPDF.value = true
                    ivFrontImage.setImageResource(R.drawable.ic_pdf)
                }
            }
        }

        observeLiveData(viewModelAddEdit.documentBackImageURL) { url ->
            run {

                if (viewModelAddEdit.getFileType().equals(Enums.IMAGE_TYPE, true)) {
                    val circularProgressDrawable = getCircularProgressDrawable()
                    if (!url.isNullOrEmpty()) {
                        Glide.with(this@AddEditDocumentActivity)
                                .load(url)
                                .placeholder(circularProgressDrawable)
                                .into(ivBackImage)
                        viewModelAddEdit.showBackView.value = true
                    } else {
                        ivBackImage.setImageDrawable(null)
                        viewModelAddEdit.showBackView.value = false

                    }
                }else{
                    viewModelAddEdit.isPDF.value = true
                    ivBackImage.setImageResource(R.drawable.ic_pdf)
                }

            }

        }
    }

    private fun getCircularProgressDrawable(): CircularProgressDrawable {
        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        return circularProgressDrawable
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModelAddEdit.expiryDate.value = DateTimeUtil().constructDateString(year, month, dayOfMonth,
                "-")
    }

    override fun onDateChanged() {
        val datePickerDialog = DatePickerDialog(this, this, calendar.time.year,
                calendar.time.month, calendar.time.date)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }

    override fun selectFrontImage() {
        Dexter.withActivity(this@AddEditDocumentActivity)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        requestCode = Enums.DOCUMENT_UPLOAD_FRONT

                        if (viewModelAddEdit.getFileType().equals(Enums.IMAGE_TYPE, true)) {
                            ImageCropperUtils.launchImageCropperActivity(this@AddEditDocumentActivity)
                        } else {
                          /*  val intent = Intent(Intent.ACTION_GET_CONTENT)
                            intent.type = "application/pdf"
                            intent.addCategory(Intent.CATEGORY_OPENABLE)
                            startActivityForResult(intent,Enums.DOCUMENT_UPLOAD_PDF)*/
                            FilePickerBuilder.instance
                                    .setMaxCount(1)
                                    .addFileSupport("Select Front Page", arrayOf(".pdf"), R.drawable.ic_pdf)
                                    .setActivityTheme(R.style.LibAppTheme)
                                    .enableDocSupport(false)
                                    .enableSelectAll(false)
                                    .withOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    .pickFile(this@AddEditDocumentActivity)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }
                }).check()
    }


    override fun selectBackImage() {
        Dexter.withActivity(this@AddEditDocumentActivity)
                .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        if (viewModelAddEdit.getFileType().equals(Enums.IMAGE_TYPE, true)) {
                            requestCode = Enums.DOCUMENT_UPLOAD_BACK
                            ImageCropperUtils.launchImageCropperActivity(this@AddEditDocumentActivity)
                        } else {
                            FilePickerBuilder.instance
                                    .setMaxCount(1)
                                    .addFileSupport("Select Back Page", arrayOf(".pdf"), R.drawable.ic_pdf)
                                    .setActivityTheme(R.style.LibAppTheme)
                                    .enableDocSupport(false)
                                    .enableSelectAll(false)
                                    .withOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                                    .pickFile(this@AddEditDocumentActivity)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }
                }).check()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    when (this.requestCode) {

                        Enums.DOCUMENT_UPLOAD_FRONT -> {
                            val frontImageFile = File(result.uri.path)
                            Glide.with(this@AddEditDocumentActivity)
                                    .load(frontImageFile)
                                    .into(ivFrontImage)
                            viewModelAddEdit.documentFrontImageFile.value = frontImageFile
                            viewModelAddEdit.showFrontView.value = true
                        }

                        else -> {
                            val backImageFile = File(result.uri.path)
                            Glide.with(this@AddEditDocumentActivity)
                                    .load(backImageFile)
                                    .into(ivBackImage)
                            viewModelAddEdit.documentBackImageFile.value = backImageFile
                            viewModelAddEdit.showBackView.value = true
                        }
                    }

                }
            }


            FilePickerConst.REQUEST_CODE_DOC -> {
                if (resultCode == Activity.RESULT_OK && data !=null) {
                    when (this.requestCode) {
                        Enums.DOCUMENT_UPLOAD_FRONT ->{
                            val selectedDoc = data?.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)
                            viewModelAddEdit.documentFrontImageFile.value = File(selectedDoc!![0])
                            viewModelAddEdit.showFrontView.value = true
                        }

                       else ->{
                           val selectedDoc = data?.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)
                           viewModelAddEdit.documentBackImageFile.value = File(selectedDoc!![0])
                           viewModelAddEdit.showFrontView.value = true
                        }

                    }
                }
            }
        }
    }

    override fun submitDocument() {
        if (viewModelAddEdit.documentFrontImageFile.value == null || !viewModelAddEdit.documentFrontImageFile.value!!.isFile) {
            ViewUtils.showToast(this, "Please select front page of document", false)
        } else if (viewModelAddEdit.showBackSide.value!! && (viewModelAddEdit.documentBackImageFile.value == null || !viewModelAddEdit.documentBackImageFile.value!!.isFile)) {
            ViewUtils.showToast(this, "Please select back page of document", false)
        } else if (viewModelAddEdit.showExpiry.value!! && viewModelAddEdit.expiryDate.value.isNullOrEmpty()) {
            ViewUtils.showToast(this, "Please select expiry date", false)
        } else {
            viewModelAddEdit.updateDocument()
        }

    }

}
