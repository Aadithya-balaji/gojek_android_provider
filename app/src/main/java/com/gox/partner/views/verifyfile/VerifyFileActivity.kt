package com.gox.partner.views.verifyfile

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityVerifyProofBinding
import com.gox.partner.views.dashboard.DashBoardActivity

class VerifyFileActivity : BaseActivity<ActivityVerifyProofBinding>(), VerifyFileNavigator {

    private var mBinding: ActivityVerifyProofBinding? = null

    override fun getLayoutId() = R.layout.activity_verify_proof

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityVerifyProofBinding
        val mViewModel = VerifyFileViewModel()
        mBinding!!.verifyFileModel = mViewModel
    }

    override fun gotoDashBoardPage() =
            startActivity(Intent(this@VerifyFileActivity, DashBoardActivity::class.java))

}