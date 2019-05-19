package com.xjek.provider.views.set_service_category_price

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySetServiceCategoryPriceBinding
import com.xjek.provider.models.ServiceCategoriesResponse
import com.xjek.provider.models.SubServiceCategoriesResponse
import com.xjek.provider.models.SubServicePriceCategoriesResponse
import com.xjek.provider.views.edit_service_price.EditServicePriceDialogFragment
import kotlinx.android.synthetic.main.activity_set_service_category_price.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import java.util.*

class SetServicePriceActivity : BaseActivity<ActivitySetServiceCategoryPriceBinding>(), SetServicePriceNavigator, SubServicePriceAdapter.ServiceItemClick {

    private lateinit var binding: ActivitySetServiceCategoryPriceBinding
    private lateinit var viewModel: SetServicePriceViewModel
    private lateinit var service: ServiceCategoriesResponse.ResponseData
    private lateinit var selectedService: SubServicePriceCategoriesResponse.ResponseData
    private lateinit var subServicePriceCategoriesResponse: SubServicePriceCategoriesResponse

    override fun getLayoutId() = R.layout.activity_set_service_category_price

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySetServiceCategoryPriceBinding
        viewModel = provideViewModel {
            SetServicePriceViewModel()
        }
        service = intent.getSerializableExtra("service") as ServiceCategoriesResponse.ResponseData
        val subService = intent.getSerializableExtra("sub_service") as SubServiceCategoriesResponse.ResponseData
        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text =
                resources.getString(R.string.setup_your_service)

        viewModel.navigator = this
        binding.servicePriceViewModel = viewModel
        viewModel.getSubCategory(service.id.toString(), subService.id)
        loadingObservable.value = true

        service_save_btn.setOnClickListener {
            val selectedService = mutableListOf<SelectedService>()
            subServicePriceCategoriesResponse.responseData.forEach {
                if (it.selected == "1" || it.providerservices.isNotEmpty()) {
                    val newService = SelectedService()
                    newService.id = it.id
                    if (service.price_choose == "provider_price") {
                        if (it.servicescityprice != null || it.providerservices.isNotEmpty()) {
                            newService.fareType = "FIXED"
                            if (it.servicescityprice != null)
                                newService.fareType = it.servicescityprice.fare_type
                            if (it.servicescityprice != null)
                                when (it.servicescityprice.fare_type) {
                                    "FIXED" -> {
                                        if (it.providerservices.isNotEmpty()) {
                                            newService.baseFare = it.providerservices[0].base_fare
                                        } else if (it.servicescityprice != null && it.servicescityprice.base_fare.isNotEmpty()) {
                                            newService.baseFare = it.servicescityprice.base_fare
                                        } else {
                                            ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                            return@setOnClickListener
                                        }
                                    }
                                    "HOURLY" -> {
                                        if (it.providerservices.isNotEmpty()) {
                                            newService.perMins = it.providerservices[0].per_mins
                                        } else if (it.servicescityprice != null && it.servicescityprice.per_mins.isNotEmpty()) {
                                            newService.perMins = it.servicescityprice.per_mins
                                        } else {
                                            ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                            return@setOnClickListener
                                        }
                                    }
                                    "DISTANCETIME" -> {
                                        if (it.providerservices.isNotEmpty()) {
                                            newService.perMins = it.providerservices[0].per_mins
                                            newService.perMiles = it.providerservices[0].per_miles
                                        } else if (it.servicescityprice.per_miles.isNotEmpty()) {
                                            if (it.servicescityprice != null && it.servicescityprice.per_mins.isNotEmpty()) {
                                                newService.perMins = it.servicescityprice.per_mins
                                            } else {
                                                ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                                return@setOnClickListener
                                            }
                                            newService.perMiles = it.servicescityprice.per_miles
                                        } else {
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
                viewModel.postSelection(service.id.toString(), subService.id, selectedService)
            }
        }

        checkResponse()
        checkErrorResponse()
        checkPrice()
        checkAddServiceResponse()
    }

    private fun checkAddServiceResponse() {
        viewModel.addServiceResponseModel.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this, it.message, true)
        })
    }

    private fun checkPrice() {
        viewModel.listPrice.observe(this, Observer {
            if (selectedService.servicescityprice != null) {
            } else {
                selectedService.servicescityprice = SubServicePriceCategoriesResponse.Servicescityprice()
            }
            selectedService.servicescityprice.fare_type = it.fareType
            when (it.fareType) {
                "FIXED" -> {
                    selectedService.providerservices = mutableListOf()
                    val providerService = SubServicePriceCategoriesResponse.ProviderServices()
                    selectedService.servicescityprice.base_fare = it.baseFare
                    providerService.base_fare = it.baseFare
                    selectedService.providerservices.add(providerService)
                }
                "DISTANCETIME" -> {
                    selectedService.providerservices = mutableListOf()
                    selectedService.servicescityprice.per_mins = it.perMins
                    selectedService.servicescityprice.per_miles = it.perMiles
                    val providerService = SubServicePriceCategoriesResponse.ProviderServices()
                    providerService.per_mins = it.perMins
                    providerService.per_miles = it.perMiles
                    selectedService.providerservices.add(providerService)
                }
                "HOURLY" -> {
                    val providerService = SubServicePriceCategoriesResponse.ProviderServices()
                    selectedService.providerservices = mutableListOf()
                    selectedService.servicescityprice.per_mins = it.perMins
                    providerService.per_mins = it.perMins
                    selectedService.providerservices.add(providerService)
                }
            }
            binding.subServiceRv.adapter!!.notifyDataSetChanged()
        })
    }

    private fun checkErrorResponse() {
        viewModel.errorResponse.observe(this, Observer {
            loadingObservable.value = false
            ViewUtils.showToast(this, it.toString(), false)
        })
    }

    private fun checkResponse() {
        viewModel.subServiceCategoriesPriceResponse.observe(this, Observer {
            loadingObservable.value = false
            if (it?.responseData != null && it.responseData.isNotEmpty()) {
                subServicePriceCategoriesResponse = it
                val adapter = SubServicePriceAdapter(this, subServicePriceCategoriesResponse, service.price_choose == "provider_price")
                binding.subServiceRv.adapter = adapter
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
                            selected.perMins = service.servicescityprice.per_mins
                            selected.perMiles = service.servicescityprice.per_miles

                        } else {
                            selected.fareType = "FIXED"
                            selected.baseFare = ""
                        }
                        if (service.providerservices.isNotEmpty()) {
                            selected.baseFare = service.providerservices[0].base_fare
                            selected.perMins = service.providerservices[0].per_mins
                            selected.perMiles = service.providerservices[0].per_miles
                        }
                        val editServicePriceDialog = EditServicePriceDialogFragment()
                        viewModel.dialogPrice.value = selected
                        editServicePriceDialog.show(supportFragmentManager, "")
                        editServicePriceDialog.isCancelable = false
                    }
                    else -> {
                        ViewUtils.showToast(this, getString(R.string.select_service), false)
                    }
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
                binding.subServiceRv.adapter!!.notifyDataSetChanged()
            }
        }
    }

    class SelectedService(
            var id: String = "",
            var fareType: String = "",
            var baseFare: String = "",
            var perMins: String = "",
            var selected: String = "",
            var perMiles: String = ""
    )

    override fun showError(error: String) {
        loadingObservable.value = false
    }
}
