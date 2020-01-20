package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.RowSavedDetailBinding
import com.gox.partner.models.CardResponseModel
import com.gox.partner.views.account_card.CardListViewModel

class CardsAdapter(context: Context, cardList: MutableList<CardResponseModel>, cardListModel: CardListViewModel)
    : RecyclerView.Adapter<CardsAdapter.CardViewHolder>(), View.OnClickListener {

    private var context: Context? = null
    private var cardList: MutableList<CardResponseModel>? = null
    private var cardListViewModel: CardListViewModel? = null
    private var selectedPosition: Int? = -1

    init {
        this.context = context
        this.cardList = cardList
        this.cardListViewModel = cardListModel
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            CardViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_saved_detail, parent, false))

    override fun getItemCount() = cardList!!.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.mBinding.tvCardType.text = cardList!!.get(position).getBrand()
        holder.mBinding.tvCardNumber.text = String.format(context!!.resources.getString(R.string.row_card_number), cardList!![position].getLastFour())
        if (selectedPosition == position && cardList!!.get(position).isCardSelected == false) selectedPosition = -1
        if (cardList!![position].isCardSelected == false) {
            holder.mBinding.cvSavedCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.bg_dispute_closed))
            holder.mBinding.tvCardNumber.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            holder.mBinding.tvCardType.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        } else {
            holder.mBinding.cvSavedCard.setCardBackgroundColor(ContextCompat.getColor(context!!, R.color.colorAccent))
            holder.mBinding.tvCardNumber.setTextColor(ContextCompat.getColor(context!!, R.color.white))
            holder.mBinding.tvCardType.setTextColor(ContextCompat.getColor(context!!, R.color.white))
        }
        holder.mBinding.root.setOnClickListener(this)
        holder.mBinding.root.tag = holder
    }

    inner class CardViewHolder(val mBinding: RowSavedDetailBinding) : RecyclerView.ViewHolder(mBinding.root)

    override fun onClick(v: View?) {
        val cardViewHolder = v!!.tag as CardViewHolder
        val position = cardViewHolder.adapterPosition
        if (selectedPosition != position) {
            selectedPosition = position
            val cardResponseModel = cardList!!.get(selectedPosition!!)
            cardListViewModel!!.navigator.cardPicked(cardResponseModel.getCardId().toString(), cardResponseModel.getId().toString(), selectedPosition!!)
        }
    }

    fun addItem(item: CardResponseModel) {
        cardList!!.add(item)
        notifyItemChanged(cardList!!.size)
    }
}