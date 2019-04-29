package com.xjek.provider.views.add_edit_document

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.DateTimeUtil
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityDocumentUploadBinding
import com.xjek.provider.utils.Constant
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import java.text.SimpleDateFormat
import java.util.*


class AddEditDocumentActivity : BaseActivity<ActivityDocumentUploadBinding>(),
        DocumentUploadNavigator, DatePickerDialog.OnDateSetListener {

    private lateinit var binding: ActivityDocumentUploadBinding
    private lateinit var viewModelAddEdit: AddEditDocumentViewModel

    private lateinit var calendar: Calendar

    override fun getLayoutId(): Int {
        return R.layout.activity_document_upload
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityDocumentUploadBinding
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
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        viewModelAddEdit.expiryDate.value = simpleDateFormat.format(calendar.time)

        viewModelAddEdit.getDocumentList(intent.getStringExtra(Constant.DOCUMENT_TYPE))
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
}
