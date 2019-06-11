package com.gox.base.chatmessage

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gox.base.R

class ChatAdapter(private var mContext: Context, private var mChatSocketResponseList: ArrayList<ChatSocketResponseModel>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ChatMessageModel.USER -> UserViewHolder(LayoutInflater.from(viewGroup.context)
                    .inflate(R.layout.row_message_layout_user, viewGroup, false))
            ChatMessageModel.PROVIDER -> ProviderViewHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.row_message_layout_provider, viewGroup, false))
            else -> Holder(View(mContext))
        }
    }

    private inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val userMessageTv = itemView.findViewById(R.id.tvMessageUser) as TextView
    }

    private inner class ProviderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val providerMessageTv = itemView.findViewById(R.id.tvMessageProvider) as TextView
    }

    private inner class Holder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount() = mChatSocketResponseList.size

    override fun getItemViewType(position: Int): Int {
        val model = mChatSocketResponseList[position]
        return when {
            model.type == "user" -> ChatMessageModel.USER
            model.type == "provider" -> ChatMessageModel.PROVIDER
            else -> 0
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val model = mChatSocketResponseList[viewHolder.adapterPosition]
        if (viewHolder is UserViewHolder) setUserMessage(model.message, viewHolder)
        if (viewHolder is ProviderViewHolder) setProviderMessage(model.message, viewHolder)
    }

    private fun setUserMessage(message: String?, viewHolder: UserViewHolder) {
        viewHolder.userMessageTv.text = message
    }

    private fun setProviderMessage(message: String?, viewHolder: ProviderViewHolder) {
        viewHolder.providerMessageTv.text = message
    }
}