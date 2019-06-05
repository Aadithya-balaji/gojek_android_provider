package com.gox.partner.views.support

import android.content.Intent
import android.net.Uri
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySupportBinding
import com.gox.partner.models.ConfigResponseModel
import com.gox.partner.models.Supportdetails
import com.gox.xjek.ui.support.SupportNavigator
import kotlinx.android.synthetic.main.activity_support.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class SupportActivity : BaseActivity<ActivitySupportBinding>(), SupportNavigator {

    lateinit var mViewDataBinding: ActivitySupportBinding
    private lateinit var supportDetails: Supportdetails

    override fun getLayoutId(): Int = R.layout.activity_support

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as ActivitySupportBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.support)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener {
            finish()
        }

        val baseApiResponseString: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.BASE_CONFIG_RESPONSE, "")!!
        val baseApiResponseData: ConfigResponseModel = Gson().fromJson<ConfigResponseModel>(baseApiResponseString, ConfigResponseModel::class.java)
        supportDetails = baseApiResponseData.responseData.appsetting.supportdetails

        phonenumber_support_tv.text = supportDetails.contact_number[0].number
        mail_support_tv.text = supportDetails.contact_email
        //website_support_tv.text = supportDetails
        call_support_ll.setOnClickListener {
            goToPhoneCall()
        }

        mail_support_ll.setOnClickListener {
            goToMail()
        }

        web_support_ll.setOnClickListener {
            goToWebsite()
        }
    }

    override fun goToPhoneCall() {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + supportDetails.contact_number[0].number)
        startActivity(intent)
    }

    override fun goToMail() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", supportDetails.contact_email, null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail))
        startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    override fun goToWebsite() {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse("https://www.google.com")
        startActivity(i)
    }

}