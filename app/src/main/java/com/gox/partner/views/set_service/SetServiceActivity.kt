package com.gox.partner.views.set_service

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.gox.base.base.BaseActivity
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySetServiceBinding
import com.gox.partner.models.ServiceCategoriesResponse
import com.gox.partner.views.set_subservice.SetSubServiceActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class SetServiceActivity : BaseActivity<ActivitySetServiceBinding>(), SetServiceNavigator, ServiceAdapter.ServiceItemClick {

    private lateinit var binding: ActivitySetServiceBinding
    private lateinit var viewModel: SetServiceViewModel

    override fun getLayoutId() = R.layout.activity_set_service

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySetServiceBinding
        viewModel = provideViewModel {
            SetServiceViewModel()
        }
        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.setup_your_service)

        viewModel.navigator = this
        binding.serviceViewModel = viewModel
        viewModel.getCategories()
        checkCategoryResponse()
        checkException()
        loadingObservable.value = true
    }

    private fun checkException() {
        viewModel.errorResponse.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this, it.toString(), false)
        })
    }

    private fun checkCategoryResponse() {
        viewModel.serviceCategoriesResponse.observe(this, Observer {
            loadingObservable.value = false
            if (it?.responseData != null && it.responseData.isNotEmpty()) {
                val adapter = ServiceAdapter(this, it)
                binding.serviceRv.adapter = adapter
                adapter.serviceItemClick = this
            }
        })
    }

    override fun onItemClick(service: ServiceCategoriesResponse.ResponseData) {
        val intent = Intent(this, SetSubServiceActivity::class.java)
        intent.putExtra("service", service)
        startActivity(intent)
    }
}
