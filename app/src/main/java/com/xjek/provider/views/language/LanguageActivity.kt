package com.xjek.provider.views.language

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.provideViewModel
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityLanguageBinding
import kotlinx.android.synthetic.main.layout_app_bar.view.*

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(),LanguageNavigator {

    private lateinit var binding: ActivityLanguageBinding
    private lateinit var viewModel: LanguageViewModel


    override fun getLayoutId(): Int = R.layout.activity_language

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivityLanguageBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            LanguageViewModel()
        }

        viewModel.navigator = this
        viewModel.setLanguage()
        binding.languageViewModel = viewModel

        setSupportActionBar(binding.toolbar.tbApp)
        binding.toolbar.tbApp.iv_toolbar_back.setOnClickListener { onBackPressed() }
        binding.toolbar.tbApp.tv_toolbar_title.text = resources.getString(R.string.title_language)

    }

}
