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
import com.gox.partner.interfaces.CustomClickListener
import com.gox.partner.models.City
import java.io.Serializable

class CityListAdapter(val activity: FragmentActivity?, val cityList: List<City>)
    : RecyclerView.Adapter<CityListAdapter.MyViewHolder>(), Filterable {

    private var citySearchList: List<City>? = null

    init {
        this.citySearchList = cityList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.country_search_list_item, parent, false))
    }

    override fun getItemCount(): Int = citySearchList!!.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.mBinding.countyName.text = citySearchList!![position].city_name
        holder.bind()
        holder.mBinding.itemClickListener = object : CustomClickListener {
            override fun onListClickListener() {
                val intent = Intent()
                intent.putExtra("selected_list", citySearchList!!.get(position) as Serializable)
                activity!!.setResult(Activity.RESULT_OK, intent)
                activity.finish()
            }
        }
    }

    inner class MyViewHolder(itemView: CountrySearchListItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        val mBinding = itemView
        fun bind() {

        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) citySearchList = cityList else {
                    val filteredList = ArrayList<City>()
                    for (row in cityList) if (row.city_name.toLowerCase().contains(charString.toLowerCase()))
                        filteredList.add(row)
                    citySearchList = filteredList
                }
                val filterResults = FilterResults()
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