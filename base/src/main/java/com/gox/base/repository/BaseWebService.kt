package com.gox.base.repository

import com.gox.base.chatmessage.SingleMessageResponse
import com.gox.base.chatmessage.ChatMessageList
import io.reactivex.Observable
import retrofit2.http.*

interface BaseWebService {

    @FormUrlEncoded
    @POST("provider/chat")
    fun sendMessage(@Header("Authorization") token: String,
                    @FieldMap params: java.util.HashMap<String, Any>
    ): Observable<SingleMessageResponse>

    @GET("provider/chat")
    fun getMessages(@Header("Authorization") token: String,
                    @Query("admin_service") adminService: String,
                    @Query("id") id: Int
    ): Observable<ChatMessageList>

}