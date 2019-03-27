package com.appoets.xjek.ui.profile

import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityEditProfileBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class Profile : BaseActivity<ActivityEditProfileBinding>() {
    lateinit var mViewDataBinding: ActivityEditProfileBinding

    override fun getLayoutId(): Int = R.layout.activity_edit_profile

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityEditProfileBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.profile)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }
    }

}