package com.xjek.xuberservice.arrived_fragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseFragment
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.FragmentArrivedLayoutBinding
import com.xjek.xuberservice.xuberMainActivity.CommunicationListener
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity

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

    override fun onAttach(context: Context) {
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