package com.xjek.xuberservice.repositary

import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.repository.BaseRepository


class XuperRepoitory:BaseRepository(){
    private val serviceId: String
        get() = PreferencesHelper.get(PreferencesKey.BASE_ID)
    companion object {
        private val TAG = XuperRepoitory::class.java.simpleName
        private var xuperRepoitory: XuperRepoitory? = null

        fun instance(): XuperRepoitory {
            if (xuperRepoitory == null) {
                synchronized(XuperRepoitory) {
                    xuperRepoitory = XuperRepoitory()
                }
            }
            return xuperRepoitory!!
        }
    }

}
