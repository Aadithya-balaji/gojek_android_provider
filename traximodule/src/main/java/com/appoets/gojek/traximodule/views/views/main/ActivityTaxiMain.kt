package com.appoets.gojek.traximodule.views.views.main

import androidx.databinding.ViewDataBinding
import com.appoets.gojek.traximodule.R
import com.appoets.gojek.traximodule.views.views.base.TaxiBaseActivity

class ActivityTaxiMain:TaxiBaseActivity<com.appoets.gojek.traximodule.databinding.ActivityTaxiMainBinding>(),ActivityTaxMainNavigator{
    private  lateinit var  activityTaxiMainBinding: com.appoets.gojek.traximodule.databinding.ActivityTaxiMainBinding
    override fun getLayoutId(): Int {
        return  R.layout.activity_taxi_main
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.activityTaxiMainBinding = mViewDataBinding as com.appoets.gojek.traximodule.databinding.ActivityTaxiMainBinding
        val activityTaxiModule = ActivityTaxiModule()
        activityTaxiModule.setNavigator(this)
    }

}
