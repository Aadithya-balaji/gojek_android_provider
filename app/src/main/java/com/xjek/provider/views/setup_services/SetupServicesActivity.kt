package com.xjek.provider.views.setup_services

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R

class SetupServicesActivity : BaseActivity<ActivitySetupServicesBinding>(), SetupServicesNavigator {

    private lateinit var binding: ActivitySetupServicesBinding
    private lateinit var viewModel: SetupServicesViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_setup_services
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySetupServicesBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            SetupServicesViewModel()
        }
        viewModel.navigator = this
        binding.setupServicesViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_setup_services)

        viewModel.getCategories()

        checkAPIResponse()
    }

    private fun checkAPIResponse() {
        observeLiveData(viewModel.getServicesDataObservable()) {

        }
    }

    override fun onMenuItemClicked(position: Int) {

    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}
