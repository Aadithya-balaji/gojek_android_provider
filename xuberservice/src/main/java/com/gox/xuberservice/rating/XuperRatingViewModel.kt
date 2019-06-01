package com.gox.xuberservice.rating

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.xuberservice.model.XuperRatingModel
import com.gox.xuberservice.repositary.XuperRepoitory

class XuperRatingViewModel : BaseViewModel<XuperRatingNavigator>() {
    val xuperRepository = XuperRepoitory.instance()
    val ratingLiveData = MutableLiveData<XuperRatingModel>()
    var comment = MutableLiveData<String>()
    val rating = MutableLiveData<String>()
    val id = MutableLiveData<String>()
    val adminServiceid = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    var showProgress = MutableLiveData<Boolean>()
    var userRating = MutableLiveData<String>("1")

    fun callRateApi() {
        showProgress.value = true
        navigator.sumitRating()
    }

    fun callRatingApi(params: HashMap<String, String>) {
        getCompositeDisposable().add(xuperRepository.xuperRatingUser(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), params))
    }
}