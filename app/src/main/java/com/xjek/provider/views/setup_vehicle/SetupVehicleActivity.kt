package com.xjek.provider.views.setup_vehicle

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySetupVehicleBinding
import com.xjek.provider.models.SetupRideResponseModel
import com.xjek.provider.models.SetupShopResponseModel
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.add_vehicle.AddVehicleActivity
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class SetupVehicleActivity : BaseActivity<ActivitySetupVehicleBinding>(), SetupVehicleNavigator {

    private lateinit var binding: ActivitySetupVehicleBinding
    private lateinit var viewModel: SetupVehicleViewModel

    override fun getLayoutId(): Int {
        return R.layout.activity_setup_vehicle
    }

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
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.title_setup_vehicle)

        observeViewModel()

        setupVehicle()
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getVehicleDataObservable()) {
            loadingObservable.value = false
            viewModel.setAdapter()
        }
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
        }
    }

    override fun onMenuItemClicked(position: Int) {
        val intent = Intent(applicationContext, AddVehicleActivity::class.java)
        intent.putExtra(Constant.SERVICE_ID, viewModel.getServiceId())
        when (viewModel.getServiceId()) {
            viewModel.getTransportId() -> {
                if (!(viewModel.getVehicleDataObservable().value as SetupRideResponseModel)
                                .responseData[position].providerService.isNullOrEmpty()) {
                    intent.putExtra(Constant.PROVIDER_VEHICLE,
                            (viewModel.getVehicleDataObservable().value as SetupRideResponseModel)
                                    .responseData[position].providerService[0].providerVehicle)
                }
            }
            viewModel.getOrderId() -> {
                if (!(viewModel.getVehicleDataObservable().value as SetupShopResponseModel)
                                .responseData[position].providerService.isNullOrEmpty())
                    intent.putExtra(Constant.PROVIDER_VEHICLE,
                            (viewModel.getVehicleDataObservable().value as SetupShopResponseModel)
                                    .responseData[position].providerService[0].providerVehicle)
            }
        }
        launchNewActivity(intent, false)
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
    }
}
