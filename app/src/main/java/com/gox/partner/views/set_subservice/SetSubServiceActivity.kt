package com.gox.partner.views.set_subservice

import android.content.Intent
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.gox.base.base.BaseActivity
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySetSubServiceBinding
import com.gox.partner.models.ServiceCategoriesResponse
import com.gox.partner.models.SubServiceCategoriesResponse
import com.gox.partner.views.set_service_category_price.SetServicePriceActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class SetSubServiceActivity : BaseActivity<ActivitySetSubServiceBinding>(),
        SetSubServiceNavigator, SubServiceAdapter.ServiceItemClick {

    private lateinit var mBinding: ActivitySetSubServiceBinding
    private lateinit var mViewModel: SetSubServiceViewModel

    private lateinit var service: ServiceCategoriesResponse.ResponseData

    override fun getLayoutId() = R.layout.activity_set_sub_service

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivitySetSubServiceBinding
        mViewModel = provideViewModel {
            SetSubServiceViewModel()
        }
        setSupportActionBar(mBinding.toolbar.tbApp)
        service = intent.getSerializableExtra("service") as ServiceCategoriesResponse.ResponseData
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text = (service.service_category_name)
        mViewModel.navigator = this
        mBinding.subServiceViewModel = mViewModel

        checkResponse()
        checkErrorResponse()
    }

    override fun onResume() {
        super.onResume()
        loadingObservable.value = true
        mViewModel.getSubCategories(service.id.toString())
    }

    private fun checkErrorResponse() {
        mViewModel.errorResponse.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this, it.toString(), false)
        })
    }

    private fun checkResponse() {
        mViewModel.subServiceCategoriesResponse.observe(this, Observer {
            loadingObservable.value = false
            if (it?.responseData != null && it.responseData.isNotEmpty()) {
                val adapter = SubServiceAdapter(this, it)
                mBinding.subServiceRv.adapter = adapter
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
