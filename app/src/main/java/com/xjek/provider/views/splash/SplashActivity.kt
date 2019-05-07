package com.xjek.provider.views.splash

import android.content.Intent
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.xjek.base.BuildConfig
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.Constants
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.provideViewModel
import com.xjek.base.extensions.readPreferences
import com.xjek.base.extensions.writePreferences
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivitySplashBinding
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.utils.Constant
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.on_board.OnBoardActivity
import com.xjek.foodservice.view.FoodLiveTaskServiceFlow
import com.xjek.base.socket.SocketListener
import com.xjek.base.socket.SocketManager
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity


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
            writePreferences("0", it.responseData.baseUrl + "/")
//            writePreferences(PreferencesKey.BASE_ID, "0")
//            writePreferences(PreferencesKey.BASE_ID, "0")

            Constants.BaseUrl.APP_BASE_URL = it.responseData.baseUrl

            writePreferences(PreferencesKey.BASE_CONFIG_RESPONSE, Gson().toJson(it.responseData))

            it.responseData.services.forEach { service ->
                run {
                    when (service.adminServiceName) {
                        "TRANSPORT" -> Constants.BaseUrl.TAXI_BASE_URL = service.baseUrl
                        "ORDER" -> Constants.BaseUrl.ORDER_BASE_URL = service.baseUrl
                        "SERVICE" -> Constants.BaseUrl.SERVICE_BASE_URL = service.baseUrl
                    }
                }
            }


            writePreferences("0", it.responseData.baseUrl + "/")
            writePreferences(PreferencesKey.TRANSPORT_ID, it.responseData.services[0].id)
            writePreferences(it.responseData.services[0].id.toString(),
                    it.responseData.services[0].baseUrl + "/")
            writePreferences(PreferencesKey.ORDER_ID, it.responseData.services[1].id)
            writePreferences(it.responseData.services[1].id.toString(),
                    it.responseData.services[1].baseUrl + "/")
            writePreferences(PreferencesKey.SERVICE_ID, it.responseData.services[2].id)
            writePreferences(it.responseData.services[2].id.toString(),
                    it.responseData.services[2].baseUrl + "/")
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
            setPayment(it)

            Constant.privacyPolicyUrl = it.responseData.appSetting.cmsPage.privacyPolicy

            if (readPreferences(PreferencesKey.ACCESS_TOKEN, "")!! == "")
                launchNewActivity(OnBoardActivity::class.java, true)
            else {
                launchNewActivity(DashBoardActivity::class.java, true)
            }
        }
    }

    fun setPayment(it: ConfigResponseModel) {
        val paymentlist = it.responseData.appSetting.payments
        val gson = Gson()
        val paymentString = gson.toJson(paymentlist)
        writePreferences(PreferencesKey.PAYMENT_LIST, paymentString)
    }


    private fun setLanguage(it: ConfigResponseModel) {
        val languages = it.responseData.appSetting.languages
        if (languages.isNotEmpty())
            Constant.languages = languages
        else {
            val defaultLanguage = ConfigResponseModel.ResponseData.AppSetting.Language("English", "en")
            Constant.languages = listOf(defaultLanguage)
        }
    }

    override fun showError(error: String) {
        finish()
    }
}
