package com.xjek.provider.views.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson
import com.xjek.base.BuildConfig
import com.xjek.base.base.BaseActivity
import com.xjek.base.base.BaseApplication
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

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashViewModel.SplashNavigator {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    public override fun getLayoutId(): Int = R.layout.activity_splash

    private lateinit var mUrlPersistence: SharedPreferences

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySplashBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel { SplashViewModel() }
        viewModel.navigator = this

        observeViewModel()

        viewModel.getConfig()

        mUrlPersistence = BaseApplication.run { getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE) }
    }

    @SuppressLint("CommitPrefEdits")
    private fun observeViewModel() {
        observeLiveData(viewModel.getConfigObservable()) {
            writePreferences(PreferencesKey.BASE_ID, "0")
            writePreferences(PreferencesKey.BASE_ID, "0")
            writePreferences(PreferencesKey.BASE_ID, "0")
            writePreferences("0", it.responseData.baseUrl + "/")
//            writePreferences(PreferencesKey.BASE_ID, "0")
//            writePreferences(PreferencesKey.BASE_ID, "0")


            mUrlPersistence.run {
                edit().putString(PreferencesKey.BASE_URL, it.responseData.baseUrl).apply()
            }

            writePreferences(PreferencesKey.BASE_CONFIG_RESPONSE, Gson().toJson(it.responseData))

            it.responseData.services.forEach { service ->
                run {
                    when (service.adminServiceName) {
                        "TRANSPORT" -> run {
                            mUrlPersistence.edit().putString(PreferencesKey.TRANSPORT_URL, service.baseUrl)
                        }
                        "ORDER" -> {
                            mUrlPersistence.edit().putString(PreferencesKey.ORDER_URL, service.baseUrl)
                        }
                        "SERVICE" -> {
                            mUrlPersistence.edit().putString(PreferencesKey.SERVICE_URL, service.baseUrl)
                        }
                        else -> {

                        }
                    }
                }
            }

            for (i in 0 until it.responseData.appSetting.payments.size) {
                val paymentData = it.responseData.appSetting.payments[i]
                when (paymentData.name.toLowerCase()) {
                    "card" -> {
                        for (j in 0 until it.responseData.appSetting.payments[i].credentials.size) {
                            val credential = it.responseData.appSetting.payments[i].credentials[j]
                            if (credential.name.toLowerCase() == "stripe_publishable_key") {
                                writePreferences(PreferencesKey.STRIPE_KEY, credential.value)
                            }
                        }

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
            else launchNewActivity(DashBoardActivity::class.java, true)
        }
    }

    private fun setPayment(it: ConfigResponseModel) {
        val paymentList = it.responseData.appSetting.payments
        writePreferences(PreferencesKey.PAYMENT_LIST, Gson().toJson(paymentList))
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