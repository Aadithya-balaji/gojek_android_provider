package com.xjek.provider.views.earnings

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.model.EarningsResponse
import com.xjek.provider.repository.AppRepository

class EarningsViewModel : BaseViewModel<EarningsNavigator>() {

    val appRepository = AppRepository.instance()
    var loadingProgress = MutableLiveData<Boolean>()

    val earningsMonth = MutableLiveData<EarningsResponse>()
    val earningsDay = MutableLiveData<EarningsResponse>()
    val earningsWeek = MutableLiveData<EarningsResponse>()

    fun earnings(userId: Int) {
        getCompositeDisposable().add(appRepository.getMonthlyEarnings(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), userId)
        )
        getCompositeDisposable().add(appRepository.getWeeklyEarnings(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), userId)
        )
        getCompositeDisposable().add(appRepository.getDailyEarnings(this,
                "Bearer " + readPreferences<String>(PreferencesKey.ACCESS_TOKEN), userId)
        )
    }
}