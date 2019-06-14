package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.gox.partner.R
import com.gox.partner.models.CountryModel
import java.util.*

class CountryAdapter(private var context: Context, placesList: List<CountryModel>) : BaseAdapter(), Filterable {

    private var mInflater = LayoutInflater.from(this.context)
    private var originalPlaceList = placesList
    private var filteredData: List<CountryModel>
    private var mFilter: ItemFilter

    init {
        this.filteredData = placesList
        mFilter = ItemFilter()
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
            holder.tvCountryName = convertView.findViewById(R.id.tv_country_name) as TextView
            holder.ivCountryFlag = convertView.findViewById(R.id.iv_flag) as ImageView

            // Bind the data efficiently with the holder.

            convertView.tag = holder
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = convertView.tag as ViewHolder
        }

        // If weren't re-ordering this you could rely on what you set last time
        holder.tvCountryName!!.text = filteredData[position].name
        holder.tvCountryCode!!.text = filteredData[position].dialCode
        holder.ivCountryFlag!!.setImageResource(filteredData[position].flag)

        return convertView!!
    }

    override fun getItem(position: Int) = filteredData[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = filteredData.size

    override fun getFilter() = mFilter

    internal class ViewHolder {
        var tvCountryCode: TextView? = null
        var tvCountryName: TextView? = null
        var ivCountryFlag: ImageView? = null
    }

    inner class ItemFilter : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {

            val filterString = constraint.toString().toLowerCase()

            val results = FilterResults()

            val list = originalPlaceList

            val count = list.size
            val mList = ArrayList<CountryModel>(count)
            var filterableString: String
            for (i in 0 until count) {
                filterableString = list[i].name
                if (filterableString.toLowerCase().contains(filterString)) mList.add(list[i])
            }

            results.values = mList
            results.count = mList.size
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filteredData = results!!.values as ArrayList<CountryModel>
            notifyDataSetChanged()
        }
    }
}
