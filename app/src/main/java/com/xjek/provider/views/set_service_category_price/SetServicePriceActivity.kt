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
            subServicePriceCategoriesResponse.responseData.forEach {
                if (it.selected == "1" || it.providerservices.isNotEmpty()) {

                } else {

                }
                ViewUtils.showToast(this, getString(R.string.select_service), false)
            }
        }

        checkResponse()
        checkErrorResponse()
        checkPrice()
    }

    private fun checkPrice() {
        viewModel.base_fare.observe(this, Observer {
            if (selectedService.servicescityprice != null) {
                selectedService.servicescityprice.base_fare = it
            } else {
                selectedService.servicescityprice = SubServicePriceCategoriesResponse.Servicescityprice()
                selectedService.servicescityprice.base_fare = it
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
                when (service.selected) {
                    "1" -> {
                        var price = ""
                        if (service.servicescityprice != null &&
                                service.servicescityprice.base_fare != null)
                            price = service.servicescityprice.base_fare

                        val editServicePriceDialog = EditServicePriceDialogFragment()
                        viewModel.dialogPrice.value = price
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
                        service.providerservices = listOf()
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
}
