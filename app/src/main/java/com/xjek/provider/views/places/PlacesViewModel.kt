package com.xjek.provider.views.places

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.CityListResponse
import com.xjek.provider.models.StateListResponse
import com.xjek.user.data.repositary.remote.model.CountryListResponse

class  PlacesViewModel:BaseViewModel<PlacesNavigator>(){
    var stateListResponse = MutableLiveData<StateListResponse>()
    var cityListResponse = MutableLiveData<CityListResponse>()
    var countryListResponse=MutableLiveData<CountryListResponse>()
    var errorResponse = MutableLiveData<Throwable>()

}