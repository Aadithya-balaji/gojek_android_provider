package com.xjek.base.base

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.xjek.base.R
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.Constants.Companion.REQUEST_CHECK_SETTINGS_GPS
import com.xjek.base.utils.LocaleUtils
import com.xjek.base.utils.NetworkUtils
import com.xjek.base.utils.PermissionUtils
import com.xjek.base.utils.RunTimePermission
import com.xjek.base.views.CustomDialog

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    private lateinit var mViewDataBinding: T
    private lateinit var customDialog: CustomDialog
    private lateinit var mParentView: View
    private lateinit var context: Context
    private var locationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    val loadingObservable: MutableLiveData<Boolean>
        get() = loadingLiveData

    protected val isNetworkConnected: Boolean get() = NetworkUtils.isNetworkConnected(applicationContext)

    protected abstract fun initView(mViewDataBinding: ViewDataBinding?)

    private var mPermissionUtils: PermissionUtils? = null

    protected var runtimePermission: RunTimePermission? = null

    fun getPermissionUtil(): PermissionUtils {
        if(mPermissionUtils==null){
            mPermissionUtils = PermissionUtils()
        }
        return  mPermissionUtils!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initView(mViewDataBinding)
        context = this
        customDialog = CustomDialog(this, true)

        observeLiveData(loadingLiveData) { isShowLoading ->
            if (isShowLoading) showLoading() else hideLoading()
        }
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(parent, name, context, attrs)
        mParentView = window.decorView.findViewById(R.id.content)
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleUtils.setLocale(newBase!!))
    }

    protected fun setBindingVariable(variableId: Int, value: Any?) {
        mViewDataBinding.setVariable(variableId, value)
        mViewDataBinding.executePendingBindings()
    }

    private fun showLoading() {
        try {
            if (customDialog.window != null) {
                customDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                customDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideLoading() {
        customDialog.cancel()
    }

    protected fun launchNewActivity(cls: Class<*>, shouldCloseActivity: Boolean) {
        startActivity(Intent(applicationContext, cls))
        if (shouldCloseActivity) finish()
    }

    protected fun launchNewActivity(intent: Intent, shouldCloseActivity: Boolean) {
        startActivity(intent)
        if (shouldCloseActivity) finish()
    }

    protected fun replaceExistingFragment(@IdRes id: Int, fragment: Fragment,
                                          tag: String?, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(id, fragment, tag)
        if (addToBackStack) transaction.addToBackStack(tag)
        transaction.commit()
    }

    protected fun showSnackBar(msg: String) {
        val snackBar = Snackbar.make(mParentView, msg, Snackbar.LENGTH_LONG)
        snackBar.setActionTextColor(Color.RED)
        snackBar.show()
    }

    fun longLog(str: String, s: String) {
        if (str.length > 4000) {
            Log.d("$s::Points : ", str.substring(0, 4000))
            longLog(str.substring(4000), s)
        } else Log.d("$s::Points : ", str)
    }

    fun glideSetImageView(imageView: ImageView, image: String, placeholder: Int) {
        Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(placeholder)
                        .error(placeholder))
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
    }


    private fun checkGps() {
        locationRequest = LocationRequest.create()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = 1000
        locationRequest!!.numUpdates = 1
        locationRequest!!.fastestInterval = (5 * 1000).toLong()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED) {
            return
        }
        val settingsBuilder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        settingsBuilder.setAlwaysShow(true)
        val result = LocationServices.getSettingsClient(this).checkLocationSettings(settingsBuilder.build())
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
            } catch (ex: ApiException) {
                when (ex.statusCode) {
                    LocationSettingsStatusCodes.SUCCESS -> {
                        val permissionLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                            // mFusedLocationClient.requestLocationUpdates(locationRequest, getPendingIntent())
                        }
                    }
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvableApiException = ex as ResolvableApiException
                        resolvableApiException.startResolutionForResult(context as Activity,
                                REQUEST_CHECK_SETTINGS_GPS)
                    } catch (e: IntentSender.SendIntentException) {

                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

   /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val strTesting="boopathi"
    }*/
}