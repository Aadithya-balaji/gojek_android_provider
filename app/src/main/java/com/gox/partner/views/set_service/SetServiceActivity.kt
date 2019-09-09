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

class SetServiceActivity : BaseActivity<ActivitySetServiceBinding>(),
        SetServiceNavigator, ServiceAdapter.ServiceItemClick {

    private lateinit var mBinding: ActivitySetServiceBinding
    private lateinit var mViewModel: SetServiceViewModel

    override fun getLayoutId() = R.layout.activity_set_service

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivitySetServiceBinding
        mViewModel = provideViewModel {
            SetServiceViewModel()
        }
        setSupportActionBar(mBinding.toolbar.tbApp)
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.setup_your_service)

        mViewModel.navigator = this
        mBinding.serviceViewModel = mViewModel
        checkCategoryResponse()
        checkException()
    }

    override fun onResume() {
        super.onResume()
        loadingObservable.value = true
        mViewModel.getCategories()
    }

    private fun checkException() {
        mViewModel.errorResponse.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this, it.toString(), false)
        })
    }

    private fun checkCategoryResponse() {
        mViewModel.serviceCategoriesResponse.observe(this, Observer {
            loadingObservable.value = false
            if (it?.responseData != null && it.responseData.isNotEmpty()) {
                val adapter = ServiceAdapter(this, it)
                mBinding.serviceRv.adapter = adapter
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
