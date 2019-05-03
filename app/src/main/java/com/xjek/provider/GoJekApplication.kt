package com.xjek.provider

import android.content.Intent
import android.util.Log
import com.xjek.base.BuildConfig
import com.xjek.base.base.BaseApplication
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.session.SessionListener
import com.xjek.base.session.SessionManager
import com.xjek.base.socket.SocketListener
import com.xjek.base.socket.SocketManager
import com.xjek.provider.views.on_board.OnBoardActivity
import io.socket.emitter.Emitter


class GoJekApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        SessionManager.instance(object : SessionListener {
            override fun invalidate() {
                logoutApp()
            }

            override fun refresh() {
            }
        })


        SocketManager.setOnConnectionListener(object : SocketListener.CallBack {
            override fun onConnected() {
                SocketManager.onEvent(Constants.ROOM_NAME.STATUS, Emitter.Listener {
                    Log.e("SOCKET","SOCKET_SK status"+it[0])
                })
            }

            override fun onDisconnected() {
                Log.e("SOCKET","SOCKET_SK disconnected")
            }

            override fun onConnectionError() {
                Log.e("SOCKET","SOCKET_SK connection error")
            }

            override fun onConnectionTimeOut() {
                Log.e("SOCKET","SOCKET_SK connection timeout")
            }
        })
        SocketManager.connect(BuildConfig.BASE_URL)

    }

    private fun logoutApp() {
        PreferencesHelper.removeAll()
        val newIntent = Intent(applicationContext, OnBoardActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
    }
}