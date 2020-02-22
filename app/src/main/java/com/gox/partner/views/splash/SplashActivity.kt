package com.gox.partner.views.splash

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.util.Base64
import android.util.Log
import androidx.databinding.ViewDataBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.data.Constants
import com.gox.base.data.Constants.ModuleTypes.ORDER
import com.gox.base.data.Constants.ModuleTypes.SERVICE
import com.gox.base.data.Constants.ModuleTypes.TRANSPORT
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.provideViewModel
import com.gox.base.extensions.readPreferences
import com.gox.base.extensions.writePreferences
import com.gox.base.utils.LocaleUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivitySplashBinding
import com.gox.partner.models.ConfigResponseModel
import com.gox.partner.models.Language
import com.gox.partner.utils.Constant
import com.gox.partner.views.dashboard.DashBoardActivity
import com.gox.partner.views.on_board.OnBoardActivity
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class SplashActivity : BaseActivity<ActivitySplashBinding>(), SplashViewModel.SplashNavigator {

    private lateinit var mBinding: ActivitySplashBinding
    private lateinit var mViewModel: SplashViewModel

    public override fun getLayoutId() = R.layout.activity_splash

    private lateinit var customPreference: SharedPreferences

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivitySplashBinding
        mBinding.lifecycleOwner = this
        mViewModel = provideViewModel { SplashViewModel() }
        mViewModel.navigator = this

        generateHash()

        LocaleUtils.setNewLocale(this, LocaleUtils.getLanguagePref(this)!!)
        observeViewModel()

        if (isNetworkConnected(this)) mViewModel.getConfig()

        customPreference = BaseApplication.getCustomPreference!!

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        return@OnCompleteListener
                    }
                    customPreference.edit().putString(PreferencesKey.DEVICE_TOKEN, task.result?.token).apply()
                })
    }

    @SuppressLint("CommitPrefEdits")
    private fun observeViewModel() {
        observeLiveData(mViewModel.configLiveData) {
            customPreference.edit().putString("0", it.responseData.base_url + "/").apply()
            customPreference.edit().putString(PreferencesKey.BASE_ID, "0").apply()
            customPreference.edit().putString(PreferencesKey.ALTERNATE_MAP_KEY, it.responseData.appsetting.android_key).apply()
            customPreference.edit().putLong(PreferencesKey.PROVIDER_NEGATIVE_BALANCE, it.responseData.appsetting.provider_negative_balance).apply()

            try {
                customPreference.edit().putString(PreferencesKey.SOS_NUMBER,
                        it.responseData.appsetting.supportdetails.contact_number[0].number).apply()
                customPreference.edit().putBoolean(PreferencesKey.SEND_SMS, it.responseData.appsetting.send_sms == 1).apply()
                customPreference.edit().putBoolean(PreferencesKey.SEND_EMAIL, it.responseData.appsetting.send_email == 1).apply()
                customPreference.edit().putBoolean(PreferencesKey.RIDE_OTP, it.responseData.appsetting.ride_otp == 1).apply()
                customPreference.edit().putBoolean(PreferencesKey.SERVICE_OTP, it.responseData.appsetting.service_otp == 1).apply()
                customPreference.edit().putBoolean(PreferencesKey.ORDER_OTP, it.responseData.appsetting.order_otp == 1).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            customPreference.run {
                edit().putString(PreferencesKey.BASE_URL, it.responseData.base_url).apply()
            }

            customPreference.edit().putString(PreferencesKey.BASE_CONFIG_RESPONSE, Gson().toJson(it.responseData)).apply()

            it.responseData.services.forEach { service ->
                when (service.admin_service) {
                    TRANSPORT -> {
                        customPreference.edit().putString(PreferencesKey.TRANSPORT_ID, service.admin_service).apply()
                        customPreference.edit().putString(PreferencesKey.TRANSPORT_URL, service.base_url).apply()
                        customPreference.edit().putString(service.id.toString(),
                                service.base_url + "/").apply()
                    }
                    ORDER -> {
                        customPreference.edit().putString(PreferencesKey.ORDER_ID, service.admin_service).apply()
                        customPreference.edit().putString(PreferencesKey.ORDER_URL, service.base_url).apply()
                        customPreference.edit().putString(service.id.toString(),
                                service.base_url + "/").apply()
                    }
                    SERVICE -> {
                        customPreference.edit().putString(PreferencesKey.SERVICE_ID, service.admin_service).apply()
                        customPreference.edit().putString(PreferencesKey.SERVICE_URL, service.base_url).apply()
                        customPreference.edit().putString(service.id.toString(),
                                service.base_url + "/").apply()
                    }
                }
            }

            for (i in 0 until it.responseData.appsetting.payments.size) {
                val paymentData = it.responseData.appsetting.payments[i]
                when (paymentData.name.toLowerCase()) {
                    "card" -> for (j in 0 until it.responseData.appsetting.payments[i].credentials.size) {
                        val credential = it.responseData.appsetting.payments[i].credentials[j]
                        if (credential.name.toLowerCase() == "stripe_publishable_key")
                            customPreference.edit().putString(PreferencesKey.STRIPE_KEY, credential.value).apply()
                    }
                }
            }


            customPreference.edit().putString(PreferencesKey.PRIVACY_POLICY,
                    it.responseData.appsetting.cmspage.privacypolicy).apply()
            customPreference.edit().putString(PreferencesKey.HELP, it.responseData.appsetting.cmspage.help).apply()
            customPreference.edit().putString(PreferencesKey.TERMS, it.responseData.appsetting.cmspage.terms).apply()

            val contactNumbers = hashSetOf<String>()
            for (contact in it.responseData.appsetting.supportdetails.contact_number)
                contactNumbers.add(contact.number)
            writePreferences(PreferencesKey.CONTACT_NUMBER, contactNumbers.toSet())
            customPreference.edit().putString(PreferencesKey.CONTACT_EMAIL,
                    it.responseData.appsetting.supportdetails.contact_email).apply()

            setLanguage(it)
            setPayment(it)

            Constants.privacyPolicyUrl = it.responseData.appsetting.cmspage.privacypolicy
            Constants.termsUrl = it.responseData.appsetting.cmspage.terms

            if (readPreferences(PreferencesKey.ACCESS_TOKEN, "")!! == "")
                openActivity(OnBoardActivity::class.java, true)
            else openActivity(DashBoardActivity::class.java, true)

//            openActivity(MainActivity::class.java, true)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun setPayment(it: ConfigResponseModel) =
            customPreference.edit().putString(PreferencesKey.PAYMENT_LIST,
                    Gson().toJson(it.responseData.appsetting.payments)).apply()

    private fun setLanguage(it: ConfigResponseModel) {
        val languages = it.responseData.appsetting.languages
        if (languages.isNotEmpty()) Constant.languages = languages
        else Constant.languages = listOf(Language("English", "en"))
    }

    override fun showError(error: String) = finish()

    private fun generateHash() {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                val packageInfo = packageManager.getPackageInfo(packageName,
                        PackageManager.GET_SIGNING_CERTIFICATES)
                val signatures = packageInfo.signingInfo.apkContentsSigners
                val md = MessageDigest.getInstance("SHA")
                for (signature in signatures) {
                    md.update(signature.toByteArray())
                    val signatureBase64 = String(Base64.encode(md.digest(), Base64.DEFAULT))
                    Log.d("KEY HASH: ", signatureBase64)
                }
            }else{
                val info = packageManager.getPackageInfo(
                        packageName,
                        PackageManager.GET_SIGNATURES)
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    Log.d("KEY HASH: ", Base64.encodeToString(md.digest(), Base64.DEFAULT))
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
        val notificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.cancelAll()
    }

    private fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isAvailable && netInfo.isConnected
    }

}