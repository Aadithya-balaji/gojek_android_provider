package com.xjek.provider.views.manage_bank_details

import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.models.BankTemplateModel
import com.xjek.provider.repository.AppRepository

class ManageBankDetailsViewModel : BaseViewModel<ManageBankDetailsNavigator>() {

    private val appRepository = AppRepository.instance()
    private val token = StringBuilder("Bearer ")
            .append(readPreferences<String>(PreferencesKey.ACCESS_TOKEN))
            .toString()

    var showLoading = MutableLiveData<Boolean>()
    var bankTemplateLiveData = MutableLiveData<BankTemplateModel>()
    var errorResponse = MutableLiveData<String>()


    fun getBankTemplate() {
        getCompositeDisposable()
                .add(appRepository.getBankTemplate(this,token))
    }

}