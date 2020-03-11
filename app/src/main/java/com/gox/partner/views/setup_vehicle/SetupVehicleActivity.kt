package com.gox.partner.views.setup_vehicle

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySetupVehicleBinding
import com.gox.partner.models.SetupRideResponseModel
import com.gox.partner.models.SetupShopResponseModel
import com.gox.partner.views.add_vehicle.AddVehicleActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class SetupVehicleActivity : BaseActivity<ActivitySetupVehicleBinding>(), SetupVehicleNavigator {

    private lateinit var mBinding: ActivitySetupVehicleBinding
    private lateinit var mViewModel: SetupVehicleViewModel

    override fun getLayoutId() = R.layout.activity_setup_vehicle

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivitySetupVehicleBinding
        mBinding.lifecycleOwner = this
        mViewModel = provideViewModel {
            SetupVehicleViewModel()
        }
        mViewModel.navigator = this
        mViewModel.setServiceId(intent.getStringExtra(Constants.SERVICE_ID))
        mBinding.setupVehicleViewModel = mViewModel
        setSupportActionBar(mBinding.toolbar.tbApp)
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_setup_vehicle)
        observeViewModel()

    }

    private fun observeViewModel() {
        observeLiveData(mViewModel.getVehicleDataObservable()) {
            loadingObservable.value = false
            mViewModel.setAdapter()
        }
    }

    override fun onResume() {
        super.onResume()
        setupVehicle()
    }

    private fun setupVehicle() {
        loadingObservable.value = true
        when (mViewModel.getServiceName()) {
            mViewModel.getTransportId() -> {
                mViewModel.getRides()
            }
            mViewModel.getOrderId() -> {
                mViewModel.getShops()
            }
            else -> loadingObservable.value = false
        }
    }

    override fun onMenuItemClicked(position: Int) {
        val providerService = mViewModel.getVehicleDataObservable().value

        val intent = Intent(applicationContext, AddVehicleActivity::class.java)
        intent.putExtra(Constants.SERVICE_ID, mViewModel.getServiceName())
        if (providerService is SetupRideResponseModel)
            intent.putExtra(Constants.CATEGORY_ID, providerService.responseData[position].id)
        else if (providerService is SetupShopResponseModel)
            intent.putExtra(Constants.CATEGORY_ID, providerService.responseData[position].id)
        if (providerService is SetupRideResponseModel
                && providerService.responseData[position].serviceList.isNotEmpty())
            intent.putExtra(Constants.TRANSPORT_VEHICLES,
                ArrayList(providerService.responseData[position].serviceList))

        if (providerService is SetupRideResponseModel
                && providerService.responseData[position].providerService != null) {
            //Need to compare ride delievery id
            val vehicleData = providerService.responseData[position].providerService!!.providerVehicle
            vehicleData.vehicleServiceId = providerService.responseData[position].providerService?.rideDeliveryId ?:0
            intent.putExtra(Constants.PROVIDER_TRANSPORT_VEHICLE,
                    vehicleData)
        } else if (providerService is SetupShopResponseModel
                && providerService.responseData[position].providerService != null) {
            intent.putExtra(Constants.PROVIDER_ORDER_VEHICLE,
                    providerService.responseData[position].providerService!!.providerVehicle)
        }
        openActivity(intent, false)
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}