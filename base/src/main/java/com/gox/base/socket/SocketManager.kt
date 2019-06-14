package com.gox.base.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URISyntaxException

object SocketManager {

    private var mSocket: Socket? = null
    private lateinit var mURL: String

    private var mSocketCallBack: SocketListener.CallBack? = null
    private var mConnectionListener: SocketListener.ConnectionRefreshCallBack? = null

    private var onConnected: Emitter.Listener = Emitter.Listener {
        if (mSocketCallBack != null)
            mSocketCallBack!!.onConnected()
        if(mConnectionListener!=null)
            mConnectionListener!!.onRefresh()
        Log.e("SocketManager", "SocketManager connected ")
    }

    private var onDisconnected: Emitter.Listener = Emitter.Listener {
        if (mSocketCallBack != null)
            mSocketCallBack!!.onDisconnected()
        Log.e("SocketManager", "SocketManager DisConnected ")
    }

    private var onConnectionError: Emitter.Listener = Emitter.Listener {
        if (mSocketCallBack != null)
            mSocketCallBack!!.onConnectionError()
        Log.e("SocketManager", "SocketManager ConnectionError ")
    }

    private var onConnectionTimeOut: Emitter.Listener = Emitter.Listener {
        if (mSocketCallBack != null)
            mSocketCallBack!!.onConnectionTimeOut()
        Log.e("SocketManager", "SocketManager ConnectionTimeout ")
    }

    fun setOnConnectionListener(socketCallBack: SocketListener.CallBack) {
        mSocketCallBack = socketCallBack
    }

    fun setOnSocketRefreshListener(connectionRefreshCallback: SocketListener.ConnectionRefreshCallBack){
        mConnectionListener = connectionRefreshCallback
    }

    fun connect(url: String) {
        mURL = url
        if (mSocket == null) {
            try {
                mSocket = IO.socket(mURL)
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }

            mSocket!!.on(Socket.EVENT_CONNECT, onConnected)
            mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnected)
            mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectionError)
            mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeOut)
        }
        mSocket!!.connect()
        Log.e("SocketManager", "SocketManager establishSocketConnection $mURL")
    }

    fun emit(key: String, value: Any) {
        mSocket!!.emit(key, value)
    }

    fun onEvent(key: String, listener: Emitter.Listener) {
        mSocket!!.on(key, listener)
    }

    fun isConnected(): Boolean = mSocket!!.connected()

    fun disConnect() {
        mSocket!!.disconnect()
    }

}