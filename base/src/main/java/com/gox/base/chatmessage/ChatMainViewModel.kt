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