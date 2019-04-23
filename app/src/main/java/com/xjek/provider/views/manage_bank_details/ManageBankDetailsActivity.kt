package com.xjek.provider.views.manage_bank_details

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityManageBankDetailsBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class ManageBankDetailsActivity : BaseActivity<ActivityManageBankDetailsBinding>(),ManageBankDetailsNavigator {

    private lateinit var mBinding:ActivityManageBankDetailsBinding
    private lateinit var viewModel:ManageBankDetailsViewModel

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
        mBinding.executePendingBindings()
    }


    override fun validateDetails(): Boolean {
        return true
    }

    override fun addBankDetails() {

    }
}
