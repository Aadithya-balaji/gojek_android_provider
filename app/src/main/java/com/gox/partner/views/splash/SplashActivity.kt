package com.gox.partner.views.splash

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Base64
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.data.Constants.ModuleTypes.ORDER
import com.gox.base.data.Constants.ModuleTypes.SERVICE
import com.gox.base.data.Constants.ModuleTypes.TRANSPORT
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.extensions.readPreferences
import com.gox.base.extensions.writePreferences
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySplashBinding
import com.gox.partner.models.ConfigResponseModel
import com.gox.partner.utils.Constant
import com.gox.partner.views.dashboard.DashBoardActivity
import com.gox.partner.views.on_board.OnBoardActivity
import java.security.MessageDigest

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashViewModel.SplashNavigator {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    public override fun getLayoutId() = R.layout.activity_splash

    private lateinit var customPrefrence: SharedPreferences

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySplashBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel { SplashViewModel() }
        viewModel.navigator = this

        observeViewModel()
        generateHash()

        if (isNetworkConnected) viewModel.getConfig()
        customPrefrence = BaseApplication.getCustomPreference!!

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("Tag", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    println("RRR :: token = ${task.result?.token}")
//                    writePreferences(PreferencesKey.DEVICE_TOKEN, task.result?.token)
                    customPrefrence.edit().putString(PreferencesKey.DEVICE_TOKEN, task.result?.token).apply()
                })
    }

    @SuppressLint("CommitPrefEdits")
    private fun observeViewModel() {
        observeLiveData(viewModel.getConfigObservable()) {
            //            writePreferences("0", it.responseData.baseUrl + "/")
            customPrefrence.edit().putString("0", it.responseData.baseUrl + "/").apply()
            customPrefrence.edit().putString(PreferencesKey.BASE_ID, "0").apply()

            try {
                customPrefrence.edit().putString(PreferencesKey.SOS_NUMBER, it.responseData.appSetting.supportDetails.contactNumber[0].number).apply()
                if (it.responseData.appSetting.otpVerify == 0) customPrefrence.edit().putBoolean(PreferencesKey.SHOW_OTP, false).apply()
                else if (it.responseData.appSetting.otpVerify == 1) customPrefrence.edit().putBoolean(PreferencesKey.SHOW_OTP, true).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            customPrefrence.run {
                edit().putString(PreferencesKey.BASE_URL, it.responseData.baseUrl).apply()
            }

            customPrefrence.edit().putString(PreferencesKey.BASE_CONFIG_RESPONSE, Gson().toJson(it.responseData)).apply()

            it.responseData.services.forEach { service ->
                when (service.adminServiceName) {
                    TRANSPORT -> customPrefrence.edit().putString(PreferencesKey.TRANSPORT_URL, service.baseUrl).apply()
                    ORDER -> customPrefrence.edit().putString(PreferencesKey.ORDER_URL, service.baseUrl).apply()
                    SERVICE -> customPrefrence.edit().putString(PreferencesKey.SERVICE_URL, service.baseUrl).apply()
                }
            }

            for (i in 0 until it.responseData.appSetting.payments.size) {
                val paymentData = it.responseData.appSetting.payments[i]
                when (paymentData.name.toLowerCase()) {
                    "card" -> {
                        for (j in 0 until it.responseData.appSetting.payments[i].credentials.size) {
                            val credential = it.responseData.appSetting.payments[i].credentials[j]
                            if (credential.name.toLowerCase() == "stripe_publishable_key") customPrefrence.edit().putString(PreferencesKey.STRIPE_KEY, credential.value).apply()
                        }
                    }
                }
            }

            customPrefrence.edit().putString("0", it.responseData.baseUrl + "/").apply()
            customPrefrence.edit().putInt(PreferencesKey.TRANSPORT_ID, it.responseData.services[0].id).apply()
            customPrefrence.edit().putString(it.responseData.services[0].id.toString(), it.responseData.services[0].baseUrl + "/").apply()
            customPrefrence.edit().putInt(PreferencesKey.ORDER_ID, it.responseData.services[1].id).apply()
            customPrefrence.edit().putString(it.responseData.services[1].id.toString(), it.responseData.services[1].baseUrl + "/").apply()
            customPrefrence.edit().putInt(PreferencesKey.SERVICE_ID, it.responseData.services[2].id).apply()
            customPrefrence.edit().putString(it.responseData.services[2].id.toString(), it.responseData.services[2].baseUrl + "/").apply()
            customPrefrence.edit().putString(PreferencesKey.PRIVACY_POLICY, it.responseData.appSetting.cmsPage.privacyPolicy).apply()
            customPrefrence.edit().putString(PreferencesKey.HELP, it.responseData.appSetting.cmsPage.help).apply()
            customPrefrence.edit().putString(PreferencesKey.TERMS, it.responseData.appSetting.cmsPage.terms).apply()
            val contactNumbers = hashSetOf<String>()
            for (contact in it.responseData.appSetting.supportDetails.contactNumber)
                contactNumbers.add(contact.number)
            writePreferences(PreferencesKey.CONTACT_NUMBER, contactNumbers.toSet())
            customPrefrence.edit().putString(PreferencesKey.CONTACT_EMAIL, it.responseData.appSetting.supportDetails.contactEmail).apply()
            setLanguage(it)
            setPayment(it)

            Constant.privacyPolicyUrl = it.responseData.appSetting.cmsPage.privacyPolicy
            Constant.termsUrl = it.responseData.appSetting.cmsPage.terms

            if (readPreferences(PreferencesKey.ACCESS_TOKEN, "")!! == "")
                launchNewActivity(OnBoardActivity::class.java, true)
            else launchNewActivity(DashBoardActivity::class.java, true)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun setPayment(it: ConfigResponseModel) {
        customPrefrence.edit().putString(PreferencesKey.PAYMENT_LIST, Gson().toJson(it.responseData.appSetting.payments)).apply()
    }

    private fun setLanguage(it: ConfigResponseModel) {
        val languages = it.responseData.appSetting.languages
        if (languages.isNotEmpty()) Constant.languages = languages
        else Constant.languages = listOf(ConfigResponseModel.ResponseData.AppSetting.Language("English", "en"))
    }

    override fun showError(error: String) {
        finish()
    }

    private fun generateHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: Exception) {
            Log.d("_genratehash", e.message)
            e.printStackTrace()
        }

    }
}