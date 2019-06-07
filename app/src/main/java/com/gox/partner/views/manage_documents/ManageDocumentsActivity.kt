package com.gox.partner.views.manage_documents

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityManageDocumentsBinding
import com.gox.partner.views.add_edit_document.AddEditDocumentActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class ManageDocumentsActivity : BaseActivity<ActivityManageDocumentsBinding>(), ManageDocumentsNavigator {

    private lateinit var binding: ActivityManageDocumentsBinding
    private lateinit var viewModel: ManageDocumentsViewModel

    private object DocumentType {
        const val ALL = "All"
        const val TRANSPORT = "Transport"
        const val DELIVERY = "Order"
        const val SERVICES = "Service"
    }

    override fun getLayoutId() = R.layout.activity_manage_documents

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
        binding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_manage_documents)

        observeViewModel()

    }

    private fun observeViewModel() {

    }

    override fun showError(error: String) {
        ViewUtils.showToast(applicationContext, error, false)
    }

    override fun showAllDocuments() {
        val intent = Intent(this, AddEditDocumentActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_NAME, getString(R.string.common_documents))
        intent.putExtra(Constants.DOCUMENT_TYPE, DocumentType.ALL)
        launchNewActivity(intent, false)
    }

    override fun showTransportDocuments() {
        val intent = Intent(this, AddEditDocumentActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_NAME, getString(R.string.transport_documents))
        intent.putExtra(Constants.DOCUMENT_TYPE, DocumentType.TRANSPORT)
        launchNewActivity(intent, false)
    }

    override fun showDelieveryDocuments() {
        val intent = Intent(this, AddEditDocumentActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_NAME, getString(R.string.delievery_documents))
        intent.putExtra(Constants.DOCUMENT_TYPE, DocumentType.DELIVERY)
        launchNewActivity(intent, false)
    }

    override fun showServicesDocuments() {
        val intent = Intent(this, AddEditDocumentActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_NAME, getString(R.string.services_documents))
        intent.putExtra(Constants.DOCUMENT_TYPE, DocumentType.SERVICES)
        launchNewActivity(intent, false)
    }
}