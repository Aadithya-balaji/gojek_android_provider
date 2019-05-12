package com.xjek.provider.views.set_subservice

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.SubServiceCategoriesResponse
import com.xjek.provider.repository.AppRepository

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
