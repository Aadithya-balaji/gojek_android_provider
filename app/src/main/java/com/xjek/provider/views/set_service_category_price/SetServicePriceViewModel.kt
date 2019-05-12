package com.xjek.provider.views.set_service_category_price

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.SubServicePriceCategoriesResponse
import com.xjek.provider.repository.AppRepository

class SetServicePriceViewModel : BaseViewModel<SetServicePriceNavigator>() {

    private val appRepository = AppRepository.instance()
    val subServiceCategoriesPriceResponse = MutableLiveData<SubServicePriceCategoriesResponse>()
    val errorResponse = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val dialogPrice = MutableLiveData<String>()

    fun getSubCategory(serviceId: String, SubServiceId: String) {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        val service = HashMap<String, String>()
        service["service_category_id"] = serviceId
        service["service_subcategory_id"] = SubServiceId
        getCompositeDisposable().add(appRepository.getSubServicePriceCategories(this, token, service))
    }
}
