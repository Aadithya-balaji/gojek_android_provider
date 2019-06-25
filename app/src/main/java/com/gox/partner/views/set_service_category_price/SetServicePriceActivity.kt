package com.gox.partner.views.set_service_category_price

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants.FareType.DISTANCE_TIME
import com.gox.base.data.Constants.FareType.FIXED
import com.gox.base.data.Constants.FareType.HOURLY
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySetServiceCategoryPriceBinding
import com.gox.partner.models.ServiceCategoriesResponse
import com.gox.partner.models.SubServiceCategoriesResponse
import com.gox.partner.models.SubServicePriceCategoriesResponse
import com.gox.partner.views.edit_service_price.EditServicePriceDialogFragment
import kotlinx.android.synthetic.main.activity_set_service_category_price.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import java.util.*

class SetServicePriceActivity : BaseActivity<ActivitySetServiceCategoryPriceBinding>(),
        SetServicePriceNavigator, SubServicePriceAdapter.ServiceItemClick {

    private lateinit var mBinding: ActivitySetServiceCategoryPriceBinding
    private lateinit var mViewModel: SetServicePriceViewModel

    private lateinit var service: ServiceCategoriesResponse.ResponseData
    private lateinit var selectedService: SubServicePriceCategoriesResponse.ResponseData
    private lateinit var subServicePriceCategoriesResponse: SubServicePriceCategoriesResponse

    override fun getLayoutId() = R.layout.activity_set_service_category_price

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivitySetServiceCategoryPriceBinding
        mViewModel = provideViewModel {
            SetServicePriceViewModel()
        }
        service = intent.getSerializableExtra("service") as ServiceCategoriesResponse.ResponseData
        val subService = intent.getSerializableExtra("sub_service")
                as SubServiceCategoriesResponse.ResponseData
        setSupportActionBar(mBinding.toolbar.tbApp)
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.setup_your_service)

        mViewModel.navigator = this
        mBinding.servicePriceViewModel = mViewModel
        mViewModel.getSubCategory(service.id.toString(), subService.id)
        loadingObservable.value = true

        service_save_btn.setOnClickListener {
            val selectedService = mutableListOf<SelectedService>()
            subServicePriceCategoriesResponse.responseData.forEach {
                if (it.selected == "1" || it.providerservices.isNotEmpty()) {
                    val newService = SelectedService()
                    newService.id = it.id
                    if (service.price_choose == "provider_price") {
                        if (it.servicescityprice != null || it.providerservices.isNotEmpty()) {
                            newService.fareType = FIXED
                            if (it.servicescityprice != null)
                                newService.fareType = it.servicescityprice.fare_type
                            if (it.servicescityprice != null)
                                when (it.servicescityprice.fare_type) {
                                    FIXED -> when {
                                        it.providerservices.isNotEmpty() -> newService.baseFare = it.providerservices[0].base_fare
                                        it.servicescityprice.base_fare.isNotEmpty() -> newService.baseFare = it.servicescityprice.base_fare
                                        else -> {
                                            ViewUtils.showToast(this,
                                                    getString(R.string.enter_amount_selected_service), false)
                                            return@setOnClickListener
                                        }
                                    }
                                    HOURLY -> when {
                                        it.providerservices.isNotEmpty() -> newService.perMin = it.providerservices[0].per_mins
                                        it.servicescityprice.per_mins.isNotEmpty() -> newService.perMin = it.servicescityprice.per_mins
                                        else -> {
                                            ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                            return@setOnClickListener
                                        }
                                    }
                                    DISTANCE_TIME -> when {
                                        it.providerservices.isNotEmpty() -> {
                                            newService.perMin = it.providerservices[0].per_mins
                                            newService.perMiles = it.providerservices[0].per_miles
                                        }
                                        it.servicescityprice.per_miles.isNotEmpty() -> {
                                            if (it.servicescityprice.per_mins.isNotEmpty()) {
                                                newService.perMin = it.servicescityprice.per_mins
                                            } else {
                                                ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                                return@setOnClickListener
                                            }
                                            newService.perMiles = it.servicescityprice.per_miles
                                        }
                                        else -> {
                                            ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                            return@setOnClickListener
                                        }
                                    }
                                }
                            else {
                                if (it.providerservices[0].base_fare != null)
                                    newService.baseFare = it.providerservices[0].base_fare
                                else {
                                    ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                    return@setOnClickListener
                                }
                            }
                        } else {
                            ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                            return@setOnClickListener
                        }
                    }
                    Collections.addAll(selectedService, newService)
                }
            }
            if (selectedService.isEmpty())
                ViewUtils.showToast(this, getString(R.string.select_service), false)
            else {
                loadingObservable.value = true
                mViewModel.postSelection(service.id.toString(), subService.id, selectedService)
            }
        }

        checkResponse()
        checkErrorResponse()
        checkPrice()
        checkAddServiceResponse()
    }

    private fun checkAddServiceResponse() {
        mViewModel.addServiceResponseModel.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this, it.message, true)
        })
    }

    private fun checkPrice() {
        mViewModel.listPrice.observe(this, Observer {
            if (selectedService.servicescityprice != null) {
            } else selectedService.servicescityprice =
                    SubServicePriceCategoriesResponse.Servicescityprice()

            selectedService.servicescityprice.fare_type = it.fareType
            when (it.fareType) {
                FIXED -> {
                    selectedService.providerservices = mutableListOf()
                    val providerService = SubServicePriceCategoriesResponse.ProviderServices()
                    selectedService.servicescityprice.base_fare = it.baseFare
                    providerService.base_fare = it.baseFare
                    selectedService.providerservices.add(providerService)
                }
                DISTANCE_TIME -> {
                    selectedService.providerservices = mutableListOf()
                    selectedService.servicescityprice.per_mins = it.perMin
                    selectedService.servicescityprice.per_miles = it.perMiles
                    val providerService = SubServicePriceCategoriesResponse.ProviderServices()
                    providerService.per_mins = it.perMin
                    providerService.per_miles = it.perMiles
                    selectedService.providerservices.add(providerService)
                }
                HOURLY -> {
                    val providerService = SubServicePriceCategoriesResponse.ProviderServices()
                    selectedService.providerservices = mutableListOf()
                    selectedService.servicescityprice.per_mins = it.perMin
                    providerService.per_mins = it.perMin
                    selectedService.providerservices.add(providerService)
                }
            }
            mBinding.subServiceRv.adapter!!.notifyDataSetChanged()
        })
    }

    private fun checkErrorResponse() {
        mViewModel.errorResponse.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this, it.toString(), false)
        })
    }

    private fun checkResponse() {
        mViewModel.subServiceCategoriesPriceResponse.observe(this, Observer {
            loadingObservable.value = false
            if (it?.responseData != null && it.responseData.isNotEmpty()) {
                subServicePriceCategoriesResponse = it
                val adapter = SubServicePriceAdapter(this, subServicePriceCategoriesResponse,
                        service.price_choose == "provider_price")
                mBinding.subServiceRv.adapter = adapter
                adapter.serviceItemClick = this
            }
        })
    }

    override fun onItemClick(service: SubServicePriceCategoriesResponse.ResponseData, isPriceEdit: Boolean) {
        selectedService = service
        when {
            isPriceEdit -> {
                when (service.selected == "1" || service.providerservices.isNotEmpty()) {
                    true -> {
                        val selected = SelectedService()
                        if (service.servicescityprice != null &&
                                service.servicescityprice.base_fare != null) {
                            selected.fareType = service.servicescityprice.fare_type
                            selected.baseFare = service.servicescityprice.base_fare
                            selected.perMin = service.servicescityprice.per_mins
                            selected.perMiles = service.servicescityprice.per_miles

                        } else {
                            selected.fareType = FIXED
                            selected.baseFare = ""
                        }
                        if (service.providerservices.isNotEmpty()) {
                            selected.baseFare = service.providerservices[0].base_fare
                            selected.perMin = service.providerservices[0].per_mins
                            selected.perMiles = service.providerservices[0].per_miles
                        }
                        val editServicePriceDialog = EditServicePriceDialogFragment()
                        mViewModel.dialogPrice.value = selected
                        editServicePriceDialog.show(supportFragmentManager, "")
                        editServicePriceDialog.isCancelable = false
                    }
                    else -> ViewUtils.showToast(this, getString(R.string.select_service), false)
                }
            }
            else -> {
                when (service.selected == "1" || service.providerservices.isNotEmpty()) {
                    true -> {
                        service.selected = "0"
                        service.providerservices = mutableListOf()
                    }
                    else -> service.selected = "1"
                }
                mBinding.subServiceRv.adapter!!.notifyDataSetChanged()
            }
        }
    }

    class SelectedService(
            var id: String = "",
            var fareType: String = "",
            var baseFare: String = "",
            var perMin: String = "",
            var perMiles: String = ""
    )

    override fun showError(error: String) {
        loadingObservable.value = false
    }
}