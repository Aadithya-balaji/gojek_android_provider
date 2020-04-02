package com.gox.partner.views.manage_documents

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.Utils
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityManageDocumentsBinding
import com.gox.partner.models.ListDocumentResponse
import com.gox.partner.views.add_edit_document.AddEditDocumentActivity
import kotlinx.android.synthetic.main.activity_manage_documents.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class ManageDocumentsActivity : BaseActivity<ActivityManageDocumentsBinding>() {

    private lateinit var mBinding: ActivityManageDocumentsBinding
    private lateinit var mViewModel: ManageDocumentsViewModel
    private lateinit var mDocsAdapter: DocumentChooseAdapter

    private var groupIndex: Int = -1
    private var childIndex: Int = -1
    override fun getLayoutId() = R.layout.activity_manage_documents

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityManageDocumentsBinding
        mBinding.lifecycleOwner = this
        mViewModel = provideViewModel {
            ManageDocumentsViewModel()
        }
        mBinding.manageDocumentsViewModel = mViewModel
        mDocsAdapter = DocumentChooseAdapter()
        expandableList.setAdapter(mDocsAdapter)

        setSupportActionBar(mBinding.toolbar.tbApp)
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_manage_documents)
        loadingObservable.value = true

        expandableList.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            groupIndex = groupPosition
            childIndex = childPosition
            val intent = Intent(this, AddEditDocumentActivity::class.java)
            intent.putExtra(Constants.DOCUMENT_NAME,
                    Utils.capitalize(
                            mViewModel.servicesLiveData.value!!.responseData[groupPosition].adminServiceName
                    ).plus(getString(R.string.documents))
            )
            intent.putExtra(Constants.DOCUMENT_TYPE,mViewModel.servicesLiveData.value!!.responseData[groupPosition].documents[childIndex])
            startActivityForResult(intent, Constants.DOCUMENT_UPLOAD_REQUEST_CODE)
            false
        }

        mViewModel.getServices()

        observeLiveData(mViewModel.servicesLiveData) { response ->
            loadingObservable.value = false
            mDocsAdapter.setData(response.responseData)
        }

        mViewModel.errorLiveData.observe(this, Observer {
            ViewUtils.showToast(applicationContext, it, false)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && Constants.DOCUMENT_UPLOAD_REQUEST_CODE == requestCode) {
            mDocsAdapter.setItem(
                    groupIndex,
                    childIndex,
                    data?.extras!!.get(Constants.DOCUMENT_TYPE) as ListDocumentResponse.ResponseData.ProviderDocument
            )
        }
    }
}