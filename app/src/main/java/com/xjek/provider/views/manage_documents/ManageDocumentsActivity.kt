package com.xjek.provider.views.manage_documents

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityManageDocumentsBinding
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

        viewModel.setAdapter()
    }

    override fun onMenuItemClicked(position: Int) {
        val intent: Intent
        intent = Intent(applicationContext, DocumentUploadActivity::class.java)
        startActivity(intent)
    }
}
