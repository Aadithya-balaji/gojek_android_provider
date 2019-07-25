package com.gox.partner.fcm

data class  NotificationDataModel(
        var message: Message? = Message()
)

data class Message(
        var data: Data? = Data(),
        var notification: NotificationData? = NotificationData(),
        var topic: String? = ""
)

data class NotificationData(
        var body: String? = "",
        var title: String? = ""
)

data class Data(
        var wallet: String? = ""
)