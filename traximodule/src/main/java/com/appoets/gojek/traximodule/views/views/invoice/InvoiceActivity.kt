package com.appoets.gojek.traximodule.views.views.invoice

import android.app.Activity
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.view.menu.ActionMenuItem
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.traximodule.R
import com.appoets.gojek.traximodule.databinding.ActivityInvoiceBinding
import com.appoets.gojek.traximodule.views.views.rating.RatingFragment


class  InvoiceActivity : BaseActivity<ActivityInvoiceBinding>(),InvoiceNavigator{
    private  lateinit var  rlRideCompleted:RelativeLayout
    private  lateinit var  rlRideInProgress:RelativeLayout

    override fun openRatingDialog() {
        val ratingFragment=RatingFragment()
        ratingFragment.show(supportFragmentManager,"rating")
        ratingFragment.isCancelable=false
    }

    private  var activityInvoiceBinding:ActivityInvoiceBinding?=null

    override fun getLayoutId(): Int {
        return  R.layout.activity_invoice
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityInvoiceBinding=mViewDataBinding as ActivityInvoiceBinding
        val invoiceModule=InvoiceModule()
        invoiceModule.setNavigator(this)
        activityInvoiceBinding!!.invoicemodel=invoiceModule

        //initViews
        rlRideCompleted=findViewById(R.id.rl_status_unselected)
        rlRideInProgress=findViewById(R.id.rl_status_selected)

        rlRideInProgress.visibility= View.GONE
        rlRideCompleted.visibility=View.VISIBLE

    }

}