package com.gox.partner.views.set_service_category_price

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants.FareType.DISTANCE_TIME
import com.gox.base.data.Constants.FareType.FIXED
import com.gox.base.data.Constants.FareType.HOURLY
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.AddVehicleResponseModel
import com.gox.partner.models.SubServicePriceCategoriesResponse
import com.gox.partner.repository.AppRepository
import com.gox.partner.views.set_service_category_price.SetServicePriceActivity.SelectedService

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
        params["admin_service_id"] = ("3")
        if (selectedService.isNotEmpty()) {
            var i = 0
            selectedService.forEach {
                params["service[$i][service_id]"] = (it.id)
                when (it.fareType) {
                    FIXED -> params["service[$i][base_fare]"] = (it.baseFare)
                    HOURLY -> params["service[$i][per_mins]"] = (it.perMins)
                    DISTANCE_TIME -> {
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
