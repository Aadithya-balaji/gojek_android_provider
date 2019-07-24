package com.gox.partner.views.privacypolicy

import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.partner.R
import com.gox.partner.databinding.ActivityPrivacyPolicyBinding

class PrivacyActivity : BaseActivity<ActivityPrivacyPolicyBinding>(), PrivacyNavigator {

    private lateinit var mBinding: ActivityPrivacyPolicyBinding
    private lateinit var mViewModel: PrivacyViewModel

    companion object {
        var showLoading: MutableLiveData<Boolean> = MutableLiveData<Boolean>().apply { false }
    }

    override fun getLayoutId() = R.layout.activity_privacy_policy

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityPrivacyPolicyBinding
        mViewModel = PrivacyViewModel()
        mViewModel.navigator = this
        mBinding.privacymodel = mViewModel
        mBinding.toolbarLayout.ivToolbarBack.setOnClickListener {
            finish()
        }
        if (Constants.privacyPolicyUrl.isNotEmpty()) mBinding.wvPrivacy.webViewClient = WebClient()
        else if (Constants.termsUrl.isNotEmpty()) mBinding.wvPrivacy.webViewClient = WebClient()

        if (intent.extras == null) {
            mBinding.toolbarLayout.tvToolbarTitle.text = resources.getString(R.string.header_label_privacy)
            mBinding.wvPrivacy.loadUrl(Constants.privacyPolicyUrl)
        } else {
            mBinding.toolbarLayout.tvToolbarTitle.text = getString(R.string.terms_conditions)
            mBinding.wvPrivacy.loadUrl(Constants.termsUrl)
        }

        observeLiveData(showLoading) {
            loadingObservable.value = it
        }

    }

    class WebClient : WebViewClient() {
        @SuppressWarnings("deprecation")
        override fun shouldOverrideUrlLoading(view: WebView, urlNewString: String): Boolean {
            view.loadUrl(urlNewString)
            return true
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val url = request.url.toString()
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            showLoading.value = false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            showLoading.value = true
        }

    }

}