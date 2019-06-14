package com.gox.base.repository

import com.gox.base.chatmessage.ChatMessageList
import com.gox.base.chatmessage.SingleMessageResponse
import io.reactivex.Observable
import retrofit2.http.*

interface BaseWebService {

    @FormUrlEncoded
    @POST("provider/chat")
    fun sendMessage(@FieldMap params: java.util.HashMap<String, Any>):
            Observable<SingleMessageResponse>

    @GET("provider/chat")
    fun getMessages(@Query("admin_service") adminService: String,
                    @Query("id") id: Int
    ): Observable<ChatMessageList>

}