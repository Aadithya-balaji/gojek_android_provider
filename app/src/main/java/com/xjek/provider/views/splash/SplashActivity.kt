package com.xjek.provider.views.splash

import android.os.Handler
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.base.BaseApplication
import com.xjek.base.data.PreferenceHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.data.getValue
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySplashBinding
import com.xjek.provider.models.LanguageModel
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.on_board.OnBoardActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashViewModel.SplashNavigator {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    private val preference = PreferenceHelper(BaseApplication.baseApplication)


    public override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    public override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySplashBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel {
            SplashViewModel()
        }
        viewModel.navigator = this

        //observeViewModel()

        moveToNextScreen()
        //viewModel.getConfig()
        val language = ArrayList<LanguageModel>()
        val english = LanguageModel()
        english.name = "English"
        english.key = "en"
        val arabic = LanguageModel()
        arabic.name = "Arabic"
        arabic.key = "ar"
        language.add(arabic)

        Constant.languages = language
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getConfigObservable()) {
            Constant.baseUrl = it.responseData.baseUrl
            Constant.privacyPolicyUrl=it.responseData.appsetting.cmspage.privacypolicy.toString()
            moveToNextScreen()
        }
    }

    private fun moveToNextScreen() {
        Handler().postDelayed({
            launchNewActivity(OnBoardActivity::class.java, true)

            if (preference.getValue(PreferencesKey.ACCESS_TOKEN, "")!! == "")
                launchNewActivity(OnBoardActivity::class.java, true)
            else
                launchNewActivity(DashBoardActivity::class.java, true)
        }, 3000)
    }

    override fun showError(error: String) {
        finish()
    }
}
