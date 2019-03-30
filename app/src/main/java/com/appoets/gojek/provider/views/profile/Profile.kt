package com.appoets.xjek.ui.profile

import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityEditProfileBinding
import com.appoets.gojek.provider.views.change_password.ChangePasswordActivity
import kotlinx.android.synthetic.main.toolbar_layout.view.*


class Profile : BaseActivity<ActivityEditProfileBinding>(), View.OnClickListener {

    lateinit var mViewDataBinding: ActivityEditProfileBinding

    override fun getLayoutId(): Int = R.layout.activity_edit_profile

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityEditProfileBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.profile)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }
        mViewDataBinding.tvChangePassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_change_password -> {
                val changePasswordIntent = Intent(applicationContext,
                        ChangePasswordActivity::class.java)
                startActivity(changePasswordIntent)
            }
        }
    }
}