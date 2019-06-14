package com.gox.base.session

object SessionManager {

    private lateinit var listener: SessionListener

    fun instance(listener: SessionListener) {
        this.listener = listener
    }

    fun clearSession() = listener.invalidate()

}