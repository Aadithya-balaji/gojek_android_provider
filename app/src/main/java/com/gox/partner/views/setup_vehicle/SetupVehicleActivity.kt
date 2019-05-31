package com.gox.partner.views.setup_vehicle

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySetupVehicleBinding
import com.gox.partner.models.SetupRideResponseModel
import com.gox.partner.models.SetupShopResponseModel
import com.gox.partner.utils.Constant
import com.gox.partner.views.add_vehicle.AddVehicleActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class SetupVehicleActivity : BaseActivity<ActivitySetupVehicleBinding>(), SetupVehicleNavigator {

    private lateinit var binding: ActivitySetupVehicleBinding
    private lateinit var viewModel: SetupVehicleViewModel

    override fun getLayoutId() = R.layout.activity_setup_vehicle

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySetupVehicleBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            SetupVehicleViewModel()
        }
        viewModel.navigator = this
        viewModel.setServiceId(intent.getIntExtra(Constant.SERVICE_ID, -1))
        binding.setupVehicleViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_setup_vehicle)

        observeViewModel()

    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getVehicleDataObservable()) {
            loadingObservable.value = false
            viewModel.setAdapter()
        }
    }

    override fun onResume() {
        super.onResume()
        setupVehicle()
    }

    private fun setupVehicle() {
        loadingObservable.value = true
        when (viewModel.getServiceId()) {
            viewModel.getTransportId() -> {
                viewModel.getRides()
            }
            viewModel.getOrderId() -> {
                viewModel.getShops()
            }
            else -> loadingObservable.value = false
        }
    }

    override fun onMenuItemClicked(position: Int) {
        val providerService = viewModel.getVehicleDataObservable().value

        val intent = Intent(applicationContext, AddVehicleActivity::class.java)
        intent.putExtra(Constant.SERVICE_ID, viewModel.getServiceId())

        if (providerService is SetupRideResponseModel)
            intent.putExtra(Constant.CATEGORY_ID, providerService.responseData[position].id)
        else if (providerService is SetupShopResponseModel)
            intent.putExtra(Constant.CATEGORY_ID, providerService.responseData[position].id)

        if (providerService is SetupRideResponseModel && providerService.responseData[position].serviceList.isNotEmpty()) {
            intent.putExtra(Constant.TRANSPORT_VEHICLES,
                    ArrayList(providerService.responseData[position].serviceList))
        }

        if (providerService is SetupRideResponseModel && providerService.responseData[position].providerService != null) {
            intent.putExtra(Constant.PROVIDER_TRANSPORT_VEHICLE,
                    providerService.responseData[position].providerService!!.providerVehicle)
        } else if (providerService is SetupShopResponseModel && providerService.responseData[position].providerService != null) {
            intent.putExtra(Constant.PROVIDER_ORDER_VEHICLE,
                    providerService.responseData[position].providerService!!.providerVehicle)
        }
        launchNewActivity(intent, false)
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}