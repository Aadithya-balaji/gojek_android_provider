package com.xjek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.xjek.provider.R
import com.xjek.provider.model.CountryModel
import com.xjek.provider.model.ciity.PlaceResponseModel
import java.util.ArrayList

class CountryAdapter(val ctxt: Context, val placesList: List<CountryModel>) : BaseAdapter(), Filterable {
    private lateinit var mInflater: LayoutInflater
    private lateinit var orignalPlaceList: List<CountryModel>
    private lateinit var context: Context
    private lateinit var filteredData: List<CountryModel>
    private lateinit var mFilter: ItemFilter
    private var isCountry: Boolean = false
    private var isCity: Boolean = false
    private var isState: Boolean = false


    init {
        this.context = ctxt
        this.orignalPlaceList = placesList
        mInflater = LayoutInflater.from(context)
        this.filteredData = placesList
        mFilter = ItemFilter()
        this.isCountry = isCountry
        this.isState = isState
        this.isCity = isCity
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // A ViewHolder keeps references to children views to avoid unnecessary calls
        // to findViewById() on each row.
        val holder: ViewHolder
        var convertView = convertView
        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_country_item, null)

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = ViewHolder()
            holder.tvCountryCode = convertView.findViewById(R.id.tv_code) as TextView
            holder.tvCountryName=convertView.findViewById(R.id.tv_country_name) as TextView
            holder.ivCountryFlag=convertView.findViewById(R.id.iv_flag) as ImageView

            // Bind the data efficiently with the holder.

            convertView.tag = holder
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = convertView.tag as ViewHolder
        }

        // If weren't re-ordering this you could rely on what you set last time
       holder.tvCountryName!!.setText(filteredData.get(position).name.toString())
        holder.tvCountryCode!!.setText("+"+filteredData.get(position).dialCode.toString())
        holder.ivCountryFlag!!.setImageResource(filteredData.get(position).flag)


        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return filteredData.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return filteredData.size
    }


    override fun getFilter(): Filter {
        return mFilter
    }

    internal class ViewHolder {
        var tvCountryCode: TextView? = null
        var  tvCountryName:TextView?=null
        var ivCountryFlag:ImageView?=null
    }


    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterString = constraint.toString().toLowerCase()

            val results = Filter.FilterResults()

            val list = orignalPlaceList

            val count = list.size
            val nlist = ArrayList<CountryModel>(count)
            var filterableString: String=""
            for (i in 0 until count) {

                    filterableString = list.get(i).name.toString()
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i))
                }
            }

            results.values = nlist
            results.count = nlist.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredData = results!!.values as java.util.ArrayList<CountryModel>
            notifyDataSetChanged()
        }

    }
}
