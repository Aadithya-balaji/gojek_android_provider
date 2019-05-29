package com.gox.xuberservice.reasons

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.xuberservice.model.ReasonModel
import com.gox.xuberservice.repositary.XuperRepoitory
import io.reactivex.disposables.Disposable

class XUberCancelReasonViewModel : BaseViewModel<XUberCancelReasonNavigator>() {

    private val xuperAppRepository = XuperRepoitory.instance()
    private lateinit var subscription: Disposable
    val mReasonResponseData = MutableLiveData<ReasonModel>()
    var errorResponse = MutableLiveData<Throwable>()

    fun dismissPopup() {
        navigator.closePopup()
    }

    fun getReason(type: String) {
        getCompositeDisposable().add(xuperAppRepository.xuperGetReason(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), Constants.ModuleTypes.SERVICE))
    }
}