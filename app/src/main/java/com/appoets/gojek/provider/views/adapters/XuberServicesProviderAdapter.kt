package com.appoets.gojek.provider.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.RowXuberserviceProviderItemBinding
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity

class XuberServicesProviderAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<XuberServicesProviderAdapter.MyViewHolder>() {

    val activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<RowXuberserviceProviderItemBinding>(LayoutInflater.from(parent.context), R.layout.row_xuberservice_provider_item, parent, false)
        return MyViewHolder(inflate)

    }

    override fun getItemCount(): Int = 1
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.rowTaxiproviderItemBinding.tvApprove.setOnClickListener() {
            activity!!.startActivity(Intent(activity, XuberMainActivity::class.java))
        }
    }

    class MyViewHolder(itemView: RowXuberserviceProviderItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val rowTaxiproviderItemBinding = itemView

    }
}