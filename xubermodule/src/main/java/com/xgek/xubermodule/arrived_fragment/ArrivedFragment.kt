package com.xgek.xubermodule.arrived_fragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseFragment
import com.xgek.xubermodule.R
import com.xgek.xubermodule.databinding.FragmentArrivedLayoutBinding
import com.xgek.xubermodule.xuberMainActivity.CommunicationListener
import com.xgek.xubermodule.xuberMainActivity.XuberMainActivity


class ArrivedFragment : BaseFragment<FragmentArrivedLayoutBinding>(), ArrivedFragmentNavigator {

    private lateinit var mCommunicationListener: CommunicationListener

    companion object {
        fun newInstance(): ArrivedFragment {
            return ArrivedFragment()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_arrived_layout

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {


    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun goToArrivedState() {

        mCommunicationListener.onMessage("arrived")


    }

    override fun cancelService() {
    }

}