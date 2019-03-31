package com.xjek.provider.views.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.xjek.provider.R
import com.xjek.taxiservice.views.main.ActivityTaxiMain
import de.hdodenhof.circleimageview.CircleImageView

class HomeAdapter (activity: AppCompatActivity): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(),View.OnClickListener{

    private  var activity:AppCompatActivity?=null

    init{
        this.activity=activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.row_taxiprovider_item, parent, false)
        return HomeViewHolder(v)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        holder.tvAccept.setOnClickListener(this)

    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivProfile = itemView.findViewById(R.id.profile_image) as CircleImageView
        val tvUserName = itemView.findViewById(R.id.tv_user_name) as TextView
        val rbHome = itemView.findViewById(R.id.rb_home) as RatingBar
        val tvServiceType = itemView.findViewById(R.id.tv_service_type) as TextView
        val tvScheduledDate = itemView.findViewById(R.id.tv_schedule_date) as TextView
        val  tvReject=itemView.findViewById(R.id.tv_reject) as TextView
        val  tvAccept=itemView.findViewById(R.id.tv_approve) as TextView
        val tvStreetAddress=itemView.findViewById(R.id.tv_street_address) as TextView
        val tvCountry=itemView.findViewById(R.id.tv_country) as TextView
    }

    override fun onClick(v: View?) {
        val intent= Intent(activity, ActivityTaxiMain::class.java)
        activity!!.startActivity(intent)
    }
}