package com.xgek.xubermodule.startservice_fragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseFragment
import com.xgek.xubermodule.R
import com.xgek.xubermodule.databinding.FragmentStartservicesLayoutBinding
import com.xgek.xubermodule.xuberMainActivity.CommunicationListener
import com.xgek.xubermodule.xuberMainActivity.XuberMainActivity

class StartServiceFragment : BaseFragment<FragmentStartservicesLayoutBinding>(), StartServiceNavigator {
    private lateinit var mCommunicationListener: CommunicationListener

    companion object {
        fun newInstance(): StartServiceFragment {
            return StartServiceFragment()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_startservices_layout

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun startService() {
        mCommunicationListener.onMessage("startservice")

    }


}