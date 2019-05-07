package com.xjek.xuberservice.reasons

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.taximodule.ui.fragment.reason.ReasonNavigator
import com.xjek.xuberservice.model.ReasonModel
import com.xjek.xuberservice.repositary.XuperRepoitory
import io.reactivex.disposables.Disposable

class ReasonViewModel : BaseViewModel<ReasonNavigator>() {

    private val xuperAppRepository = XuperRepoitory.instance()
    private lateinit var subscription: Disposable
    val mReasonResponseData = MutableLiveData<ReasonModel>()
    var errorResponse = MutableLiveData<Throwable>()

    fun dismissPopup() {
        navigator.closePopup()
    }

    fun getReason(type: String) {
        getCompositeDisposable().add(xuperAppRepository.xuperGetReason(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), Constants.Reasons.SERVICE))
    }
}