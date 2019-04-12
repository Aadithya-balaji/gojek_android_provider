package com.xjek.provider.views.splash

import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.extensions.readPreferences
import com.xjek.base.extensions.writePreferences
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySplashBinding
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.models.ConfigResponseModel.ResponseData.AppSetting.Language
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.on_board.OnBoardActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashViewModel.SplashNavigator {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    public override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySplashBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            SplashViewModel()
        }
        viewModel.navigator = this

        observeViewModel()

        viewModel.getConfig()
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getConfigObservable()) {
            writePreferences(PreferencesKey.BASE_ID, "0")
            writePreferences(PreferencesKey.BASE_ID, "0")
            writePreferences(PreferencesKey.BASE_ID, "0")
            writePreferences("0", it.responseData.baseUrl+"/")
            writePreferences(PreferencesKey.TRANSPORT_ID, it.responseData.services[0].id)
            writePreferences(it.responseData.services[0].id.toString(),
                    it.responseData.services[0].baseUrl+"/")
            writePreferences(PreferencesKey.ORDER_ID, it.responseData.services[1].id)
            writePreferences(it.responseData.services[1].id.toString(),
                    it.responseData.services[1].baseUrl+"/")
            writePreferences(PreferencesKey.SERVICE_ID, it.responseData.services[2].id)
            writePreferences(it.responseData.services[2].id.toString(),
                    it.responseData.services[2].baseUrl+"/")
            writePreferences(PreferencesKey.PRIVACY_POLICY,
                    it.responseData.appSetting.cmsPage.privacyPolicy)
            writePreferences(PreferencesKey.HELP, it.responseData.appSetting.cmsPage.help)
            writePreferences(PreferencesKey.TERMS, it.responseData.appSetting.cmsPage.terms)
            val contactNumbers = hashSetOf<String>()
            for (contact in it.responseData.appSetting.supportDetails.contactNumber)
                contactNumbers.add(contact.number)
            writePreferences(PreferencesKey.CONTACT_NUMBER, contactNumbers.toSet())
            writePreferences(PreferencesKey.CONTACT_EMAIL,
                    it.responseData.appSetting.supportDetails.contactEmail)

            setLanguage(it)

            if (readPreferences(PreferencesKey.ACCESS_TOKEN, "")!! == "")
                launchNewActivity(OnBoardActivity::class.java, true)
            else
                launchNewActivity(DashBoardActivity::class.java, true)
        }
    }

    private fun setLanguage(it: ConfigResponseModel) {
        val languages = it.responseData.appSetting.languages
        if (languages.isNotEmpty())
            Constant.languages = languages
        else {
            val defaultLanguage = Language("English", "en")
            Constant.languages = listOf(defaultLanguage)
        }
    }

    override fun showError(error: String) {
        finish()
    }
}
