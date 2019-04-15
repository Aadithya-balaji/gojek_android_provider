package com.xjek.provider.views.pendinglist

import android.view.View
import com.xjek.base.base.BaseViewModel

class  PendingListViewModel:BaseViewModel<PendingListNavigator>(){
     fun selectedPendingList(view: View){
         navigator.pickItem(view)
     }
}