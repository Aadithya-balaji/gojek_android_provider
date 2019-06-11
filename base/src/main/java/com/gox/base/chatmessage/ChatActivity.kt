package com.gox.base.chatmessage

import android.util.Base64
import android.util.Log
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.gox.base.BuildConfig.SALT_KEY
import com.gox.base.R
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
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

    private val userId = readPreferences("userId", 0)
    private val requestId = readPreferences("RequestId", 0)
    private val providerId = readPreferences("providerId", 0)
    private val adminServiceId = readPreferences("adminServiceId", 0)
    private val userFirstName = readPreferences("userFirstName", "")
    private val providerFirstName = readPreferences("providerFirstName", "")

    override fun getLayoutId() = R.layout.activity_chat_main

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityChatMainBinding
        mBinding.tvtoolBarText.text = getText(R.string.chat)
        mBinding.ivBack.setOnClickListener { finish() }

        decodeString = String(Base64.decode(SALT_KEY, Base64.DEFAULT), charset("UTF-8"))
        createRoomName()
        mChatSocketResponseList = ArrayList()
        mChatSocketResponseList!!.clear()
        mViewModel = ViewModelProviders.of(this).get(ChatMainViewModel::class.java)
        mViewModel?.getMessages("TRANSPORT", requestId!!)
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
//                chatMessageModel.user = chatRequestModel.userFirstName
//                chatMessageModel.provider = chatRequestModel.providerFirstName
//                chatMessageModel.message = message.toString()
//                mChatSocketResponseList!!.add(chatMessageModel)
//                mBinding.chatAdapter!!.notifyDataSetChanged()
                mBinding.messageInput.text.clear()
            }
        })

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        mBinding.messages.layoutManager = layoutManager
        mBinding.chatAdapter = ChatAdapter(this, mChatSocketResponseList!!)
        mBinding.chatAdapter!!.notifyDataSetChanged()
        mBinding.messages.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                mBinding.messages.postDelayed({
                    mBinding.messages.smoothScrollToPosition(
                            mBinding.messages.adapter!!.itemCount - 1)
                }, 100)
            }
        }

        SocketManager.emit(Constants.ROOM_NAME.JOIN_ROOM_NAME, roomName.toString())

        mBinding.sendButton.setOnClickListener {
            if (mBinding.messageInput.text.isNotEmpty()) {
                message = mBinding.messageInput.text.toString()
                chatRequestModel.requestId = requestId
                chatRequestModel.type = "provider"
                chatRequestModel.userFirstName = userFirstName
                chatRequestModel.providerFirstName = providerFirstName
                chatRequestModel.adminServiceId = adminServiceId
                chatRequestModel.message = message.toString()
                chatRequestModel.roomName = roomName

                val chatObject = JSONObject(Gson().toJson(chatRequestModel))
                SocketManager.emit(Constants.ROOM_NAME.CHATROOM, chatObject)
                mViewModel?.sendMessage(chatRequestModel)
            }
        }

        layoutManager.smoothScrollToPosition(mBinding.messages, null, mBinding.chatAdapter!!.itemCount)

        SocketManager.onEvent(Constants.ROOM_NAME.ON_MESSAGE_RECEIVE, Emitter.Listener {
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
                    Log.e("ChatActivity ", e.message)
                }
            }
        })
    }

    private fun createRoomName() {
        val roomPrefix = "room"
        roomName = roomPrefix + "_" + decodeString + "_" + requestId +
                "_" + userId + "_" + providerId + "_" + adminServiceId
        println("RRR :: roomName = $roomName")
    }
}