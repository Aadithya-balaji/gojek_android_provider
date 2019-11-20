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
import com.gox.partner.models.*
import com.gox.partner.views.edit_service_price.EditServicePriceDialogFragment
import kotlinx.android.synthetic.main.activity_set_service_category_price.*
import kotlinx.android.synthetic.main.layout_app_bar.view.*
import java.util.*

class SetServicePriceActivity : BaseActivity<ActivitySetServiceCategoryPriceBinding>(),
        SetServicePriceNavigator, SubServicePriceAdapter.ServiceItemClick {

    private lateinit var mBinding: ActivitySetServiceCategoryPriceBinding
    private lateinit var mViewModel: SetServicePriceViewModel

    private lateinit var service: ServiceCategoriesResponse.ResponseData
    private lateinit var selectedService: SubServicePriceResponseData
    private lateinit var subServicePriceCategoriesResponse: SubServicePriceCategoriesResponse
    private var isEdit:Boolean = false

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
            subServicePriceCategoriesResponse.responseData!!.forEach {
                if (it!!.selected == "1" || it.providerservices!!.isNotEmpty()) {
                    val newService = SelectedService()
                    newService.id = it.id
                    if (service.price_choose == "provider_price") {
                        if (it.providerservices!!.isNotEmpty()) {
                            newService.fareType = FIXED
                            newService.fareType = it.service_city?.fare_type
                            if (it.service_city != null)
                                when (it.service_city?.fare_type) {
                                    FIXED -> when {
                                        it.providerservices!!.isNotEmpty() -> newService.baseFare = it.providerservices!![0]!!.base_fare
                                        it.service_city?.base_fare!! > 0 -> newService.baseFare = it.service_city?.base_fare
                                        else -> {
                                            ViewUtils.showToast(this,
                                                    getString(R.string.enter_amount_selected_service), false)
                                            return@setOnClickListener
                                        }
                                    }
                                    HOURLY -> when {
                                        it.providerservices!!.isNotEmpty() -> newService.perMin = it.providerservices!![0]!!.per_mins
                                        it.service_city?.per_mins!! > 0 -> newService.perMin = it.service_city?.per_mins
                                        else -> {
                                            ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                            return@setOnClickListener
                                        }
                                    }
                                    DISTANCE_TIME -> when {
                                        it.providerservices!!.isNotEmpty() -> {
                                            newService.perMin = it.providerservices!![0]!!.per_mins
                                            newService.perMiles = it.providerservices!![0]!!.per_miles
                                        }
                                        it.service_city?.per_miles!! > 0 -> {
                                            if (it.service_city?.per_mins!! > 0) {
                                                newService.perMin = it.service_city?.per_mins
                                            } else {
                                                ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                                return@setOnClickListener
                                            }
                                            newService.perMiles = it.service_city?.per_miles
                                        }
                                        else -> {
                                            ViewUtils.showToast(this, getString(R.string.enter_amount_selected_service), false)
                                            return@setOnClickListener
                                        }
                                    }
                                }
                            else {
                                if (it.providerservices!![0]!!.base_fare != null)
                                    newService.baseFare = it.providerservices!![0]!!.base_fare
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
            /*if (selectedService.isEmpty())
                ViewUtils.showToast(this, getString(R.string.select_service), false)
            else {
                loadingObservable.value = true
                mViewModel.postSelection(service.id.toString(), subService.id, selectedService)
            }*/
            loadingObservable.value = true
            mViewModel.postSelection(isEdit,service.id.toString(), subService.id, selectedService)
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
            selectedService.service_city = ServiceCity()
            selectedService.service_city?.fare_type = it.fareType!!
            when (it.fareType) {
                FIXED -> {
                    selectedService.providerservices = mutableListOf()
                    val providerService = ProviderService()
                    selectedService.service_city?.base_fare = it.baseFare
                    providerService.base_fare = it.baseFare
                    selectedService.providerservices!!.add(providerService)
                }
                DISTANCE_TIME -> {
                    selectedService.providerservices = mutableListOf()
                    selectedService.service_city?.per_mins = it.perMin
                    selectedService.service_city?.per_miles = it.perMiles
                    val providerService = ProviderService()
                    providerService.per_mins = it.perMin
                    providerService.per_miles = it.perMiles
                    selectedService.providerservices!!.add(providerService)
                }
                HOURLY -> {
                    val providerService = ProviderService()
                    selectedService.providerservices = mutableListOf()
                    selectedService.service_city?.per_mins = it.perMin
                    providerService.per_mins = it.perMin
                    selectedService.providerservices!!.add(providerService)
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
            subServicePriceCategoriesResponse = SubServicePriceCategoriesResponse()
            if (it?.responseData != null && it.responseData!!.isNotEmpty()) {
                subServicePriceCategoriesResponse.responseData = mutableListOf()

                for (i in it.responseData!!.indices)
                    if (it.responseData!![i]!!.service_city != null)
                        subServicePriceCategoriesResponse.responseData!!.add(it.responseData!![i]!!)


                isEdit = it.responseData?.any { data -> data?.providerservices?.size ?: 0 > 0}?:false

                if (!subServicePriceCategoriesResponse.responseData.isNullOrEmpty()
                        && subServicePriceCategoriesResponse.responseData?.size!! > 0){
                    val adapter = SubServicePriceAdapter(this, subServicePriceCategoriesResponse,
                            service.price_choose == "provider_price")
                    mBinding.subServiceRv.adapter = adapter
                    adapter.serviceItemClick = this
                }
            }
        })
    }

    override fun onItemClick(service: SubServicePriceResponseData, isPriceEdit: Boolean) {
        selectedService = service
        when {
            isPriceEdit -> when (service.selected == "1" || service.providerservices!!.isNotEmpty()) {
                true -> {
                    val selected = SelectedService()
                    if (service.service_city?.base_fare != null) {
                        selected.fareType = service.service_city?.fare_type
                        selected.baseFare = service.service_city?.base_fare
                        selected.perMin = service.service_city?.per_mins
                        selected.perMiles = service.service_city?.per_miles
                    } else {
                        selected.fareType = FIXED
                        selected.baseFare = 0
                    }
                    if (service.providerservices!!.isNotEmpty()) {
                        selected.baseFare = service.providerservices!![0]!!.base_fare
                        selected.perMin = service.providerservices!![0]!!.per_mins
                        selected.perMiles = service.providerservices!![0]!!.per_miles
                    }
                    val editServicePriceDialog = EditServicePriceDialogFragment()
                    mViewModel.dialogPrice.value = selected
                    editServicePriceDialog.show(supportFragmentManager, "")
                    editServicePriceDialog.isCancelable = false
                }
                else -> ViewUtils.showToast(this, getString(R.string.select_service), false)
            }
            else -> {
                when (service.selected == "1" || service.providerservices!!.isNotEmpty()) {
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
            var id: Int? = 0,
            var fareType: String? = "",
            var baseFare: Int? = 0,
            var perMin: Int? = 0,
            var perMiles: Int? = 0
    )

    override fun showError(error: String) {
        loadingObservable.value = false
    }
}