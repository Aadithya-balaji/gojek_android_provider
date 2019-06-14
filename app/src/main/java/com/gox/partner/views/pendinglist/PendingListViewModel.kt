package com.gox.partner.views.pendinglist

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.partner.utils.Constant
import com.gox.partner.views.home.VerificationModel

class PendingListViewModel : BaseViewModel<PendingListNavigator>() {

    fun getVerificationObservable(): MutableLiveData<VerificationModel> = Constant.verificationObservable

    fun selectedPendingList(view: View) {
        navigator.pickItem(view)
    }
}