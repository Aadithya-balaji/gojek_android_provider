package com.xjek.provider.views.support

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySupportBinding
import com.xjek.xjek.ui.support.SupportNavigator
import kotlinx.android.synthetic.main.toolbar_layout.view.*


class SupportActivity : BaseActivity<ActivitySupportBinding>(), SupportNavigator {

    lateinit var mViewDataBinding: ActivitySupportBinding


    override fun getLayoutId(): Int = R.layout.activity_support


    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivitySupportBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.support)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }
    }

    override fun goToPhoneCall() {

    }

    override fun goToMail() {
    }

    override fun goToWebsite() {
    }
}