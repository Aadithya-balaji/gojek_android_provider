package com.xjek.provider.views.document_upload

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.DateTimeUtil
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityDocumentUploadBinding
import com.xjek.provider.models.DocumentUploadDataModel
import com.xjek.provider.utils.Constant
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import java.text.SimpleDateFormat
import java.util.*


class DocumentUploadActivity : BaseActivity<ActivityDocumentUploadBinding>(),
        DocumentUploadNavigator, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityDocumentUploadBinding
    private lateinit var viewModel: DocumentUploadViewModel

    private lateinit var calendar: Calendar

    override fun getLayoutId(): Int {
        return R.layout.activity_document_upload
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityDocumentUploadBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            DocumentUploadViewModel()
        }
        viewModel.navigator = this
        binding.documentUploadViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text = intent.getStringExtra(Constant.DOCUMENT_NAME)

        setDocumentData(intent.extras!!)

        calendar = Calendar.getInstance(Locale.getDefault())
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        viewModel.expiryDate.value = simpleDateFormat.format(calendar.time)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    private fun setDocumentData(bundle: Bundle) {
        val documentData = DocumentUploadDataModel()
        documentData.documentId = bundle.getInt(Constant.DOCUMENT_ID)
        val stringBuilder = StringBuilder(bundle.getString(Constant.DOCUMENT_NAME))
                .append(" ")
        documentData.frontPageTitle = StringBuilder(stringBuilder)
                .append(resources.getString(R.string.front)).toString()
        val isRequiredBackPage = bundle.getString(Constant.IS_BACK_PAGE_REQUIRED)
        if (isRequiredBackPage == "1") {
            documentData.isBackPageRequired = true
            documentData.backPageTitle = StringBuilder(stringBuilder)
                    .append(resources.getString(R.string.back)).toString()
        } else {
            documentData.isBackPageRequired = false
        }
        documentData.frontPageUrl = bundle.getString(Constant.DOCUMENT_FRONT_PAGE_URL)
        documentData.backPageUrl = bundle.getString(Constant.DOCUMENT_BACK_PAGE_URL)
        documentData.isExpiryDateRequired = bundle.getInt(Constant.IS_EXPIRY_DATE_REQUIRED)
        viewModel.setDocumentData(documentData)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.expiryDate.value = DateTimeUtil().constructDateString(year, month, dayOfMonth,
                "/")
    }

    override fun onDateChanged() {
        val datePickerDialog = DatePickerDialog(this, this, calendar.time.year,
                calendar.time.month, calendar.time.date)
        datePickerDialog.datePicker.minDate = calendar.timeInMillis
        datePickerDialog.show()
    }
}
