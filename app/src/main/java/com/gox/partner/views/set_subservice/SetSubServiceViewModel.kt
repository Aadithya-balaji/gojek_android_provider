package com.gox.partner.views.set_subservice

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.SubServiceCategoriesResponse
import com.gox.partner.repository.AppRepository

class SetSubServiceViewModel : BaseViewModel<SetSubServiceNavigator>() {

    private val appRepository = AppRepository.instance()
    val subServiceCategoriesResponse = MutableLiveData<SubServiceCategoriesResponse>()
    val errorResponse = MutableLiveData<String>()

    fun getSubCategories(id: String) {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        val service = HashMap<String, String>()
        service["service_category_id"] = id
        getCompositeDisposable().add(appRepository.getSubServiceCategories(this, token, service))
    }
}
