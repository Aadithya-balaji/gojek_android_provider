package com.appoets.xjek.ui.signup

import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityRegisterBinding
import com.appoets.xjek.ui.signin.SignInActivity
import kotlin.math.sign

class SignupActivity : BaseActivity<com.appoets.gojek.provider.databinding.ActivityRegisterBinding>(), SignupNavigator {


    lateinit var mViewDataBinding: ActivityRegisterBinding

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityRegisterBinding
        val signupViewmodel = SignupViewModel()
        signupViewmodel.setNavigator(this)
      //  mViewDataBinding.signupmodel= signupViewmodel
       // mViewDataBinding.signupviewmodel = signupViewmodel
    }

    //do registration
    override fun signup() {

    }

    // move to signin page
    override fun openSignin() {
        openNewActivity(this@SignupActivity, SignInActivity::class.java, true)
    }

}
