package com.gox.partner.views.splash

//import com.crashlytics.android.Crashlytics
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
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
import java.security.NoSuchAlgorithmException

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashViewModel.SplashNavigator {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: SplashViewModel

    public override fun getLayoutId() = R.layout.activity_splash

    private lateinit var customPreference: SharedPreferences

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        binding = mViewDataBinding as ActivitySplashBinding
        binding.lifecycleOwner = this
        viewModel = provideViewModel { SplashViewModel() }
        viewModel.navigator = this

        observeViewModel()
        generateHash()

        if (isNetworkConnected) viewModel.getConfig()
        customPreference = BaseApplication.getCustomPreference!!

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("Tag", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    println("RRR :: token = ${task.result?.token}")
//                    writePreferences(PreferencesKey.DEVICE_TOKEN, task.result?.token)
                    customPreference.edit().putString(PreferencesKey.DEVICE_TOKEN, task.result?.token).apply()
                })
    }

    @SuppressLint("CommitPrefEdits")
    private fun observeViewModel() {
        observeLiveData(viewModel.getConfigObservable()) {
            //            writePreferences("0", it.responseData.baseUrl + "/")
            customPreference.edit().putString("0", it.responseData.baseUrl + "/").apply()
            customPreference.edit().putString(PreferencesKey.BASE_ID, "0").apply()

            try {
                customPreference.edit().putString(PreferencesKey.SOS_NUMBER,
                        it.responseData.appSetting.supportDetails.contactNumber[0].number).apply()
                if (it.responseData.appSetting.otpVerify == 0)
                    customPreference.edit().putBoolean(PreferencesKey.SHOW_OTP, false).apply()
                else if (it.responseData.appSetting.otpVerify == 1)
                    customPreference.edit().putBoolean(PreferencesKey.SHOW_OTP, true).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            customPreference.run {
                edit().putString(PreferencesKey.BASE_URL, it.responseData.baseUrl).apply()
            }

            customPreference.edit().putString(PreferencesKey.BASE_CONFIG_RESPONSE, Gson().toJson(it.responseData)).apply()

            it.responseData.services.forEach { service ->
                when (service.adminServiceName) {
                    TRANSPORT -> customPreference.edit().putString(PreferencesKey.TRANSPORT_URL, service.baseUrl).apply()
                    ORDER -> customPreference.edit().putString(PreferencesKey.ORDER_URL, service.baseUrl).apply()
                    SERVICE -> customPreference.edit().putString(PreferencesKey.SERVICE_URL, service.baseUrl).apply()
                }
            }

            for (i in 0 until it.responseData.appSetting.payments.size) {
                val paymentData = it.responseData.appSetting.payments[i]
                when (paymentData.name.toLowerCase()) {
                    "card" -> {
                        for (j in 0 until it.responseData.appSetting.payments[i].credentials.size) {
                            val credential = it.responseData.appSetting.payments[i].credentials[j]
                            if (credential.name.toLowerCase() == "stripe_publishable_key") customPreference.edit().putString(PreferencesKey.STRIPE_KEY, credential.value).apply()
                        }
                    }
                }
            }

            customPreference.edit().putString("0", it.responseData.baseUrl + "/").apply()
            customPreference.edit().putInt(PreferencesKey.TRANSPORT_ID, it.responseData.services[0].id).apply()
            customPreference.edit().putString(it.responseData.services[0].id.toString(), it.responseData.services[0].baseUrl + "/").apply()
            customPreference.edit().putInt(PreferencesKey.ORDER_ID, it.responseData.services[1].id).apply()
            customPreference.edit().putString(it.responseData.services[1].id.toString(), it.responseData.services[1].baseUrl + "/").apply()
            customPreference.edit().putInt(PreferencesKey.SERVICE_ID, it.responseData.services[2].id).apply()
            customPreference.edit().putString(it.responseData.services[2].id.toString(), it.responseData.services[2].baseUrl + "/").apply()
            customPreference.edit().putString(PreferencesKey.PRIVACY_POLICY, it.responseData.appSetting.cmsPage.privacyPolicy).apply()
            customPreference.edit().putString(PreferencesKey.HELP, it.responseData.appSetting.cmsPage.help).apply()
            customPreference.edit().putString(PreferencesKey.TERMS, it.responseData.appSetting.cmsPage.terms).apply()
            val contactNumbers = hashSetOf<String>()
            for (contact in it.responseData.appSetting.supportDetails.contactNumber)
                contactNumbers.add(contact.number)
            writePreferences(PreferencesKey.CONTACT_NUMBER, contactNumbers.toSet())
            customPreference.edit().putString(PreferencesKey.CONTACT_EMAIL, it.responseData.appSetting.supportDetails.contactEmail).apply()
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
        customPreference.edit().putString(PreferencesKey.PAYMENT_LIST, Gson().toJson(it.responseData.appSetting.payments)).apply()
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
            if (Build.VERSION.SDK_INT >= 28) {
                val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNING_CERTIFICATES)
                val signatures = packageInfo.signingInfo.apkContentsSigners
                val md = MessageDigest.getInstance("SHA")
                for (signature in signatures) {
                    md.update(signature.toByteArray())
                    val signatureBase64 = String(Base64.encode(md.digest(), Base64.DEFAULT))
                    Log.d("KeyHash:: ", signatureBase64)
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

}