package com.xjek.provider.views.privacypolicy

import android.graphics.Bitmap
import android.os.Build
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityPrivacyPolicyBinding
import com.xjek.provider.utils.Constant


class PrivacyActivity : BaseActivity<ActivityPrivacyPolicyBinding>(), PrivactyNavigator {
    private lateinit var privacyBinding: ActivityPrivacyPolicyBinding
    private lateinit var wvPrivacy: WebView
    private lateinit var ivBack: ImageView
    private lateinit var privacyViewModel: PrivacyViewModel

    companion object {
        var loadingProgress: MutableLiveData<Boolean>? = null

    }

    override fun getLayoutId(): Int {
        return com.xjek.provider.R.layout.activity_privacy_policy
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        privacyBinding = mViewDataBinding as ActivityPrivacyPolicyBinding
        privacyViewModel = PrivacyViewModel()
        privacyViewModel.navigator = this
        privacyBinding.privacymodel = privacyViewModel
        privacyBinding.toolbarLayout.tvToolbarTitle.text = resources.getString(R.string.header_label_privacy)
        privacyBinding.toolbarLayout.ivToolbarBack.setOnClickListener {
            finish()
        }
        if (!Constant.privacyPolicyUrl.isNullOrEmpty()) {
            privacyBinding.wvPrivacy.setWebViewClient(WebClient())
        }
        privacyBinding.wvPrivacy.loadUrl(Constant.privacyPolicyUrl)
        loadingProgress?.let {
            observeLiveData(it){
                loadingObservable.value = it
            }
        }
        //loadingProgress = loadingObservable as MutableLiveData<Boolean>

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
            loadingProgress!!.value = false
        }

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            loadingProgress!!.value = true
        }

    }

}