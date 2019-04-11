package com.xjek.provider.views.invitereferals

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.ProfileResponse
import com.xjek.provider.models.ReferalDataModel
import com.xjek.provider.repository.AppRepository

public class InviteReferalsViewModel : BaseViewModel<InviteReferalsNavigator>() {

    var appRepository = AppRepository.instance()
    var mReferalAmount = MutableLiveData<String>()
    var mReferalCount = MutableLiveData<String>()
    var mUserReferalAmount = MutableLiveData<String>()
    var mUserReferalCount = MutableLiveData<String>()
    var mReferalObj = MutableLiveData<ReferalDataModel>()
    var profileResponse = MutableLiveData<ProfileResponse>()
    var loadingProgress = MutableLiveData<Boolean>()


    fun shareMyReferalCode() {
        navigator.goToInviteOption()
    }

    fun getProfileDetail() {
        getCompositeDisposable().add(appRepository.getReferal(this, "Bearer" + " " +
                readPreferences<String>(PreferencesKey.ACCESS_TOKEN)))
    }

    fun getProfileLiveData() = profileResponse
}