package com.xjek.xuberservice.rating

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.xuberservice.model.XuperRatingModel
import com.xjek.xuberservice.repositary.XuperRepoitory

class  XuperRatingViewModel:BaseViewModel<XuperRatingNavigator>(){
    val xuperRepository=XuperRepoitory.instance()
    val ratingLiveData=MutableLiveData<XuperRatingModel>()
    var comment=MutableLiveData<String>()
    val rating=MutableLiveData<String>()
    val id=MutableLiveData<String>()
    val adminServiceid=MutableLiveData<String>()
    val firstName=MutableLiveData<String>()
    val lastName=MutableLiveData<String>()
    var showProgress=MutableLiveData<Boolean>()
    var userRating=MutableLiveData<String>()


    fun  callRateApi(){
        showProgress.value=true
        navigator.sumitRating()
    }

    fun callRatingApi(params:HashMap<String,String>){
        getCompositeDisposable().add(xuperRepository.xuperRatingUser(this, "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN),params))
    }
}