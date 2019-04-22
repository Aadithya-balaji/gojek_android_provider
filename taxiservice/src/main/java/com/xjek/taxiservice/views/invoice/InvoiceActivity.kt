package com.xjek.taxiservice.views.invoice

import android.view.View
import android.widget.RelativeLayout
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.taxiservice.R
import com.xjek.taxiservice.views.rating.RatingFragment
import  com.xjek.taxiservice.databinding.ActivityInvoiceBinding
import com.xjek.taxiservice.views.tollcharge.TollCharge


class  InvoiceActivity : BaseActivity<com.xjek.taxiservice.databinding.ActivityInvoiceBinding>(), InvoiceNavigator {



    private  lateinit var  rlRideCompleted:RelativeLayout
    private  lateinit var  rlRideInProgress:RelativeLayout

    override fun openRatingDialog() {
        val ratingFragment= RatingFragment()
        ratingFragment.show(supportFragmentManager,"rating")
        ratingFragment.isCancelable=false
    }


    private  var activityInvoiceBinding: ActivityInvoiceBinding?=null

    override fun getLayoutId(): Int {
        return  R.layout.activity_invoice
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityInvoiceBinding=mViewDataBinding as ActivityInvoiceBinding
        val invoiceModule= InvoiceModule()
        invoiceModule.navigator=this
        activityInvoiceBinding!!.invoicemodel=invoiceModule

        //initViews
        rlRideCompleted=findViewById(R.id.rl_status_unselected)
        rlRideInProgress=findViewById(R.id.rl_status_selected)

        rlRideInProgress.visibility= View.GONE
        rlRideCompleted.visibility=View.VISIBLE

    }

    override fun showTollDialog() {
        val tollChargeDialog=TollCharge()
        tollChargeDialog.show(supportFragmentManager,"tollCharge")
    }


    override fun tollCharge(amount: String) {

    }

}
