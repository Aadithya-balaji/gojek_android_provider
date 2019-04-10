package com.xjek.base.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.xjek.base.R
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.utils.NetworkUtils
import com.xjek.base.views.CustomDialog
import com.xjek.basemodule.utils.PermissionUtils

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    private val loadingLiveData = MutableLiveData<Boolean>()
    private lateinit var mViewDataBinding: T
    private lateinit var customDialog: CustomDialog
    private  lateinit var mParentView: View

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected val loadingObservable: MutableLiveData<*>
        get() = loadingLiveData

    protected val isNetworkConnected: Boolean
        get() = NetworkUtils.isNetworkConnected(applicationContext)

    protected abstract fun initView(mViewDataBinding: ViewDataBinding?)

    protected val mPermissionUtils: PermissionUtils? = null

    fun getPermissionUtil(): PermissionUtils {
        return if (mPermissionUtils == null) PermissionUtils() else mPermissionUtils
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        initView(mViewDataBinding)
        customDialog = CustomDialog(this)

        observeLiveData(loadingLiveData) { isShowLoading ->
            if (isShowLoading) {
                showLoading()
            }else{
                hideLoading()
            }
        }
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(parent, name, context, attrs)
        mParentView=window.decorView.findViewById(R.id.content)
    }

    protected fun setBindingVariable(variableId: Int, value: Any?) {
        mViewDataBinding.setVariable(variableId, value)
        mViewDataBinding.executePendingBindings()
    }

    protected fun showLoading() {
        if (customDialog.window != null) {
            customDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            customDialog.show()
        }
    }

    protected fun hideLoading() {
        customDialog.cancel()
    }

    protected fun launchNewActivity(cls: Class<*>, shouldCloseActivity: Boolean) {
        startActivity(Intent(applicationContext, cls))
        if (shouldCloseActivity)
            finish()
    }

    protected fun launchNewActivity(intent: Intent, shouldCloseActivity: Boolean) {
        startActivity(intent)
        if (shouldCloseActivity)
            finish()
    }

    protected fun replaceExistingFragment(@IdRes containerViewId: Int, fragment: Fragment,
                                          tag: String?, doRememberTransaction: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(containerViewId, fragment, tag)
        if (doRememberTransaction)
            transaction.addToBackStack(tag)
        transaction.commit()
    }

    protected  fun showSnackBar(msg:String){
        val snackbar = Snackbar.make(mParentView, msg, Snackbar.LENGTH_LONG)
        snackbar.setActionTextColor(Color.RED)
        snackbar.show()
    }
}
