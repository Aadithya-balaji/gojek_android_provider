package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.RowSavedDetailBinding
import com.xjek.provider.models.CardResponseModel

class CardsAdapter(context: Context, cardList: List<CardResponseModel>) : RecyclerView.Adapter<CardsAdapter.CardViewHolder>() {

    private var context: Context? = null
    private var cardList: List<CardResponseModel>? = null

    init {
        this.context = context
        this.cardList = cardList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflate = DataBindingUtil.inflate<RowSavedDetailBinding>(LayoutInflater.from(parent.context), R.layout.row_saved_detail, parent, false)
        return CardViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return  cardList!!.size
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.cardViewBinding.tvCardType.setText(cardList!!.get(position).getBrand())
        holder.cardViewBinding.tvCardNumber.setText(String.format(context!!.resources.getString(R.string.card_number),cardList!!.get(position).getLastFour()))
    }


    inner class CardViewHolder(cardViewBinding: RowSavedDetailBinding) : RecyclerView.ViewHolder(cardViewBinding.root) {
        val cardViewBinding = cardViewBinding
    }
}