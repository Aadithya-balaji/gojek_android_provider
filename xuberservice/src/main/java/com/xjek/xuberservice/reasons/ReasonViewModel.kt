package com.xjek.taximodule.ui.fragment.reason

import android.preference.Preference
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel;
import com.xjek.base.base.BaseApplication
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.xuberservice.model.ReasonModel
import com.xjek.xuberservice.repositary.XuperRepoitory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ReasonViewModel : BaseViewModel<ReasonNavigator>() {
    val xuperAppRepository=XuperRepoitory.instance()
    private lateinit var subscription: Disposable
    val mReasonResponseData = MutableLiveData<ReasonModel>()
    var errorResponse = MutableLiveData<Throwable>()


    fun dismissPopup() {
        navigator.closePopup()
    }

    fun getReason(type: String) {
       getCompositeDisposable().add(xuperAppRepository.xuperGetReason(this,"Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), Constants.Reasons.SERVICE))
    }


}
