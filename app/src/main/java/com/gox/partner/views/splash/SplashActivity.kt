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

        observeViewModel()
        generateHash()

        if (isNetworkConnected(this)) mViewModel.getConfig()

        if (BaseApplication.isNetworkAvailable) mViewModel.getConfig()

        customPreference = BaseApplication.getCustomPreference!!

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("Tag", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    println("RRR :: token = ${task.result?.token}")
                    customPreference.edit().putString(PreferencesKey.DEVICE_TOKEN, task.result?.token).apply()
                })
    }

    @SuppressLint("CommitPrefEdits")
    private fun observeViewModel() {
        observeLiveData(mViewModel.getConfigObservable()) {
            customPreference.edit().putString("0", it.responseData.base_url + "/").apply()
            customPreference.edit().putString(PreferencesKey.BASE_ID, "0").apply()
            customPreference.edit().putString(PreferencesKey.ALTERNATE_MAP_KEY, it.responseData.appsetting.android_key).apply()

            try {
                customPreference.edit().putString(PreferencesKey.SOS_NUMBER,
                        it.responseData.appsetting.supportdetails.contact_number[0].number).apply()
                if (it.responseData.appsetting.otp_verify == 0)
                    customPreference.edit().putBoolean(PreferencesKey.SHOW_OTP, false).apply()
                else if (it.responseData.appsetting.otp_verify == 1)
                    customPreference.edit().putBoolean(PreferencesKey.SHOW_OTP, true).apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            customPreference.run {
                edit().putString(PreferencesKey.BASE_URL, it.responseData.base_url).apply()
            }

            customPreference.edit().putString(PreferencesKey.BASE_CONFIG_RESPONSE, Gson().toJson(it.responseData)).apply()

            it.responseData.services.forEach { service ->
                when (service.admin_service_name) {
                    TRANSPORT -> customPreference.edit().putString(PreferencesKey.TRANSPORT_URL, service.base_url).apply()
                    ORDER -> customPreference.edit().putString(PreferencesKey.ORDER_URL, service.base_url).apply()
                    SERVICE -> customPreference.edit().putString(PreferencesKey.SERVICE_URL, service.base_url).apply()
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

            customPreference.edit().putString("0", it.responseData.base_url + "/").apply()
            customPreference.edit().putInt(PreferencesKey.TRANSPORT_ID, it.responseData.services[0].id).apply()
            customPreference.edit().putString(it.responseData.services[0].id.toString(),
                    it.responseData.services[0].base_url + "/").apply()
            customPreference.edit().putInt(PreferencesKey.ORDER_ID, it.responseData.services[1].id).apply()
            customPreference.edit().putString(it.responseData.services[1].id.toString(),
                    it.responseData.services[1].base_url + "/").apply()
            customPreference.edit().putInt(PreferencesKey.SERVICE_ID, it.responseData.services[2].id).apply()
            customPreference.edit().putString(it.responseData.services[2].id.toString(),
                    it.responseData.services[2].base_url + "/").apply()
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
                launchNewActivity(OnBoardActivity::class.java, true)
            else launchNewActivity(DashBoardActivity::class.java, true)
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