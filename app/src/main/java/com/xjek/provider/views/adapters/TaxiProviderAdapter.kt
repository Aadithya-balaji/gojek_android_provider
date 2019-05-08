package com.xjek.provider.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.RowTaxiproviderItemBinding
import com.xjek.taxiservice.views.main.TaxiDashboardActivity

class TaxiProviderAdapter(activity: FragmentActivity?) : RecyclerView.Adapter<TaxiProviderAdapter.MyViewHolder>() {
    val activity = activity
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val inflate = DataBindingUtil.inflate<RowTaxiproviderItemBinding>(LayoutInflater.from(parent.context), R.layout.row_taxiprovider_item, parent, false)
        return MyViewHolder(inflate)

    }

    override fun getItemCount(): Int = 1
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.rowTaxiproviderItemBinding.tvApprove.setOnClickListener() {
            activity!!.startActivity(Intent(activity, TaxiDashboardActivity::class.java))
        }
    }

    class MyViewHolder(itemView: RowTaxiproviderItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val rowTaxiproviderItemBinding = itemView

    }
}