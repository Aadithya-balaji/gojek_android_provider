package com.gox.partner.views.setup_vehicle

import android.content.Intent
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySetupVehicleBinding
import com.gox.partner.models.SetupDeliveryResponseModel
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
            mViewModel.getDeliveryId() -> {
                mViewModel.getDelivery()
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
        else if (providerService is SetupDeliveryResponseModel)
            intent.putExtra(Constants.CATEGORY_ID, providerService.responseData[position].id)


        if (providerService is SetupRideResponseModel) {
            if (providerService.responseData[position].providerService != null) {
                intent.putExtra(Constants.SERVICE_STATUS, providerService.responseData[position].providerService?.status)
            }
        }else if (providerService is SetupShopResponseModel) {
            if (providerService.responseData[position].providerService != null) {
                intent.putExtra(Constants.SERVICE_STATUS, providerService.responseData[position].providerService?.status)
            }
        }else if (providerService is SetupDeliveryResponseModel) {
            if (providerService.responseData[position].providerService != null) {
                intent.putExtra(Constants.SERVICE_STATUS, providerService.responseData[position].providerService?.status)
            }
        }

        if (providerService is SetupRideResponseModel
                && providerService.responseData[position].serviceList.isNotEmpty())
            intent.putExtra(Constants.TRANSPORT_VEHICLES,
                    ArrayList(providerService.responseData[position].serviceList))
        if (providerService is SetupDeliveryResponseModel
                && providerService.responseData[position].serviceList.isNotEmpty()){
            System.out.println("vechiadata--> "+ Gson().toJson(providerService.responseData[position].serviceList))
            intent.putExtra(Constants.DELIVERY_VEHICLES,
                    ArrayList(providerService.responseData[position].serviceList))}


        if (providerService is SetupRideResponseModel
                && providerService.responseData[position].providerService != null) {
            //Need to compare ride delievery id
            val vehicleData = providerService.responseData[position].providerService!!.providerVehicle
            vehicleData.vehicleServiceId = providerService.responseData[position].providerService?.rideDeliveryId ?:0
            intent.putExtra(Constants.PROVIDER_TRANSPORT_VEHICLE, vehicleData)
        } else if (providerService is SetupShopResponseModel
                && providerService.responseData[position].providerService != null) {
            intent.putExtra(Constants.PROVIDER_ORDER_VEHICLE,
                    providerService.responseData[position].providerService!!.providerVehicle)
        }else if (providerService is SetupDeliveryResponseModel
                && providerService.responseData[position].providerService != null) {
            val vehicleData = providerService.responseData[position].providerService!!.providerVehicle
            vehicleData.vehicleServiceId = providerService.responseData[position].providerService?.deliveryVehicleId ?:0
            intent.putExtra(Constants.PROVIDER_DELIVERY_VEHICLE, vehicleData)
        }
        openActivity(intent, false)
    }

    override fun switchOnCliked(position: Int,status:Boolean) {
        loadingObservable.value = true
        val providerService = mViewModel.getVehicleDataObservable().value
        val UpadteServiceMap:HashMap<String,String>  = HashMap();
        UpadteServiceMap.put("admin_service",mViewModel.getServiceName())
        if(status){
            UpadteServiceMap.put("status","1");
        }else{
            UpadteServiceMap.put("status","0");
        }

        if (providerService is SetupRideResponseModel) {
            if (providerService.responseData[position].providerService?.providerVehicle != null) {
                mViewModel.updateService(status, providerService.responseData[position].providerService!!.providerVehicle.id, UpadteServiceMap,position);
            }else{
                loadingObservable.value = false
                Toast.makeText(this,"No vechicle details found",Toast.LENGTH_LONG).show()
                setupVehicle()
            }
        }else if (providerService is SetupShopResponseModel) {
            if (providerService.responseData[position].providerService?.providerVehicle != null) {
                mViewModel.updateService(status, providerService.responseData[position].providerService!!.providerVehicle.id, UpadteServiceMap,position);
            }else{
                loadingObservable.value = false
                Toast.makeText(this,"No vechicle details found",Toast.LENGTH_LONG).show()
                setupVehicle()
            }
        } else if (providerService is SetupDeliveryResponseModel){
            if (providerService.responseData[position].providerService?.providerVehicle != null) {
                mViewModel.updateService(status,providerService.responseData[position].providerService!!.providerVehicle.id,UpadteServiceMap,position);
            }else{
                loadingObservable.value = false
                Toast.makeText(this,"No vechicle details found!",Toast.LENGTH_LONG).show()
                setupVehicle()
            }
        }
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(applicationContext, error, false)
        setupVehicle()
    }

    override fun showSuccess(status:Boolean,postion:Int) {
        if(status){
            if (mViewModel.getVehicleDataObservable().value is SetupRideResponseModel)
                (mViewModel.getVehicleDataObservable().value as SetupRideResponseModel)?.responseData?.get(postion)?.providerService?.status = "ACTIVE"
            else if (mViewModel.getVehicleDataObservable().value is SetupShopResponseModel)
                (mViewModel.getVehicleDataObservable().value as SetupShopResponseModel)?.responseData?.get(postion)?.providerService?.status = "ACTIVE"
            else if (mViewModel.getVehicleDataObservable().value is SetupDeliveryResponseModel)
                (mViewModel.getVehicleDataObservable().value as SetupDeliveryResponseModel)?.responseData?.get(postion)?.providerService?.status = "ACTIVE"
            loadingObservable.value = false
            ViewUtils.showToast(applicationContext, "Service Turn-ON successfully.", true)
        }else{
            if (mViewModel.getVehicleDataObservable().value is SetupRideResponseModel)
                (mViewModel.getVehicleDataObservable().value as SetupRideResponseModel)?.responseData?.get(postion)?.providerService?.status = "INACTIVE"
            else if (mViewModel.getVehicleDataObservable().value is SetupShopResponseModel)
                (mViewModel.getVehicleDataObservable().value as SetupShopResponseModel)?.responseData?.get(postion)?.providerService?.status = "INACTIVE"
            else if (mViewModel.getVehicleDataObservable().value is SetupDeliveryResponseModel)
                (mViewModel.getVehicleDataObservable().value as SetupDeliveryResponseModel)?.responseData?.get(postion)?.providerService?.status = "INACTIVE"
            loadingObservable.value = false
            ViewUtils.showToast(applicationContext, "Service Turn-OFF successfully.", true)
        }
//        onBackPressed()
    }
}