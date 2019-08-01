package com.gox.partner.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.RowTaxiproviderItemBinding
import com.gox.taxiservice.views.main.TaxiDashboardActivity

class TaxiProviderAdapter(val activity: FragmentActivity?) : RecyclerView.Adapter<TaxiProviderAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.row_taxiprovider_item, parent, false))
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mBinding.tvApprove.setOnClickListener {
            activity!!.startActivity(Intent(activity, TaxiDashboardActivity::class.java))
        }
    }

    class MyViewHolder(itemView: RowTaxiproviderItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
    }
}