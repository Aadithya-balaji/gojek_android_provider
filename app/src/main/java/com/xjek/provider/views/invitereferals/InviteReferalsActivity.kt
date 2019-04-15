package com.xjek.provider.views.invitereferals

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityInviteFriendBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class InviteReferalsActivity : BaseActivity<ActivityInviteFriendBinding>(), InviteReferalsNavigator {
    private lateinit var mViewDataBinding: ActivityInviteFriendBinding
    private lateinit var inviteReferalsViewModel: InviteReferalsViewModel
    override fun getLayoutId(): Int = R.layout.activity_invite_friend
    private var mShareLink: String? = null


    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityInviteFriendBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.title_invite_referrals)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }

        inviteReferalsViewModel = InviteReferalsViewModel()
        inviteReferalsViewModel!!.navigator = this
        mViewDataBinding.invitemodel = inviteReferalsViewModel
        getApiResponse()
        inviteReferalsViewModel.getProfileDetail()
    }

    fun getApiResponse() {
        observeLiveData(inviteReferalsViewModel.profileResponse) {
            if (inviteReferalsViewModel.profileResponse.value != null) {
                if (inviteReferalsViewModel.profileResponse.value!!.profileData!!.referalData != null) {
                    inviteReferalsViewModel.mReferalObj.value = inviteReferalsViewModel.profileResponse.value!!.profileData!!.referalData
                    mViewDataBinding.tvInviteHeader.setText(String.format(resources.getString(R.string.invite_referal_hint), inviteReferalsViewModel.mReferalObj.value!!.referalAmount, inviteReferalsViewModel.mReferalObj.value!!.referralCount))
                    mShareLink=inviteReferalsViewModel.mReferalObj.value!!.referralCode
                    mViewDataBinding.tvReferalCode.text=inviteReferalsViewModel.mReferalObj.value!!.referralCode
                }
            }
        }
    }


    override fun goToInviteOption() {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        share.putExtra(Intent.EXTRA_SUBJECT, "Go jek")
        share.putExtra(Intent.EXTRA_TEXT, mShareLink)
        startActivity(Intent.createChooser(share, "Share"))
    }

    override fun getProfileDetail() {
        loadingObservable.value = true
        inviteReferalsViewModel!!.getProfileDetail()
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(this, error, false)
    }

}