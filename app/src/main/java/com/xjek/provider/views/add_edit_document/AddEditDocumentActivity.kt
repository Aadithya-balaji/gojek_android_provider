package com.xjek.provider.views.add_edit_document

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.DatePicker
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.DateTimeUtil
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityAddEditDocumentBinding
import com.xjek.provider.utils.Constant
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddEditDocumentActivity : BaseActivity<ActivityAddEditDocumentBinding>(),
        DocumentUploadNavigator, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityAddEditDocumentBinding
    private lateinit var viewModelAddEdit: AddEditDocumentViewModel

    private lateinit var calendar: Calendar
    override fun getLayoutId(): Int {
        return R.layout.activity_add_edit_document
    }

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
            ViewUtils.showToast(this, "Document added successfully", false)
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
                if (!url.isNullOrEmpty()) {
                    Glide.with(this@AddEditDocumentActivity)
                            .load(url)
                            .into(binding.ivFrontImage)
                    viewModelAddEdit.showFrontView.value = true
                } else {
                    binding.ivFrontImage.setImageDrawable(null)
                    viewModelAddEdit.showFrontView.value = false

                }
            }
        }

        observeLiveData(viewModelAddEdit.documentBackImageURL) { url ->
            run {
                if (!url.isNullOrEmpty()) {
                    Glide.with(this@AddEditDocumentActivity)
                            .load(url)
                            .into(binding.ivBackImage)
                    viewModelAddEdit.showBackView.value = true
                } else {
                    binding.ivBackImage.setImageDrawable(null)
                    viewModelAddEdit.showBackView.value = false

                }
            }

        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModelAddEdit.expiryDate.value = DateTimeUtil().constructDateString(year, month, dayOfMonth,
                "/")
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
                        EasyImage.openGallery(this@AddEditDocumentActivity, Constant.PICK_FRONT_IMAGE)
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
                        EasyImage.openGallery(this@AddEditDocumentActivity, Constant.PICK_BACK_IMAGE)
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }
                }).check()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(requestCode, resultCode, data, this@AddEditDocumentActivity, object : EasyImage.Callbacks {
            override fun onImagesPicked(imageFiles: MutableList<File>, source: EasyImage.ImageSource?, type: Int) {
                val myBitmap = BitmapFactory.decodeFile(imageFiles[0].absolutePath)
                if (type == Constant.PICK_FRONT_IMAGE) {
                    binding.ivFrontImage.setImageBitmap(myBitmap)
                    viewModelAddEdit.documentFrontImageFile.value = imageFiles[0]
                    viewModelAddEdit.showFrontView.value = true
                } else {
                    binding.ivBackImage.setImageBitmap(myBitmap)
                    viewModelAddEdit.documentBackImageFile.value = imageFiles[0]
                    viewModelAddEdit.showBackView.value = true
                }
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
            }
        })
    }

    override fun submitDocument() {
        if (viewModelAddEdit.documentFrontImageFile.value == null || !viewModelAddEdit.documentFrontImageFile.value!!.isFile) {
            ViewUtils.showToast(this, "Please select front page of document", false)
        } else if (viewModelAddEdit.showBackSide.value!! && (viewModelAddEdit.documentBackImageFile.value == null || !viewModelAddEdit.documentBackImageFile.value!!.isFile)) {
            ViewUtils.showToast(this, "Please select back page of document", false)
        } else if (viewModelAddEdit.showExpiry.value!! && !viewModelAddEdit.expiryDate.value.isNullOrEmpty()) {
            ViewUtils.showToast(this, "Please select expiry date", false)
        } else {
            viewModelAddEdit.updateDocument()
        }

    }

}
