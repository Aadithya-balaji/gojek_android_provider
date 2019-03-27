package com.appoets.taxiservice.views.invoice

import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.taxiservice.R
import com.appoets.gojek.taxiservice.databinding.ActivityInvoiceBinding
import com.appoets.taxiservice.views.rating.RatingFragment


class  InvoiceActivity : BaseActivity<ActivityInvoiceBinding>(), InvoiceNavigator {
    private  lateinit var  rlRideCompleted:RelativeLayout
    private  lateinit var  rlRideInProgress:RelativeLayout

    override fun openRatingDialog() {
        val ratingFragment= RatingFragment()
        ratingFragment.show(supportFragmentManager,"rating")
        ratingFragment.isCancelable=false
    }

    private  var activityInvoiceBinding:ActivityInvoiceBinding?=null

    override fun getLayoutId(): Int {
        return  R.layout.activity_invoice
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityInvoiceBinding=mViewDataBinding as ActivityInvoiceBinding
        val invoiceModule= InvoiceModule()
        invoiceModule.setNavigator(this)
        activityInvoiceBinding!!.invoicemodel=invoiceModule

        //initViews
        rlRideCompleted=findViewById(R.id.rl_status_unselected)
        rlRideInProgress=findViewById(R.id.rl_status_selected)

        rlRideInProgress.visibility= View.GONE
        rlRideCompleted.visibility=View.VISIBLE

    }

}