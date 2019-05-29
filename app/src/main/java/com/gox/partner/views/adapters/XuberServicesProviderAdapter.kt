package com.gox.partner.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.RowXuberserviceProviderItemBinding

class XuberServicesProviderAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<XuberServicesProviderAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<RowXuberserviceProviderItemBinding>(LayoutInflater.from(parent.context), R.layout.row_xuberservice_provider_item, parent, false)
        return MyViewHolder(inflate)

    }

    override fun getItemCount(): Int = 1
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


    }

    class MyViewHolder(itemView: RowXuberserviceProviderItemBinding) : RecyclerView.ViewHolder(itemView.root) {


    }
}