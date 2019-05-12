package com.xjek.provider.views.setup_services

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.ServiceCategoriesResponse
import com.xjek.provider.repository.AppRepository

class SetupServicesViewModel : BaseViewModel<SetupServicesNavigator>() {

    private val appRepository = AppRepository.instance()
    private val serviceCategoriesResponse = MutableLiveData<ServiceCategoriesResponse>()
    private val loadingObservable = MutableLiveData<Boolean>()
    var errorResponse = MutableLiveData<String>()

    private val adapter: SetupServicesAdapter = SetupServicesAdapter(this)

    fun setAdapter() {
        adapter.notifyDataSetChanged()
    }

    fun getAdapter() = adapter

    fun getItemCount(): Int {
        return 0
    }

    fun getServiceName(position: Int): String {
        return ""
    }

    fun getCategories() {
//        val token = StringBuilder("Bearer ")
//                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
//                .toString()
//        getCompositeDisposable().add(appRepository.getServiceCategories(this, token))
    }

    fun getServicesDataObservable() = serviceCategoriesResponse

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}