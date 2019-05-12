package com.xjek.provider.views.set_subservice

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySetSubServiceBinding
import com.xjek.provider.models.ServiceCategoriesResponse
import com.xjek.provider.models.SubServiceCategoriesResponse
import com.xjek.provider.views.set_service_category_price.SetServicePriceActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class SetSubServiceActivity : BaseActivity<ActivitySetSubServiceBinding>(), SetSubServiceNavigator, SubServiceAdapter.ServiceItemClick {

    private lateinit var binding: ActivitySetSubServiceBinding
    private lateinit var viewModel: SetSubServiceViewModel
    private lateinit var service: ServiceCategoriesResponse.ResponseData

    override fun getLayoutId() = R.layout.activity_set_sub_service

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySetSubServiceBinding
        viewModel = provideViewModel {
            SetSubServiceViewModel()
        }
        setSupportActionBar(binding.toolbar.tbApp)
        service = intent.getSerializableExtra("service") as ServiceCategoriesResponse.ResponseData
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text = (service.service_category_name)
        viewModel.navigator = this
        binding.subServiceViewModel = viewModel
        viewModel.getSubCategories(service.id.toString())
        loadingObservable.value = true
        checkResponse()
        checkErrorResponse()
    }

    private fun checkErrorResponse() {
        viewModel.errorResponse.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this, it.toString(), false)
        })
    }

    private fun checkResponse() {
        viewModel.subServiceCategoriesResponse.observe(this, Observer {
            loadingObservable.value = false
            if (it?.responseData != null && it.responseData.isNotEmpty()) {
                val adapter = SubServiceAdapter(this, it)
                binding.subServiceRv.adapter = adapter
                adapter.serviceItemClick = this
            }
        })
    }

    override fun onItemClick(service: SubServiceCategoriesResponse.ResponseData) {
        val intent = Intent(this, SetServicePriceActivity::class.java)
        intent.putExtra("service", this.service)
        intent.putExtra("sub_service", service)
        startActivity(intent)
    }
}
