package com.xjek.provider.views.manage_documents

import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityManageDocumentsBinding
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.document_upload.DocumentUploadActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class ManageDocumentsActivity : BaseActivity<ActivityManageDocumentsBinding>(),
        ManageDocumentsNavigator {

    private lateinit var binding: ActivityManageDocumentsBinding
    private lateinit var viewModel: ManageDocumentsViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_manage_documents
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityManageDocumentsBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            ManageDocumentsViewModel()
        }
        viewModel.navigator = this
        binding.manageDocumentsViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_manage_documents)

        observeViewModel()

        loadingObservable.value = true
        viewModel.getDocumentTypes()
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getDocumentTypeObservable()) {
            loadingObservable.value = false
            viewModel.setAdapter()
        }
    }

    override fun onMenuItemClicked(position: Int) {
        val intent = Intent(applicationContext, DocumentUploadActivity::class.java)
        val bundle = Bundle()
        bundle.putInt(Constant.DOCUMENT_ID,
                viewModel.getDocumentTypeObservable().value!!.responseData[position].id)
        bundle.putString(Constant.DOCUMENT_NAME,
                viewModel.getDocumentTypeObservable().value!!.responseData[position].name)
        bundle.putString(Constant.IS_BACK_PAGE_REQUIRED,
                viewModel.getDocumentTypeObservable().value!!.responseData[position].isBackside)
        if (viewModel.getDocumentTypeObservable().value!!.responseData[position].providerDocument
                != null) {
            bundle.putString(Constant.DOCUMENT_FRONT_PAGE_URL,
                    viewModel.getDocumentTypeObservable().value!!.responseData[position]
                            .providerDocument!!.url[0].url)
            if (viewModel.getDocumentTypeObservable().value!!.responseData[position]
                            .providerDocument!!.url.size > 1)
                bundle.putString(Constant.DOCUMENT_BACK_PAGE_URL,
                        viewModel.getDocumentTypeObservable().value!!.responseData[position]
                                .providerDocument!!.url[1].url)
        }
        bundle.putString(Constant.IS_BACK_PAGE_REQUIRED,
                viewModel.getDocumentTypeObservable().value!!.responseData[position].isBackside)
        bundle.putInt(Constant.IS_EXPIRY_DATE_REQUIRED,
                viewModel.getDocumentTypeObservable().value!!.responseData[position].isExpire)
        intent.putExtras(bundle)
        launchNewActivity(intent, false)
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}
