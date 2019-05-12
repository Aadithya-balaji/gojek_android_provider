package com.xjek.provider.views.set_service

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.ServiceCategoriesResponse
import com.xjek.provider.repository.AppRepository

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
