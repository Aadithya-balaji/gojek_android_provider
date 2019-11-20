package com.gox.partner.views.set_service_category_price

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.Constants.FareType.DISTANCE_TIME
import com.gox.base.data.Constants.FareType.FIXED
import com.gox.base.data.Constants.FareType.HOURLY
import com.gox.base.repository.ApiListener
import com.gox.partner.models.AddVehicleResponseModel
import com.gox.partner.models.SubServicePriceCategoriesResponse
import com.gox.partner.repository.AppRepository
import com.gox.partner.views.set_service_category_price.SetServicePriceActivity.SelectedService

class SetServicePriceViewModel : BaseViewModel<SetServicePriceNavigator>() {

    private val mRepository = AppRepository.instance()
    val subServiceCategoriesPriceResponse = MutableLiveData<SubServicePriceCategoriesResponse>()
    val errorResponse = MutableLiveData<String>()
    val listPrice = MutableLiveData<SelectedService>()
    val dialogPrice = MutableLiveData<SelectedService>()
    val addServiceResponseModel = MutableLiveData<AddVehicleResponseModel>()

    fun getSubCategory(serviceId: String, SubServiceId: String) {
        val service = HashMap<String, String>()
        service["service_category_id"] = serviceId
        service["service_subcategory_id"] = SubServiceId
        getCompositeDisposable().add(mRepository.getSubServicePriceCategories(object : ApiListener {
            override fun success(successData: Any) {
                subServiceCategoriesPriceResponse.value = successData as SubServicePriceCategoriesResponse
            }

            override fun fail(failData: Throwable) {
                errorResponse.value = getErrorMessage(failData)
            }
        }, service))
    }

    fun postSelection(isEdit:Boolean,toString: String, id: String, selectedService: MutableList<SelectedService>) {
        val params = HashMap<String, String>()
        params["category_id"] = (toString)
        params["sub_category_id"] = (id)
        params["admin_service"] = Constants.ModuleTypes.SERVICE
        if (selectedService.isNotEmpty()) {
            var i = 0
            selectedService.forEach {
                params["service[$i][service_id]"] = (it.id.toString())
                when (it.fareType) {
                    FIXED -> params["service[$i][base_fare]"] = (it.baseFare.toString())
                    HOURLY -> params["service[$i][per_mins]"] = (it.perMin.toString())
                    DISTANCE_TIME -> {
                        params["service[$i][per_mins]"] = (it.perMin.toString())
                        params["service[$i][per_miles]"] = (it.perMiles.toString())
                    }
                }
                i += 1
            }
        }

        if(!isEdit){
            getCompositeDisposable().add(mRepository.postVehicle(object : ApiListener {
                override fun success(successData: Any) {
                    addServiceResponseModel.value = successData as AddVehicleResponseModel
                }

                override fun fail(failData: Throwable) {
                    navigator.showError(getErrorMessage(failData))
                }
            }, params))
        }else{
            getCompositeDisposable().add(mRepository.editVehicle(object : ApiListener {
                override fun success(successData: Any) {
                    addServiceResponseModel.value = successData as AddVehicleResponseModel
                }

                override fun fail(failData: Throwable) {
                    navigator.showError(getErrorMessage(failData))
                }
            }, params))
        }




    }
}
