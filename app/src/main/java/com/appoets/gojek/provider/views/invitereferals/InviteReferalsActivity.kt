package com.appoets.xjek.ui.invitereferals

import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityInviteFriendBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class InviteReferalsActivity : BaseActivity<ActivityInviteFriendBinding>() {
    lateinit var mViewDataBinding: ActivityInviteFriendBinding
    override fun getLayoutId(): Int = R.layout.activity_invite_friend

    override fun initView(mViewDataBinding: ViewDataBinding?) {


        this.mViewDataBinding = mViewDataBinding as ActivityInviteFriendBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.invite_refferals)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }

    }

}