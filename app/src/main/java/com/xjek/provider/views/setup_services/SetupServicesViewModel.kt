package com.xjek.provider.views.setup_services

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.views.setup_vehicle.SetupVehicleAdapter

class SetupServicesViewModel : BaseViewModel<SetupServicesNavigator>() {

    private val appRepository = AppRepository.instance()
    private val vehicleLiveData = MutableLiveData<Any>()

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

//    fun getCategories() {
//        val token = StringBuilder("Bearer ")
//                .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
//                .toString()
//        getCompositeDisposable().add(appRepository.getRides(this, token))
//    }

    fun getVehicleDataObservable() = vehicleLiveData

    fun onItemClick(position: Int) {
        navigator.onMenuItemClicked(position)
    }
}