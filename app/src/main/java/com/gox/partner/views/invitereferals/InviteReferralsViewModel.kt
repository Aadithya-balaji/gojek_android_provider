package com.gox.partner.views.invitereferals

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.partner.models.ProfileResponse
import com.gox.partner.models.Referral
import com.gox.partner.repository.AppRepository

class InviteReferralsViewModel : BaseViewModel<InviteReferralsNavigator>() {

    var mRepository = AppRepository.instance()
    var mReferralObj = MutableLiveData<Referral>()
    var profileResponse = MutableLiveData<ProfileResponse>()
    var showLoading = MutableLiveData<Boolean>()

    fun getProfileLiveData() = profileResponse

    fun shareMyReferralCode() = navigator.goToInviteOption()

    fun getProfileDetail() {
        getCompositeDisposable().add(mRepository.getReferral(object : ApiListener {
            override fun success(successData: Any) {
                getProfileLiveData().value = successData as ProfileResponse
            }

            override fun fail(failData: Throwable) {
                navigator.showError(getErrorMessage(failData))
            }
        }))
    }
}