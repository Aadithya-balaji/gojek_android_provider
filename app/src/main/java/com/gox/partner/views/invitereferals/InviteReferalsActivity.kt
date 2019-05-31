package com.gox.partner.views.invitereferals

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityInviteFriendBinding
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
        inviteReferalsViewModel.navigator = this
        mViewDataBinding.invitemodel = inviteReferalsViewModel
        getApiResponse()
        inviteReferalsViewModel.getProfileDetail()
    }

    private fun getApiResponse() {
        observeLiveData(inviteReferalsViewModel.profileResponse) {
            if (inviteReferalsViewModel.profileResponse.value != null)
                inviteReferalsViewModel.mReferalObj.value = inviteReferalsViewModel.profileResponse.value!!.profileData.referral
                mViewDataBinding.tvInviteHeader.text = String.format(resources.getString(R.string.invite_referal_hint),
                        inviteReferalsViewModel.mReferalObj.value!!.referral_amount, inviteReferalsViewModel.mReferalObj.value!!.referral_count)
                mShareLink = inviteReferalsViewModel.mReferalObj.value!!.referral_code
                mViewDataBinding.tvReferalCode.text = inviteReferalsViewModel.mReferalObj.value!!.referral_code
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
        inviteReferalsViewModel.getProfileDetail()
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(this, error, false)
    }

}