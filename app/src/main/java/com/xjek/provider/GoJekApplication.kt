package com.xjek.provider

import android.content.Intent
import com.xjek.base.base.BaseApplication
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.session.SessionListener
import com.xjek.base.session.SessionManager
import com.xjek.provider.views.on_board.OnBoardActivity


class GoJekApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        SessionManager.instance(object: SessionListener {
            override fun invalidate() {
                logoutApp()
            }

            override fun refresh() {
            }
        })
    }

    private fun logoutApp() {
        PreferencesHelper.removeAll()
        val newIntent = Intent(applicationContext, OnBoardActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
    }
}