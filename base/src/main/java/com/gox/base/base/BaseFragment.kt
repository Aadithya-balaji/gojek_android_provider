package com.gox.base.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.LocaleUtils
import com.gox.base.views.CustomDialog

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {

    private var mActivity: FragmentActivity? = null
    private lateinit var mViewDataBinding: T
    private val loadingLiveData = MutableLiveData<Boolean>()
    private var customDialog: CustomDialog? = null

    val loadingObservable: MutableLiveData<*>
        get() = loadingLiveData

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        LocaleUtils.setLocale(context)
        mActivity = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        return mViewDataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(mViewDataBinding.root, mViewDataBinding)
        customDialog = CustomDialog(mActivity!!,true)

        observeLiveData(loadingLiveData) { isShowLoading ->
            if (isShowLoading) showLoading() else hideLoading()
        }
    }

    protected fun setBindingVariable(variableId: Int, value: Any) {
        mViewDataBinding.setVariable(variableId, value)
        mViewDataBinding.executePendingBindings()
    }

    protected fun launchNewActivity(cls: Class<*>, shouldCloseActivity: Boolean) {
        startActivity(Intent(mActivity!!.applicationContext, cls))
        if (shouldCloseActivity)
            mActivity!!.finish()
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    protected fun showLoading() {
        if (customDialog!!.window != null) {
            customDialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            customDialog!!.show()
        }
    }

    protected fun hideLoading() {
        customDialog!!.cancel()
    }

}