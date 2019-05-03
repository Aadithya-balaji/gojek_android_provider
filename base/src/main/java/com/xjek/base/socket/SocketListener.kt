package com.xjek.base.socket

interface SocketListener{

    interface ConnectionListener{
        fun onConnected()

        fun onDisconnected()

        fun onConnectionError()

        fun onConnectionTimeOut()
    }

}