package com.xjek.provider.views.set_service_category_price

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.AddVehicleResponseModel
import com.xjek.provider.models.SubServicePriceCategoriesResponse
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.views.set_service_category_price.SetServicePriceActivity.SelectedService

class SetServicePriceViewModel : BaseViewModel<SetServicePriceNavigator>() {

    private val appRepository = AppRepository.instance()
    val subServiceCategoriesPriceResponse = MutableLiveData<SubServicePriceCategoriesResponse>()
    val errorResponse = MutableLiveData<String>()
    val listPrice = MutableLiveData<SelectedService>()
    val dialogPrice = MutableLiveData<SelectedService>()
    val addServiceResponseModel = MutableLiveData<AddVehicleResponseModel>()

    fun getSubCategory(serviceId: String, SubServiceId: String) {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        val service = HashMap<String, String>()
        service["service_category_id"] = serviceId
        service["service_subcategory_id"] = SubServiceId
        getCompositeDisposable().add(appRepository.getSubServicePriceCategories(this, token, service))
    }

    fun postSelection(toString: String, id: String, selectedService: MutableList<SelectedService>) {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        val params = HashMap<String, String>()
        params["category_id"] = (toString)
        params["sub_category_id"] = (id)
        //TODO Need to add Proper Admin Service ID
        params["admin_service_id"] = ("3")
        if (selectedService.isNotEmpty()) {
            var i = 0
            selectedService.forEach {
                params["service[$i][service_id]"] = (it.id)
                when (it.fareType) {
                    "FIXED" -> params["service[$i][base_fare]"] = (it.baseFare)
                    "HOURLY" -> params["service[$i][per_mins]"] = (it.perMins)
                    "DISTANCETIME" -> {
                        params["service[$i][per_mins]"] = (it.perMins)
                        params["service[$i][per_miles]"] = (it.perMiles)
                    }
                }
                i += 1
            }
        }
        getCompositeDisposable().add(appRepository.postVehicle(this, token, params))
    }
}
