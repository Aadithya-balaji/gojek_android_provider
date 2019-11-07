package com.gox.partner

import android.content.Intent
import android.util.Log
import com.downloader.PRDownloader
import com.gox.base.BuildConfig
import com.gox.base.base.BaseApplication
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesHelper
import com.gox.base.session.SessionListener
import com.gox.base.session.SessionManager
import com.gox.base.socket.SocketListener
import com.gox.base.socket.SocketManager
import com.gox.partner.views.on_board.OnBoardActivity
import io.socket.emitter.Emitter

class GoJekApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        PRDownloader.initialize(applicationContext)

        SessionManager.instance(object : SessionListener {
            override fun invalidate() {
                logoutApp()
            }

            override fun refresh() {
            }
        })

        SocketManager.setOnConnectionListener(object : SocketListener.CallBack {
            override fun onConnected() {
                SocketManager.onEvent(Constants.RoomName.STATUS, Emitter.Listener {
                    Log.e("SOCKET", "SOCKET_SK status " + it[0])
                })

                SocketManager.onEvent(Constants.RoomName.UPDATELOCATION, Emitter.Listener {
                    Log.e("SOCKET", "SOCKET_SK location update status " + it[0])
                })
            }

            override fun onDisconnected() {
                Log.e("SOCKET", "SOCKET_SK disconnected")
            }

            override fun onConnectionError() {
                Log.e("SOCKET", "SOCKET_SK connection error")
            }

            override fun onConnectionTimeOut() {
                Log.e("SOCKET", "SOCKET_SK connection timeout")
            }
        })
        SocketManager.connect(BuildConfig.BASE_URL)
    }

    private fun logoutApp() {
        PreferencesHelper.removeAll()
        val newIntent = Intent(applicationContext, OnBoardActivity::class.java)
        newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(newIntent)
    }

}