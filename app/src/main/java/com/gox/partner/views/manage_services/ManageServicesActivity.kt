package com.gox.partner.views.manage_services

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityManageServicesBinding
import com.gox.partner.models.ManageServicesDataModel
import com.gox.partner.views.set_service.SetServiceActivity
import com.gox.partner.views.setup_vehicle.SetupVehicleActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class ManageServicesActivity : BaseActivity<ActivityManageServicesBinding>(), ManageServicesNavigator {

    private lateinit var mBinding: ActivityManageServicesBinding
    private lateinit var mViewModel: ManageServicesViewModel
    private lateinit var serviceData: List<ManageServicesDataModel>

    override fun getLayoutId() = R.layout.activity_manage_services

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityManageServicesBinding
        mBinding.lifecycleOwner = this
        mViewModel = provideViewModel {
            ManageServicesViewModel()
        }
        mViewModel.navigator = this
        mBinding.manageServicesViewModel = mViewModel

        setSupportActionBar(mBinding.toolbar.tbApp)
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_manage_services)

        val colors = resources.getIntArray(R.array.color_manage_services)
        val icons = resources.obtainTypedArray(R.array.icon_manage_services)
        val titles = resources.getStringArray(R.array.title_manage_services)
        val descriptions = resources.getStringArray(R.array.desc_manage_services)
        serviceData = List(titles.size) {
            ManageServicesDataModel(colors[it], icons.getResourceId(it, -1), titles[it],
                    descriptions[it])
        }
        icons.recycle()
        observeViewModel()
        loadingObservable.value = true
        mViewModel.getServices()
    }

    private fun observeViewModel() {
        observeLiveData(mViewModel.getServicesObservable()) { response ->
            run {
                loadingObservable.value = false
                mViewModel.setServiceData(serviceData)
                mViewModel.setAdapter()
            }
        }
    }

    override fun onMenuItemClicked(position: Int) {
        when (position) {
            0 -> intent = Intent(applicationContext, SetupVehicleActivity::class.java)
            1 -> intent = Intent(applicationContext, SetupVehicleActivity::class.java)
            2 -> intent = Intent(applicationContext, SetServiceActivity::class.java)
        }

        val response = mViewModel.getServicesObservable().value!!.responseData
        if (!response.isNullOrEmpty() && response.size > position) {
            intent.putExtra(Constants.SERVICE_ID,
                    mViewModel.getServicesObservable().value!!.responseData[position].adminServiceName)
            openActivity(intent, false)
        } else ViewUtils.showToast(this, "Service not configured. Please contact admin", false)
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}
