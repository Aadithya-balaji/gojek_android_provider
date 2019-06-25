package com.gox.partner.views.manage_documents

import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityManageDocumentsBinding
import com.gox.partner.views.add_edit_document.AddEditDocumentActivity
import kotlinx.android.synthetic.main.activity_manage_documents.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class ManageDocumentsActivity : BaseActivity<ActivityManageDocumentsBinding>(), ManageDocumentsNavigator {

    private lateinit var mBinding: ActivityManageDocumentsBinding
    private lateinit var mViewModel: ManageDocumentsViewModel

    private object DocumentType {
        const val ALL = "All"
        const val TRANSPORT = "Transport"
        const val DELIVERY = "Order"
        const val SERVICES = "Service"
    }

    override fun getLayoutId() = R.layout.activity_manage_documents

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityManageDocumentsBinding
        mBinding.lifecycleOwner = this
        mViewModel = provideViewModel {
            ManageDocumentsViewModel()
        }
        mViewModel.navigator = this
        mBinding.manageDocumentsViewModel = mViewModel

        setSupportActionBar(mBinding.toolbar.tbApp)
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_manage_documents)
        loadingObservable.value = true
        mViewModel.getServices()
        observeLiveData(mViewModel.servicesLiveData) { response ->
            loadingObservable.value = false
            for (data in response.responseData)
                when (data.adminServiceName) {
                    Constants.ModuleTypes.TRANSPORT -> if (data.providerServices != null)
                        cvTransportDocuments.visibility = View.VISIBLE
                    Constants.ModuleTypes.SERVICE -> if (data.providerServices != null)
                        cvServicesDocuments.visibility = View.VISIBLE
                    Constants.ModuleTypes.ORDER -> if (data.providerServices != null)
                        cvDeliveryDocuments.visibility = View.VISIBLE
                }
        }
    }

    override fun showError(error: String) = ViewUtils.showToast(applicationContext, error, false)

    override fun showAllDocuments() {
        val intent = Intent(this, AddEditDocumentActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_NAME, getString(R.string.common_documents))
        intent.putExtra(Constants.DOCUMENT_TYPE, DocumentType.ALL)
        openActivity(intent, false)
    }

    override fun showTransportDocuments() {
        val intent = Intent(this, AddEditDocumentActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_NAME, getString(R.string.transport_documents))
        intent.putExtra(Constants.DOCUMENT_TYPE, DocumentType.TRANSPORT)
        openActivity(intent, false)
    }

    override fun showDeliveryDocuments() {
        val intent = Intent(this, AddEditDocumentActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_NAME, getString(R.string.delievery_documents))
        intent.putExtra(Constants.DOCUMENT_TYPE, DocumentType.DELIVERY)
        openActivity(intent, false)
    }

    override fun showServicesDocuments() {
        val intent = Intent(this, AddEditDocumentActivity::class.java)
        intent.putExtra(Constants.DOCUMENT_NAME, getString(R.string.services_documents))
        intent.putExtra(Constants.DOCUMENT_TYPE, DocumentType.SERVICES)
        openActivity(intent, false)
    }
}