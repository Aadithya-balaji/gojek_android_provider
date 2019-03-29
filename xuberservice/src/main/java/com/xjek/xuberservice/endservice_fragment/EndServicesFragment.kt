package com.xjek.xuberservice.endservice_fragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseFragment
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.FragmentEndserviceLayoutBinding
import com.xjek.xuberservice.xuberMainActivity.CommunicationListener
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity


class EndServicesFragment : BaseFragment<FragmentEndserviceLayoutBinding>(), EndServicesNavigator {

    private lateinit var mCommunicationListener: CommunicationListener

    companion object {
        fun newInstance(): EndServicesFragment {
            return EndServicesFragment()
        }
    }

    lateinit var mViewDataBinding: FragmentEndserviceLayoutBinding
    override fun getLayoutId(): Int = R.layout.fragment_endservice_layout

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentEndserviceLayoutBinding

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun endService() {
        mCommunicationListener.onMessage("endservice")
    }

}
