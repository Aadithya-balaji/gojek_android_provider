package com.appoets.xjek.ui.signup

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseActivity
import com.appoets.gojek.provider.R
import com.appoets.gojek.provider.databinding.ActivityRegisterBinding
import com.appoets.gojek.provider.views.countrypicker.CountryCodeActivity
import com.appoets.gojek.provider.views.dashboard.DashBoardActivity
import com.appoets.gojek.provider.views.document.DocumentActivity
import com.appoets.xjek.ui.signin.SignInActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.math.sign

class SignupActivity : BaseActivity<com.appoets.gojek.provider.databinding.ActivityRegisterBinding>(), SignupNavigator,View.OnClickListener {


    private  lateinit var  tlCountryCode:TextInputLayout

    lateinit var mViewDataBinding: ActivityRegisterBinding
    private  lateinit var  edtCountryCode: EditText

    override fun getLayoutId(): Int = R.layout.activity_register

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivityRegisterBinding
        val signupViewmodel = SignupViewModel()
        signupViewmodel.setNavigator(this)
        this.mViewDataBinding.registermodel = signupViewmodel


        tlCountryCode=findViewById(R.id.tl_country_code)
        edtCountryCode=findViewById(R.id.countrycode_register_et)

        //initListener
        initListener()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== Activity.RESULT_OK){
            if(data!=null){
               if(data.hasExtra("countryName")){
                   Log.e("code Picker","--------------"+data.extras.get("countryName"))
               }
            }
        }
    }

    fun initListener(){
       // tlCountryCode.setOnClickListener (this)
        edtCountryCode.setOnClickListener(this)
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


    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.countrycode_register_et -> {
                val intent =Intent(this@SignupActivity,CountryCodeActivity::class.java)
                startActivityForResult(intent,111)
            }
        }
    }

}
