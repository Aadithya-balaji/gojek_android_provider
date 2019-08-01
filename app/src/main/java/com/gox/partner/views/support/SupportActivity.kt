package com.gox.partner.views.support

import android.content.Intent
import android.net.Uri
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.gox.base.BuildConfig
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.data.PreferencesKey
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySupportBinding
import com.gox.partner.models.ConfigResponseData
import com.gox.partner.models.Supportdetails
import kotlinx.android.synthetic.main.activity_support.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*

class SupportActivity : BaseActivity<ActivitySupportBinding>(), SupportNavigator {

    lateinit var mBinding: ActivitySupportBinding
    private lateinit var supportDetails: Supportdetails
    private lateinit var supportURL:String

    override fun getLayoutId() = R.layout.activity_support

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mBinding = mViewDataBinding as ActivitySupportBinding
        mViewDataBinding.toolbarLayout.title_toolbar.setTitle(R.string.support)
        mViewDataBinding.toolbarLayout.toolbar_back_img.setOnClickListener {
            finish()
        }

        val baseApiResponseString: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.BASE_CONFIG_RESPONSE, "")!!
        val baseApiResponseData: ConfigResponseData = Gson().fromJson<ConfigResponseData>(
                baseApiResponseString, ConfigResponseData::class.java)
        supportDetails = baseApiResponseData.appsetting.supportdetails

        if (!supportDetails.contact_number.isEmpty())
            phonenumber_support_tv.text = supportDetails.contact_number[0].number

        mail_support_tv.text = supportDetails.contact_email
        supportURL = baseApiResponseData.appsetting.cmspage.help
        website_support_tv.text = supportURL

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
        if (!supportDetails.contact_number.isEmpty()) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + supportDetails.contact_number[0].number)
            startActivity(intent)
        }
    }

    override fun goToMail() {
        if (supportDetails.contact_email.isNotEmpty()) {
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", supportDetails.contact_email, null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_mail))
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        }
    }

    override fun goToWebsite() {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(supportURL)
        startActivity(i)
    }

}