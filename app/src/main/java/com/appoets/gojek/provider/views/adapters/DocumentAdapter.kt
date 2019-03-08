package com.appoets.gojek.provider.views.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.appoets.gojek.provider.R

class DocumentAdapter(context: Context, serviceList:ArrayList<String> ):BaseAdapter(),View.OnClickListener{



    private var context:Context?=null
    private  var  serviceLists:ArrayList<String>?=null
    private  var inflater:LayoutInflater ?= null
    private  var selectedPosition:Int=0;


    init {
        this.context=context
        this.serviceLists=serviceLists
        this.inflater= LayoutInflater.from(context)
    }


    inner class  DocumentViewHolder(rView:View){
         val tvServiceName: TextView

        init {
            this.tvServiceName=rView.findViewById(R.id.tv_doc_service)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View?
        var vh: DocumentViewHolder?=null
        if (convertView == null) {
            view = this.inflater!!.inflate(R.layout.row_document, parent, false)
            vh = DocumentViewHolder(view)
            view.tag = position
        } else {
            view = convertView
        }

        vh!!.tvServiceName.text = serviceLists?.let { it.get(position)}
        if(selectedPosition==position){
           vh!!.tvServiceName.background=ContextCompat.getDrawable(context!!,R.drawable.bg_service_selected)
        }else{
            vh!!.tvServiceName.background=ContextCompat.getDrawable(context!!,R.drawable.bg_service_unselected)

        }
        return view!!
    }

    override fun getItem(position: Int): Any {
        return  serviceLists!!.get(position)
    }


    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun getCount(): Int {
     return  serviceLists!!.let { it.size }
    }

    override fun onClick(v: View?) {
        val position =v!!.tag as Int
        selectedPosition=position


    }

}
