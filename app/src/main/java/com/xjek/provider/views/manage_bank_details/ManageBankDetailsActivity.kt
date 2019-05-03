package com.xjek.provider.views.manage_bank_details

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityManageBankDetailsBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class ManageBankDetailsActivity : BaseActivity<ActivityManageBankDetailsBinding>(), ManageBankDetailsNavigator {

    private lateinit var mBinding: ActivityManageBankDetailsBinding
    private lateinit var viewModel: ManageBankDetailsViewModel


    override fun getLayoutId(): Int = R.layout.activity_manage_bank_details

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityManageBankDetailsBinding
        mBinding.toolbarLayout.title_toolbar.setTitle(R.string.title_bank_details)
        mBinding.toolbarLayout.toolbar_back_img.setOnClickListener {
            finish()
        }

        viewModel = ViewModelProviders.of(this).get(ManageBankDetailsViewModel::class.java)
        viewModel.navigator = this

        mBinding.viewModel = viewModel
        mBinding.lifecycleOwner = this
        mBinding.executePendingBindings()

        observeLiveData(viewModel.showLoading) {
            loadingObservable.value = it
        }

        viewModel.getBankTemplate()

        observeResponses()
    }

    private fun observeResponses() {

        observeLiveData(viewModel.bankResponse) {
            viewModel.showLoading.value = false
            viewModel.setAdapter()
            viewModel.preSetValues()
            viewModel.showEmpty.value = it.responseData.isEmpty()
        }

        observeLiveData(viewModel.addEditBankResponse) {
            viewModel.getBankTemplate()
        }

        observeLiveData(viewModel.errorResponse) { errorMessage ->
            run {
                viewModel.showLoading.value = false
                viewModel.showEmpty.value = true
                ViewUtils.showToast(this, errorMessage, false)
            }
        }

        observeLiveData(viewModel.addEditBankErrorResponse){
            errorMessage ->
            run {
                viewModel.showLoading.value = false
                ViewUtils.showToast(this, errorMessage, false)
            }
        }
    }


    override fun validateDetails(): Boolean {
        val data = viewModel.bankResponse.value!!.responseData
        for (i in 0 until data.size) {
            if (data[i].lableValue.isEmpty()) {
                ViewUtils.showToast(this, "Please enter ${data[i].label.toLowerCase()}", false)
                return false
            } else if (data[i].lableValue.length < data[i].min || data[i].lableValue.length > data[i].max) {
                ViewUtils.showToast(this, "Please enter valid ${data[i].label.toLowerCase()}", false)
                return false
            }
        }
        return true
    }

}
