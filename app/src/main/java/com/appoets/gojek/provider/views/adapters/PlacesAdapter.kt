package com.appoets.gojek.provider.views.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.model.ciity.PlaceResponseModel

class PlacesAdapter(val ctxt: Context, isCountry: Boolean, isCity: Boolean, isState: Boolean, val placesList: ArrayList<PlaceResponseModel>) : BaseAdapter(), Filterable {
    private lateinit var mInflater: LayoutInflater
    private lateinit var orignalPlaceList: ArrayList<PlaceResponseModel>
    private lateinit var context: Context
    private lateinit var filteredData: ArrayList<PlaceResponseModel>
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
            convertView = mInflater.inflate(R.layout.list_item, null)

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = ViewHolder()
            holder.text = convertView.findViewById<View>(R.id.list_view) as TextView

            // Bind the data efficiently with the holder.

            convertView.tag = holder
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = convertView.tag as ViewHolder
        }

        // If weren't re-ordering this you could rely on what you set last time
        if ( isCountry) {
            holder.text!!.setText(filteredData.get(position).countryName.toString())
        }else if(isState){
            holder.text!!.setText(filteredData.get(position).stateName.toString())

        }else if (isCity) {

            holder.text!!.setText(filteredData.get(position).cityName.toString())
        }


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
        var text: TextView? = null
    }


    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterString = constraint.toString().toLowerCase()

            val results = Filter.FilterResults()

            val list = orignalPlaceList

            val count = list.size
            val nlist = ArrayList<PlaceResponseModel>(count)
            var filterableString: String=""
            for (i in 0 until count) {
                if(isCountry) {
                    filterableString = list.get(i).countryName.toString()
                }else if(isState){
                    filterableString = list.get(i).stateName.toString()

                }else if(isCity){
                    filterableString = list.get(i).cityName.toString()

                }
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i))
                }
            }

            results.values = nlist
            results.count = nlist.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredData = results!!.values as java.util.ArrayList<PlaceResponseModel>
            notifyDataSetChanged()
        }

    }
}
