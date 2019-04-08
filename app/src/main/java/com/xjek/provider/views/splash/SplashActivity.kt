package com.xjek.provider.views.splash

import android.os.Handler
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySplashBinding
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.on_board.OnBoardActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashViewModel.SplashNavigator {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

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

        observeViewModel()

        viewModel.getConfig()
    }

    private fun observeViewModel() {
        observeLiveData(viewModel.getConfigObservable()) {
            Constant.baseUrl = it.responseData.baseUrl
            Handler().postDelayed({
                launchNewActivity(OnBoardActivity::class.java, true)
            }, 3000)
        }
    }

    override fun showError(error: String) {
        finish()
    }
}
