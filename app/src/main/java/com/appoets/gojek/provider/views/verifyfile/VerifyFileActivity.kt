package com.appoets.gojek.provider.views.verifyfile

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityVerifyProofBinding
import com.appoets.gojek.provider.views.dashboard.DashBoardActivity
import com.google.android.gms.maps.model.Dash

class VerifyFileActivity : BaseActivity<ActivityVerifyProofBinding>(), VerifyFileNavigator {


    private var mVerifyProofBinding: ActivityVerifyProofBinding? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_verify_proof
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mVerifyProofBinding = mViewDataBinding as ActivityVerifyProofBinding
        val verfiyFileViewModel = VerifyFIleVIewModel()
          verfiyFileViewModel.setNavigator(this)
        mVerifyProofBinding!!.verifyFileModel = verfiyFileViewModel
    }

    override fun gotoDashBoardPage() {
        val intent = Intent(this@VerifyFileActivity, DashBoardActivity::class.java)
        startActivity(intent)
    }

}