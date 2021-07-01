package com.gox.taxiservice.locationpick

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.gox.base.data.PlacesModel
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.RowPlacesLayoutBinding
import com.gox.taxiservice.interfaces.CustomClickListener


class PlacesAdapter(private val placesList: ArrayList<PlacesModel>) :
        RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private var mOnViewAdapterClickListener: CustomClickListener? = null
    fun setOnClickListener(onViewClickListener: CustomClickListener) {
        this.mOnViewAdapterClickListener = onViewClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesAdapter.ViewHolder {
        return ViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
                R.layout.row_places_layout, parent, false))
    }

    override fun onBindViewHolder(holder: PlacesAdapter.ViewHolder, position: Int) {
        holder.bindItems(placesList[position])
        holder.itemView.setOnClickListener {
            if (mOnViewAdapterClickListener != null) mOnViewAdapterClickListener!!.onListClickListener(
                    position)
        }
    }

    override fun getItemCount(): Int {
        return placesList.size
    }

    class ViewHolder(itemView: RowPlacesLayoutBinding) : RecyclerView.ViewHolder(itemView.root) {
        fun bindItems(places: PlacesModel) {
            val area = itemView.findViewById(R.id.area) as TextView
            val address = itemView.findViewById(R.id.address) as TextView

            area.text = places.mPrimary
            address.text = places.mSecondary

        }
    }
}