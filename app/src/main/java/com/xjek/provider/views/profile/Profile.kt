package com.xjek.provider.views.profile

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityEditProfileBinding
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