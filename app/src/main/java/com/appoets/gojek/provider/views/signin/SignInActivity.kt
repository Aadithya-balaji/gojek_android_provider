package com.appoets.xjek.ui.signin

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivitySiginBinding
import com.appoets.gojek.provider.views.dashboard.DashBoardActivity
import com.appoets.xjek.ui.signup.SignupActivity

class SignInActivity : BaseActivity<ActivitySiginBinding>(), SigninNavigator {


    val TAG = "SigninActivity"
    lateinit var mViewDataBinding: ActivitySiginBinding

    override fun getLayoutId(): Int = R.layout.activity_sigin

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivitySiginBinding
        val signViewModel = SigninViewModel()
        signViewModel.setNavigator(this)

    }


    override fun changeSigninViaPhone(): Boolean = true

    override fun changeSigninViaMail(): Boolean = true

    //move to signup page
    override fun goToSignup() {

        openNewActivity(this@SignInActivity, SignupActivity::class.java, false)
    }

    override fun googleSignin() {


    }

    override fun facebookSignin() {
    }

    override fun gotoHome() {

        val intent= Intent(this@SignInActivity,DashBoardActivity::class.java)
        startActivity(intent)

    }

}