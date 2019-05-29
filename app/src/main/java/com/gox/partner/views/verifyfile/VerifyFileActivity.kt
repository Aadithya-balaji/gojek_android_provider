package com.gox.partner.views.verifyfile

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.partner.R
import com.gox.partner.databinding.ActivityVerifyProofBinding
import com.gox.partner.views.dashboard.DashBoardActivity

class VerifyFileActivity : BaseActivity<ActivityVerifyProofBinding>(), VerifyFileNavigator {


    private var mVerifyProofBinding: ActivityVerifyProofBinding? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_verify_proof
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mVerifyProofBinding = mViewDataBinding as ActivityVerifyProofBinding
        val verfiyFileViewModel = VerifyFIleVIewModel()
        mVerifyProofBinding!!.verifyFileModel = verfiyFileViewModel
    }

    override fun gotoDashBoardPage() {
        val intent = Intent(this@VerifyFileActivity, DashBoardActivity::class.java)
        startActivity(intent)
    }

}