package com.appoets.gojek.provider.views.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.appoets.gojek.provider.R
import android.app.Activity




class AccountAdapter(var cont: Context, val titleList: Array<String>, val iconsList:IntArray):BaseAdapter(){



    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        //val view = LayoutInflater.from(parent.context).inflate(com.appoets.gojek.provider.R.layout.row_account, parent, false)
        val inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.row_account,null)
        var tvTitle=view.findViewById(com.appoets.gojek.provider.R.id.tv_account_title) as TextView
        var ivAccount: ImageView =view.findViewById(com.appoets.gojek.provider.R.id.iv_account) as ImageView
        tvTitle.setText(titleList.get(position).toString())
        when(position){
            0 -> {
                ivAccount.setImageResource(R.drawable.ic_profile)

            }

            1 -> {
                ivAccount.setImageResource(R.drawable.ic_gift_card)
            }

            2 -> {
                ivAccount.setImageResource(R.drawable.card)
            }

            3 -> {
                ivAccount.setImageResource(R.drawable.ic_privacy_policy)

            }
            4 -> {
                ivAccount.setImageResource(R.drawable.ic_support)
            }
            5 -> {
                ivAccount.setImageResource(R.drawable.card)

            }

        }

        return view
    }

    override fun getItem(position: Int): Any? {
        return  null
    }

    override fun getItemId(position: Int): Long {
        return  0
    }

    override fun getCount(): Int {
        return  titleList.size
    }

}
