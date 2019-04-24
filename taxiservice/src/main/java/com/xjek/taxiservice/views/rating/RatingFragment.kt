package com.xjek.taxiservice.views.rating

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseDialogFragment
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.FragmentRatingBinding

class RatingFragment:BaseDialogFragment<FragmentRatingBinding>(), RatingNavigator {

     private  var  appCompatActivity:AppCompatActivity?=null
    private  var fragmentRatingBinding:FragmentRatingBinding?=null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        appCompatActivity=context as AppCompatActivity
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    override fun goToAppModule() {
        val intent = Intent(appCompatActivity,Class.forName("com.appoets.gojek.provider.views.dashboard.DashBoardActivity"))
        appCompatActivity!!.startActivity(intent)
        appCompatActivity!!.finish()
    }

    override fun getLayout(): Int {
        return R.layout.fragment_rating
    }

    override fun initView(viewDataBinding: ViewDataBinding,view: View) {
        fragmentRatingBinding=viewDataBinding as FragmentRatingBinding
        val ratingViewModel= RatingViewModel()
        fragmentRatingBinding!!.ratingmodel=ratingViewModel
    }
}

