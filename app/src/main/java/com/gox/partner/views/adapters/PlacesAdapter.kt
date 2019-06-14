package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import com.gox.partner.R
import com.gox.partner.models.PlaceResponseModel

class PlacesAdapter(private var context: Context,
                    isCountry: Boolean,
                    isCity: Boolean,
                    isState: Boolean,
                    placesList: ArrayList<PlaceResponseModel>) : BaseAdapter(), Filterable {

    private var mInflater = LayoutInflater.from(this.context)
    private var orignalPlaceList = placesList
    private var filteredData: ArrayList<PlaceResponseModel>
    private var mFilter: ItemFilter
    private var isCountry: Boolean = false
    private var isCity: Boolean = false
    private var isState: Boolean = false

    init {
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
        when {
            isCountry -> holder.text!!.text = filteredData.get(position).countryName.toString()
            isState -> holder.text!!.text = filteredData.get(position).stateName.toString()
            isCity -> holder.text!!.text = filteredData.get(position).cityName.toString()
        }

        return convertView!!
    }

    override fun getItem(position: Int) = filteredData[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = filteredData.size

    override fun getFilter() = mFilter

    internal class ViewHolder {
        var text: TextView? = null
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterString = constraint.toString().toLowerCase()

            val results = FilterResults()

            val list = orignalPlaceList

            val count = list.size
            val mList = ArrayList<PlaceResponseModel>(count)
            var filterableString = ""
            for (i in 0 until count) {
                when {
                    isCountry -> filterableString = list.get(i).countryName.toString()
                    isState -> filterableString = list.get(i).stateName.toString()
                    isCity -> filterableString = list.get(i).cityName.toString()
                }
                if (filterableString.toLowerCase().contains(filterString)) mList.add(list.get(i))
            }

            results.values = mList
            results.count = mList.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredData = results!!.values as java.util.ArrayList<PlaceResponseModel>
            notifyDataSetChanged()
        }
    }
}
