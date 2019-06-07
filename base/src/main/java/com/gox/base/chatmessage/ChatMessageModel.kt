package com.gox.base.chatmessage

import java.io.Serializable

class ChatMessageModel : Serializable {
    var messageType: String? = null
    var message: String? = null
    var userName: String? = null
    var provider: String? = null

    companion object {
        const val USER = 1
        const val PROVIDER = 2
    }
}