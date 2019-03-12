package com.appoets.gojek.provider.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.appoets.gojek.provider.R
import org.w3c.dom.Text

class ServiceListAdapter(appCompatActivity: AppCompatActivity, servicesList: List<String>) : BaseAdapter() {

    private var serviceList: List<String>? = null
    private var appCompatActivity: AppCompatActivity? = null
    private var inflater: LayoutInflater? = null
    private var previousSelectedPosition = -1


    init {
        this.serviceList = serviceList
        this.appCompatActivity = appCompatActivity
        this.inflater = LayoutInflater.from(appCompatActivity)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.row_services, null)
        val tvService = view.findViewById(R.id.tv_service_type) as TextView
        return view
    }

    override fun getItem(position: Int): Any {
        return serviceList!!.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return serviceList!!.size
    }

}
