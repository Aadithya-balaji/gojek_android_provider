package com.xjek.provider.views.verifyfile

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityVerifyProofBinding
import com.xjek.provider.views.dashboard.DashBoardActivity

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