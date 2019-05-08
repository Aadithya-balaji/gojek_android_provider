package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.models.CardResponseModel
import com.xjek.provider.views.wallet.WalletViewModel

class CardsAdapter(context: Context, cardList: MutableList<CardResponseModel>, walletViewModel: WalletViewModel) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>(), View.OnLongClickListener, View.OnClickListener {

    private var context: Context? = null
    private var cardList: MutableList<CardResponseModel>? = null
    private var walletViewModel: WalletViewModel? = null
    private var selectedPosition: Int? = -1

    init {
        this.context = context
        this.cardList = cardList
        this.walletViewModel = walletViewModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflate = DataBindingUtil.inflate<RowSavedDetailBinding>(LayoutInflater.from(parent.context), R.layout.row_saved_detail, parent, false)
        return CardViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return cardList!!.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.cardViewBinding.tvCardType.setText(cardList!!.get(position).getBrand())
        holder.cardViewBinding.tvCardNumber.setText(String.format(context!!.resources.getString(R.string.row_card_number), cardList!!.get(position).getLastFour()))
        holder.cardViewBinding.root
        if (selectedPosition == position && cardList!!.get(position).isCardSelected == false) {
            selectedPosition = -1
        }
        if (cardList!!.get(position).isCardSelected == false) {
            holder.cardViewBinding.root.setBackgroundColor(ContextCompat.getColor(context!!, R.color.card_unselected))
            holder.cardViewBinding.tvCardNumber.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            holder.cardViewBinding.tvCardType.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        } else {
            holder.cardViewBinding.root.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
            holder.cardViewBinding.tvCardNumber.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            holder.cardViewBinding.tvCardType.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        }

        holder.cardViewBinding.root.setOnClickListener(this)
        holder.cardViewBinding.root.tag = holder
        // holder.cardViewBinding.root.setOnLongClickListener(this)

    }


    inner class CardViewHolder(cardViewBinding: RowSavedDetailBinding) : RecyclerView.ViewHolder(cardViewBinding.root) {
        val cardViewBinding = cardViewBinding
    }

    override fun onLongClick(v: View?): Boolean {
        /*var cardViewHolder = v!!.tag as CardViewHolder
        var position = cardViewHolder.adapterPosition
        if (selectedPosition != position) {
            selectedPosition=position
            val cardResponseModel=cardList!!.get(selectedPosition!!)
            walletViewModel!!.navigator.cardPicked(cardResponseModel.getCardId().toString(),cardResponseModel.getId().toString(), selectedPosition!!)
        }*/
        return true
    }

    override fun onClick(v: View?) {
        var cardViewHolder = v!!.tag as CardViewHolder
        var position = cardViewHolder.adapterPosition
        if (selectedPosition != position) {
            selectedPosition = position
            val cardResponseModel = cardList!!.get(selectedPosition!!)
            walletViewModel!!.navigator.cardPicked(cardResponseModel.getCardId().toString(), cardResponseModel.getId().toString(), selectedPosition!!)
        }
    }
}