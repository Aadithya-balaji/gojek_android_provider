package com.xjek.provider.views.support

import android.content.Intent
import android.net.Uri
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.provider.R
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.xjek.ui.support.SupportNavigator
import kotlinx.android.synthetic.main.activity_support.*


class SupportActivity : BaseActivity<ActivitySupportBinding>(), SupportNavigator {

    lateinit var mViewDataBinding: ActivitySupportBinding
    val preference = PreferencesHelper
    private lateinit var supportDetails: ConfigResponseModel.ResponseData.AppSetting.SupportDetails


    override fun getLayoutId(): Int = R.layout.activity_support


    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivitySupportBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.support)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener { view ->
            finish()
        }


        val baseApiResponseString: String = preference.get(PreferencesKey.BASE_CONFIG_RESPONSE, "")!! as String
        val baseApiResponsedata: ConfigResponseModel.ResponseData = Gson().fromJson<ConfigResponseModel.ResponseData>(baseApiResponseString, ConfigResponseModel.ResponseData::class.java)
        supportDetails = baseApiResponsedata.appSetting.supportDetails

        phonenumber_support_tv.text = supportDetails.contactNumber[0].number
        mail_support_tv.text = supportDetails.contactEmail
        //website_support_tv.text = supportDetails

        phonenumber_support_tv.setOnClickListener {
            goToPhoneCall()
        }

        mail_support_tv.setOnClickListener {
            goToMail()
        }

    }


    override fun goToPhoneCall() {

        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + supportDetails.contactNumber[0].number.toString())
        startActivity(intent)
    }

    override fun goToMail() {

        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", supportDetails.contactEmail, null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail))
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    override fun goToWebsite() {
        /*      val i = Intent(Intent.ACTION_VIEW)
              i.data = Uri.parse("")
              startActivity(i)*/
    }


}