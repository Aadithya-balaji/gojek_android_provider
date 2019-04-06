package com.xjek.provider.views.invitereferals

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityInviteFriendBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class InviteReferalsActivity : BaseActivity<ActivityInviteFriendBinding>() {
    lateinit var mViewDataBinding: ActivityInviteFriendBinding
    override fun getLayoutId(): Int = R.layout.activity_invite_friend

    override fun initView(mViewDataBinding: ViewDataBinding?) {


        this.mViewDataBinding = mViewDataBinding as ActivityInviteFriendBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.title_invite_referrals)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }

    }

}