package com.gox.base.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.LocationManager
import android.net.Uri
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
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.material.snackbar.Snackbar
import com.gox.base.R
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.LocaleUtils
import com.gox.base.utils.PermissionUtils
import com.gox.base.utils.RunTimePermission
import com.gox.base.views.CustomDialog

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private val loadingLiveData = MutableLiveData<Boolean>()

    private lateinit var mViewDataBinding: T
    private lateinit var mCustomLoaderDialog: CustomDialog
    private lateinit var mNoInternetDialog: Dialog
    private lateinit var mParentView: View
    private lateinit var context: Context

    private var locationManager: LocationManager? = null
    private var locationRequest: LocationRequest? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    val loadingObservable: MutableLiveData<Boolean> get() = loadingLiveData

    protected abstract fun initView(mViewDataBinding: ViewDataBinding?)

    private var mPermissionUtils: PermissionUtils? = null

    protected var runtimePermission: RunTimePermission? = null

    fun getPermissionUtil(): PermissionUtils {
        if (mPermissionUtils == null) mPermissionUtils = PermissionUtils()
        return mPermissionUtils!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initView(mViewDataBinding)
        context = this
        mCustomLoaderDialog = CustomDialog(this, true)

        observeLiveData(loadingLiveData) { isShowLoading ->
            if (isShowLoading) showLoading() else hideLoading()
        }

        try {
            mNoInternetDialog = Dialog(this)
            mNoInternetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            mNoInternetDialog.setContentView(R.layout.dialog_no_internet)
            mNoInternetDialog.findViewById<AppCompatTextView>(R.id.tvSettings).setOnClickListener {
                startActivity( Intent(android.provider.Settings.ACTION_SETTINGS), null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        observeLiveData(BaseApplication.getInternetMonitorLiveData) { isInternetAvailable ->
            if (isInternetAvailable) mNoInternetDialog.dismiss() else mNoInternetDialog.show()
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
            if (mCustomLoaderDialog.window != null) {
                mCustomLoaderDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
                mCustomLoaderDialog.show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun hideLoading() = mCustomLoaderDialog.cancel()

    protected fun openActivity(cls: Class<*>, shouldCloseActivity: Boolean) {
        startActivity(Intent(context, cls))
        if (shouldCloseActivity) finish()
    }

    protected fun openActivity(intent: Intent, shouldCloseActivity: Boolean) {
        startActivity(intent)
        if (shouldCloseActivity) finish()
    }

    protected fun replaceFragment(@IdRes id: Int, fragment: Fragment, tag: String?, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(id, fragment, tag)
        if (addToBackStack) transaction.addToBackStack(tag)
        transaction.commit()
    }

    private fun showSnackBar(msg: String) {
        val snackBar = Snackbar.make(window.decorView.rootView, msg, Snackbar.LENGTH_LONG)
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
                        .circleCrop()
                        .error(placeholder))
                .load(image)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
    }

    fun glideSetImageView(imageView: ImageView, uri: Uri, placeholder: Int) {
        Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(placeholder)
                        .circleCrop()
                        .error(placeholder))
                .load(uri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView)
    }
}