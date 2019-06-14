package com.gox.partner.views.language

import androidx.databinding.ViewDataBinding
import com.gox.base.base.BaseActivity
import com.gox.base.extensions.provideViewModel
import com.gox.base.utils.LocaleUtils
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityLanguageBinding
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(), LanguageNavigator {

    private lateinit var mBinding: ActivityLanguageBinding
    private lateinit var mViewModel: LanguageViewModel

    private lateinit var selectedLanguage: String

    override fun getLayoutId() = R.layout.activity_language

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityLanguageBinding
        mBinding.lifecycleOwner = this

        mViewModel = provideViewModel {
            LanguageViewModel()
        }

        selectedLanguage = LocaleUtils.getLanguagePref(this)!!

        mViewModel.navigator = this
        mViewModel.setLanguage(selectedLanguage)

        mBinding.languageViewModel = mViewModel

        setSupportActionBar(mBinding.toolbar.tbApp)
        mBinding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        mBinding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_language)
    }

    override fun onLanguageChanged() {
        if (mViewModel.getCurrentLanguage() != selectedLanguage) if (mViewModel.getCurrentLanguage() != "en") {
            ViewUtils.showToast(this@LanguageActivity, "For future purpose", false)
            return
        }
        ViewUtils.showToast(this@LanguageActivity, getString(R.string.language_change_success), true)
    }
}
