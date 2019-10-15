package com.gox.base.chatmessage

import android.util.Base64
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.gox.base.BuildConfig.BASE_URL
import com.gox.base.BuildConfig.SALT_KEY
import com.gox.base.R
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.databinding.ActivityChatMainBinding
import com.gox.base.extensions.readPreferences
import com.gox.base.socket.SocketManager
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject

class ChatActivity : BaseActivity<ActivityChatMainBinding>() {

    private lateinit var mBinding: ActivityChatMainBinding
    private var mViewModel: ChatMainViewModel? = null
    private var mChatSocketResponseList: ArrayList<ChatSocketResponseModel>? = null
    private val chatRequestModel = ChatRequestModel()
    private var message: String? = null
    private var roomName: String? = null
    private var decodeString: String? = null

    private val userId = readPreferences(Constants.Chat.USER_ID, 0)
    private val requestId = readPreferences(Constants.Chat.REQUEST_ID, 0)
    private val providerId = readPreferences(Constants.Chat.PROVIDER_ID, 0)

    private val userName = readPreferences(Constants.Chat.USER_NAME, "")
    private val serviceType = readPreferences(Constants.Chat.ADMIN_SERVICE, "")
    private val providerName = readPreferences(Constants.Chat.PROVIDER_NAME, "")
    private val adminService = readPreferences(Constants.Chat.ADMIN_SERVICE, "")

    override fun getLayoutId() = R.layout.activity_chat_main

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityChatMainBinding
        mBinding.tvtoolBarText.text = getText(R.string.chat)

        mBinding.ivBack.setOnClickListener { finish() }

        decodeString = String(Base64.decode(SALT_KEY, Base64.DEFAULT), charset("UTF-8"))
        mChatSocketResponseList = ArrayList()
        mChatSocketResponseList!!.clear()
        mViewModel = ViewModelProviders.of(this).get(ChatMainViewModel::class.java)
        mViewModel?.getMessages(serviceType!!, requestId!!)
        mViewModel?.getChatMessageList?.observe(this, Observer {
            if (it != null) if (it.statusCode == "200") {
                mChatSocketResponseList!!.clear()
                mChatSocketResponseList!!.addAll(it.responseData!![0].chatSocketResponseModel!!)
                mBinding.chatAdapter!!.notifyDataSetChanged()
            }
        })

        createRoomName()

        mViewModel?.singleMessageResponse?.observe(this, Observer {
            if (it != null) if (it.statusCode == "200") {
//                val chatMessageModel = ChatSocketResponseModel()
//                chatMessageModel.type = chatRequestModel.type
//                chatMessageModel.user = chatRequestModel.userName
//                chatMessageModel.provider = chatRequestModel.providerName
//                chatMessageModel.message = message.toString()
//                mChatSocketResponseList!!.add(chatMessageModel)
//                mBinding.chatAdapter!!.notifyDataSetChanged()
                mBinding.messageInput.text?.clear()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        mBinding.messages.layoutManager = layoutManager
        mBinding.chatAdapter = ChatAdapter(this, mChatSocketResponseList!!)
        mBinding.chatAdapter!!.notifyDataSetChanged()
        mBinding.messages.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) mBinding.messages.postDelayed({
                mBinding.messages.smoothScrollToPosition(
                        mBinding.messages.adapter!!.itemCount - 1)
            }, 100)
        }

        SocketManager.emit(Constants.RoomName.JOIN_ROOM_NAME, roomName.toString())

        mBinding.sendButton.setOnClickListener {
            if (mBinding.messageInput.text!!.isNotEmpty()) {
                message = mBinding.messageInput.text.toString()
                chatRequestModel.roomName = roomName!!
                chatRequestModel.url = "${Constants.BaseUrl.APP_BASE_URL}/chat"
                chatRequestModel.saltKey = SALT_KEY
                chatRequestModel.requestId = requestId!!
                chatRequestModel.adminService = adminService!!
                chatRequestModel.type = "provider"
                chatRequestModel.userName = userName!!
                chatRequestModel.providerName = providerName!!
                chatRequestModel.message = message.toString()

                val chatObject = JSONObject(Gson().toJson(chatRequestModel))
                SocketManager.emit(Constants.RoomName.CHATROOM, chatObject)
                mBinding.messageInput.text?.clear()
//                mViewModel?.sendMessage(chatRequestModel)
            }
        }

        layoutManager.smoothScrollToPosition(mBinding.messages, null, mBinding.chatAdapter!!.itemCount)

        SocketManager.onEvent(Constants.RoomName.ON_MESSAGE_RECEIVE, Emitter.Listener {
            runOnUiThread {
                val chatMessageModel = ChatSocketResponseModel()
                val data1 = it[0] as JSONObject
                try {
                    chatMessageModel.type = data1.getString("type")
                    chatMessageModel.message = data1.getString("message")
                    chatMessageModel.user = data1.getString("user")
                    chatMessageModel.provider = data1.getString("provider")
                    mChatSocketResponseList!!.add(chatMessageModel)
                    mBinding.chatAdapter!!.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun createRoomName() {
//        room_1_R<RideId>_U<UserId>_P<ProviderId>_TRANSPORT
        val roomPrefix = "room"
        roomName = roomPrefix + "_" + decodeString + "_R" + requestId +
                "_U" + userId + "_P" + providerId + "_" + adminService
        println("RRR :: roomName = $roomName")
    }
}