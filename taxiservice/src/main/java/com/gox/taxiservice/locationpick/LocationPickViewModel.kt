package com.gox.taxiservice.locationpick

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.taxiservice.repositary.TaxiRepository
import io.reactivex.disposables.Disposable

class LocationPickViewModel : BaseViewModel<LocationPickNavigator>() {

    private val mRepository = TaxiRepository.instance()
    var errorResponse = MutableLiveData<String>()

    private lateinit var subscription: Disposable

}
