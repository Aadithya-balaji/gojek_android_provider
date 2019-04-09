package com.xjek.provider.views.document_upload

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.DateTimeUtil
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityDocumentUploadBinding
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

        calendar = Calendar.getInstance(Locale.getDefault())
        viewModel.expiryDate.value = DateTimeUtil().constructDateString(calendar.time, "/")
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
