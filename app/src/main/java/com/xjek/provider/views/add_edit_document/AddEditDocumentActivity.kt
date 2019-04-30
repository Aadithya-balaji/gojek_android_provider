package com.xjek.provider.views.add_edit_document

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.DateTimeUtil
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityAddEditDocumentBinding
import com.xjek.provider.utils.Constant
import kotlinx.android.synthetic.main.layout_app_bar.view.*
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
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        viewModelAddEdit.expiryDate.value = simpleDateFormat.format(calendar.time)

        observeLiveData(viewModelAddEdit.showLoading){
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

        observeLiveData(viewModelAddEdit.errorResponse) { error ->
            run {
                ViewUtils.showToast(this,error,false)
                viewModelAddEdit.showEmpty.value = true
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

    }

    override fun selectBackImage() {

    }

    override fun submitDocument() {
        ViewUtils.showToast(this,"Something went wrong",false)
    }

}
