package com.gox.partner.views.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.gox.partner.R
import com.gox.partner.databinding.CountrySearchListItemBinding
import com.gox.partner.interfaces.CustomClickListner
import com.gox.partner.models.CountryResponseData
import java.io.Serializable


class CountryListAdapter(val activity: FragmentActivity?, val countryList: List<CountryResponseData>)
    : RecyclerView.Adapter<CountryListAdapter.MyViewHolder>(), Filterable {

    private var countrySearchList: List<CountryResponseData>? = null

    init {
        this.countrySearchList = countryList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<CountrySearchListItemBinding>(LayoutInflater.from(parent.context), R.layout.country_search_list_item, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = countrySearchList!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.currentOderItemlistBinding.countyName.setText(countrySearchList!!.get(position).country_name)



        holder.bind()
        holder.currentOderItemlistBinding.itemClickListener = object : CustomClickListner {
            override fun onListClickListner() {
                val intent = Intent()
                intent.putExtra("selected_list", countrySearchList!!.get(position) as Serializable)
                activity!!.setResult(Activity.RESULT_OK, intent)
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
                    countrySearchList = countryList
                } else {
                    val filteredList = ArrayList<CountryResponseData>()
                    for (row in countryList) {
                        if (row.country_name!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    countrySearchList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = countrySearchList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                countrySearchList = results?.values as ArrayList<CountryResponseData>
                notifyDataSetChanged()
            }

        }
    }

}