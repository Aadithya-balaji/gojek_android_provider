package com.xjek.provider.views.adapters

import android.app.Activity
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
import com.xjek.provider.models.City
import java.io.Serializable

class CityListAdapter(val activity: FragmentActivity?, val citylist: List<City>) : RecyclerView.Adapter<CityListAdapter.MyViewHolder>(), Filterable {

    private var citySearchList: List<City>? = null

    init {
        this.citySearchList = citylist
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate = DataBindingUtil.inflate<CountrySearchListItemBinding>(LayoutInflater.from(parent.context), R.layout.country_search_list_item, parent, false)
        return MyViewHolder(inflate)
    }

    override fun getItemCount(): Int = citySearchList!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.currentOderItemlistBinding.countyName.setText(citySearchList!![position].city_name)
        holder.bind()
        holder.currentOderItemlistBinding.itemClickListener = object : CustomClickListner {
            override fun onListClickListner() {
                val intent = Intent()
                intent.putExtra("selected_list", citySearchList!!.get(position) as Serializable)
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
                    citySearchList = citylist
                } else {
                    val filteredList = ArrayList<City>()
                    for (row in citylist) {
                        if (row.city_name!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    citySearchList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = citySearchList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                citySearchList = results?.values as ArrayList<City>
                notifyDataSetChanged()
            }

        }
    }


}