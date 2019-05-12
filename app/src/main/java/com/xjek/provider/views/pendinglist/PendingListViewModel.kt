package com.xjek.provider.views.pendinglist

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseViewModel
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.home.VerificationModel

class PendingListViewModel : BaseViewModel<PendingListNavigator>() {


    fun getVerificationObservable():MutableLiveData<VerificationModel> = Constant.verificationObservable

    fun selectedPendingList(view: View) {
        navigator.pickItem(view)
    }
}