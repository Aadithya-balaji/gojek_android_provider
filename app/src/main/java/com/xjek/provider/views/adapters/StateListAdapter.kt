package com.xjek.provider.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.provider.databinding.CountrySearchListItemBinding
import com.xjek.provider.interfaces.CustomClickListner
import com.xjek.provider.models.StateListResponseData
import java.io.Serializable

class StateListAdapter(val activity: FragmentActivity?, val stateList: List<StateListResponseData>)
    : RecyclerView.Adapter<StateListAdapter.MyViewHolder>(), Filterable {

    private var stateSearchList: List<StateListResponseData>? = null

    init {
        this.stateSearchList = stateList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<CountrySearchListItemBinding>(LayoutInflater.from(parent.context), R.layout.country_search_list_item, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = stateSearchList!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        holder.currentOderItemlistBinding.countyName.setText(stateSearchList!!.get(position).state_name)

        holder.bind()
        holder.currentOderItemlistBinding.itemClickListener = object : CustomClickListner {
            override fun onListClickListner() {

                val intent = Intent()
                intent.putExtra("selected_list", stateSearchList!!.get(position) as Serializable)
                activity!!.setResult(101, intent)
                activity!!.finish()
            }

        }
    }


    inner class MyViewHolder(itemView: CountrySearchListItemBinding) : RecyclerView.ViewHolder(itemView.root) {

        val currentOderItemlistBinding = itemView

        fun bind() {

        }


    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    stateSearchList = stateList
                } else {
                    val filteredList = ArrayList<StateListResponseData>()
                    for (row in stateList) {
                        if (row.state_name!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    stateSearchList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = stateSearchList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                stateSearchList = results?.values as ArrayList<StateListResponseData>
                notifyDataSetChanged()
            }

        }
    }
}


