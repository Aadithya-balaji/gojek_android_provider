package com.gox.partner.views.earnings

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.partner.models.EarningsResponse
import com.gox.partner.repository.AppRepository

class EarningsViewModel : BaseViewModel<EarningsNavigator>() {

    val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()

    val earnings = MutableLiveData<EarningsResponse>()

    fun earnings(userId: Int) {
        getCompositeDisposable().add(appRepository.getEarnings(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), userId)
        )
    }
}