package com.gox.base.socket

interface SocketListener{

    interface CallBack{
        fun onConnected()

        fun onDisconnected()

        fun onConnectionError()

        fun onConnectionTimeOut()
    }

    interface connectionRefreshCallBack{
        fun onRefresh()
    }

}