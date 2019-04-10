package com.xjek.provider.views.invitereferals

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.models.ProfileResponse
import com.xjek.provider.models.ReferalDataModel
import com.xjek.provider.repository.AppRepository
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.invitereferals.InviteReferalsNavigator

public class InviteReferalsViewModel : BaseViewModel<InviteReferalsNavigator>() {

    var appRepository=AppRepository.instance()
    var mReferalAmount=MutableLiveData<String>()
    var mReferalCount=MutableLiveData<String>()
    var mUserReferalAmount=MutableLiveData<String>()
    var mUserReferalCount=MutableLiveData<String>()
    var mReferalObj=MutableLiveData<ReferalDataModel>()
    var profileResponse = MutableLiveData<ProfileResponse>()
    var loadingProgress = MutableLiveData<Boolean>()


    fun shareMyReferalCode() {
        navigator.goToInviteOption()
    }

    fun getProfileDetail(){
       getCompositeDisposable().add(appRepository.getReferal(this,"Bearer"+" "+Constant.accessToken))
    }

    fun getProfileLiveData()=profileResponse
}