package com.appoets.xjek.ui.signup

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityRegisterBinding
import com.appoets.gojek.provider.views.dashboard.DashBoardActivity
import com.appoets.gojek.provider.views.document.DocumentActivity
import com.appoets.xjek.ui.signin.SignInActivity
import kotlin.math.sign

class SignupActivity : BaseActivity<com.appoets.gojek.provider.databinding.ActivityRegisterBinding>(), SignupNavigator {



    lateinit var mViewDataBinding: ActivityRegisterBinding
    override fun getLayoutId(): Int = R.layout.activity_register

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityRegisterBinding
        val signupViewmodel = SignupViewModel()
        signupViewmodel.setNavigator(this)
        this.mViewDataBinding.registermodel = signupViewmodel

    }

    //do registration
    override fun signup() {

    }

    // move to signin page
    override fun openSignin() {
        openNewActivity(this@SignupActivity, SignInActivity::class.java, true)
    }

    override fun gotoDocumentPage() {
        val intent=Intent(this@SignupActivity,DocumentActivity::class.java)
        startActivity(intent)

    }



}
