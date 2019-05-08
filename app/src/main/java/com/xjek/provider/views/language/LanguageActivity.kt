package com.xjek.provider.views.language

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.utils.LocaleUtils
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityLanguageBinding
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(), LanguageNavigator {

    private lateinit var binding: ActivityLanguageBinding
    private lateinit var viewModel: LanguageViewModel
    private lateinit var selectedLanguage: String

    override fun getLayoutId(): Int = R.layout.activity_language

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityLanguageBinding
        binding.lifecycleOwner = this

        viewModel = provideViewModel {
            LanguageViewModel()
        }

        selectedLanguage = LocaleUtils.getLanguagePref(this)!!

        viewModel.navigator = this
        viewModel.setLanguage(selectedLanguage)

        binding.languageViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_language)

    }

    override fun onLanguageChanged() {
        if (viewModel.getCurrentLanguage() != selectedLanguage) {
            if(viewModel.getCurrentLanguage() != "en"){
                ViewUtils.showToast(this@LanguageActivity, "For future purpose", false)
                return
            }
//            LocaleUtils.setNewLocale(this, viewModel.getCurrentLanguage())
//            recreate()
        }

        ViewUtils.showToast(this@LanguageActivity, getString(R.string.language_change_success), true)
    }
}
