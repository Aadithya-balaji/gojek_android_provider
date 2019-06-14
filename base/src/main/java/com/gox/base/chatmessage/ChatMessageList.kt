package com.gox.base.chatmessage

import com.google.gson.annotations.SerializedName

class ChatMessageList {
    var statusCode: String? = null
    var title: String? = null
    var message: String? = null
    var responseData: List<ResponseData>? = null
}

class ResponseData {
    var id: Int? = null
    @SerializedName("request_id")
    var requestId: Int? = null
    @SerializedName("admin_service_id")
    var adminServiceId: Int? = null
    @SerializedName("data")
    var chatSocketResponseModel: List<ChatSocketResponseModel>? = null

}
