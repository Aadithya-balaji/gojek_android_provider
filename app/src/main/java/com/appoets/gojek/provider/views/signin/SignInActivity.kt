package com.appoets.xjek.ui.signin

import android.content.Intent
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivitySiginBinding
import com.appoets.gojek.provider.views.dashboard.DashBoardActivity
import com.appoets.xjek.ui.signup.SignupActivity
import com.google.android.material.textfield.TextInputLayout

class SignInActivity : BaseActivity<ActivitySiginBinding>(), SigninNavigator, CompoundButton.OnCheckedChangeListener {


    val TAG = "SigninActivity"
    private lateinit var mViewDataBinding: ActivitySiginBinding
    private lateinit var rbEmail: RadioButton
    private lateinit var rbPhone: RadioButton
    private lateinit var tlEmail: TextInputLayout
    private lateinit var llPhone: LinearLayout

    override fun getLayoutId(): Int = R.layout.activity_sigin

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivitySiginBinding
        val signViewModel = SigninViewModel()
        signViewModel.setNavigator(this)
        this.mViewDataBinding.signinmodel = signViewModel

        rbEmail = findViewById(R.id.rb_email)
        rbPhone = findViewById(R.id.rb_phone)
        tlEmail = findViewById(R.id.tl_signin_email)
        llPhone = findViewById(R.id.ll_signin_phone)

        //InitListener
        rbEmail.setOnCheckedChangeListener(this)
        rbPhone.setOnCheckedChangeListener(this)

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
        val intent = Intent(this@SignInActivity, DashBoardActivity::class.java)
        startActivity(intent)
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        when (buttonView!!.id) {
            R.id.rb_email -> {
                if(isChecked) {
                    tlEmail.visibility = View.VISIBLE
                    llPhone.visibility = View.GONE
                }
            }

            R.id.rb_phone -> {
                if(isChecked) {
                    llPhone.visibility = View.VISIBLE
                    tlEmail.visibility = View.GONE
                }
            }
        }
    }

}