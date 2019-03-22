package com.xgek.xubermodule.endservice_fragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseFragment
import com.xgek.xubermodule.R
import com.xgek.xubermodule.databinding.FragmentEndserviceLayoutBinding
import com.xgek.xubermodule.invoice_fragment.InvoiceFragment
import com.xgek.xubermodule.xuberMainActivity.CommunicationListener
import com.xgek.xubermodule.xuberMainActivity.XuberMainActivity


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

        this.mViewDataBinding = mViewDataBinding as  FragmentEndserviceLayoutBinding

    }
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun endService() {
        mCommunicationListener.onMessage("endservice")
    }

}
