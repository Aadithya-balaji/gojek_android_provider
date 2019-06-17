package com.gox.partner.views.invitereferals

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityInviteFriendBinding
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class InviteReferralsActivity : BaseActivity<ActivityInviteFriendBinding>(), InviteReferralsNavigator {

    private lateinit var mBinding: ActivityInviteFriendBinding
    private lateinit var mViewModel: InviteReferralsViewModel

    private var mShareLink: String? = null

    override fun getLayoutId() = R.layout.activity_invite_friend

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mBinding = mViewDataBinding as ActivityInviteFriendBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.title_invite_referrals)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }

        mViewModel = InviteReferralsViewModel()
        mViewModel.navigator = this
        mViewDataBinding.invitemodel = mViewModel
        getApiResponse()
        mViewModel.getProfileDetail()
    }

    private fun getApiResponse() {
        observeLiveData(mViewModel.profileResponse) {
            if (mViewModel.profileResponse.value != null)
                mViewModel.mReferralObj.value = mViewModel.profileResponse.value!!.profileData.referral
            mBinding.tvInviteHeader.text = String.format(resources.getString(R.string.invite_referal_hint),
                    mViewModel.mReferralObj.value!!.referral_amount, mViewModel.mReferralObj.value!!.referral_count)
            mShareLink = mViewModel.mReferralObj.value!!.referral_code
            mBinding.tvReferalCode.text = mViewModel.mReferralObj.value!!.referral_code
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
        mViewModel.getProfileDetail()
    }

    override fun showError(error: String) {
        loadingObservable.value = false
        ViewUtils.showToast(this, error, false)
    }

}