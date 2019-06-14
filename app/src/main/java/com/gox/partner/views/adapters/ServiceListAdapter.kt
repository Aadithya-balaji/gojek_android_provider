package com.gox.partner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gox.partner.R

class ServiceListAdapter(appCompatActivity: AppCompatActivity) : BaseAdapter() {

    private var serviceList: List<String>? = null
    private var appCompatActivity: AppCompatActivity? = null
    private var inflater: LayoutInflater? = null

    init {
        this.appCompatActivity = appCompatActivity
        this.inflater = LayoutInflater.from(appCompatActivity)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        inflater = parent?.context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater!!.inflate(R.layout.row_services, null)
        view.findViewById(R.id.tv_service_type) as TextView
        return view
    }

    override fun getItem(position: Int) = serviceList!![position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = serviceList!!.size
}