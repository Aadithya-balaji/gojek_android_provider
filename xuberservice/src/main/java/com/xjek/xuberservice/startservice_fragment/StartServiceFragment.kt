package com.xjek.xuberservice.startservice_fragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseFragment
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.FragmentStartservicesLayoutBinding
import com.xjek.xuberservice.xuberMainActivity.CommunicationListener
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun startService() {
        mCommunicationListener.onMessage("startservice")

    }


}