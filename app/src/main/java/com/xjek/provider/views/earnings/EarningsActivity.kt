package com.xjek.provider.views.earnings

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityEarningsBinding

class EarningsActivity : BaseActivity<ActivityEarningsBinding>(), EarningsNavigator {

    override fun getLayoutId(): Int = R.layout.activity_earnings

    override fun initView(mViewDataBinding: ViewDataBinding?) {

    }
}
