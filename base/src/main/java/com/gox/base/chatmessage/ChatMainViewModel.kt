package com.gox.base.chatmessage

import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseViewModel
import com.gox.base.repository.ApiListener
import com.gox.base.repository.BaseModuleRepository

class ChatMainViewModel : BaseViewModel<ChatNavigator>() {

    private val mRepository = BaseModuleRepository.instance()
    var errorResponse = MutableLiveData<String>()
    var getChatMessageList = MutableLiveData<ChatMessageList>()
    val singleMessageResponse = MutableLiveData<SingleMessageResponse>()

    fun sendMessage(chatRequestModel: ChatRequestModel) {
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["admin_service_id"] = chatRequestModel.adminServiceId!!
        hashMap["id"] = chatRequestModel.requestId.toString()
        hashMap["provider_name"] = chatRequestModel.providerFirstName.toString()
        hashMap["user_name"] = chatRequestModel.userFirstName.toString()
        hashMap["message"] = chatRequestModel.message.toString()
        hashMap["type"] = chatRequestModel.type.toString()

        getCompositeDisposable().add(mRepository.sendMessage(object : ApiListener {
            override fun success(successData: Any) {
                singleMessageResponse.value = successData as SingleMessageResponse
            }

            override fun fail(failData: Throwable) {
                errorResponse.postValue(getErrorMessage(failData))
            }
        }, hashMap))
    }

    fun getMessages(admin_service: String, id: Int) {
        getCompositeDisposable().add(mRepository.getMessage(object : ApiListener {
            override fun success(successData: Any) {
                getChatMessageList.value = successData as ChatMessageList
            }

            override fun fail(failData: Throwable) {
                errorResponse.postValue(getErrorMessage(failData))
            }
        }, admin_service, id))
    }
}