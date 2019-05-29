package com.gox.partner.views.places

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.partner.models.CountryListResponse
import com.gox.partner.models.CityListResponse
import com.gox.partner.models.StateListResponse

class PlacesViewModel : BaseViewModel<PlacesNavigator>() {
    var stateListResponse = MutableLiveData<StateListResponse>()
    var cityListResponse = MutableLiveData<CityListResponse>()
    var countryListResponse = MutableLiveData<CountryListResponse>()
    var errorResponse = MutableLiveData<Throwable>()

}