package com.gox.partner.views.invitereferals

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.ProfileResponse
import com.gox.partner.models.Referral
import com.gox.partner.repository.AppRepository

public class InviteReferralsViewModel : BaseViewModel<InviteReferralsNavigator>() {

    var appRepository = AppRepository.instance()
    var mReferalAmount = MutableLiveData<String>()
    var mReferalCount = MutableLiveData<String>()
    var mUserReferalAmount = MutableLiveData<String>()
    var mUserReferalCount = MutableLiveData<String>()
    var mReferalObj = MutableLiveData<Referral>()
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