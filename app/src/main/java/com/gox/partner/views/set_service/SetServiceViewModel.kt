package com.gox.partner.views.set_service

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.ServiceCategoriesResponse
import com.gox.partner.repository.AppRepository

class SetServiceViewModel : BaseViewModel<SetServiceNavigator>() {

    private val appRepository = AppRepository.instance()
    val serviceCategoriesResponse = MutableLiveData<ServiceCategoriesResponse>()
    val errorResponse = MutableLiveData<String>()

    fun getCategories() {
        val token = StringBuilder("Bearer ")
                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
                .toString()
        getCompositeDisposable().add(appRepository.getServiceCategories(this, token))
    }
}
