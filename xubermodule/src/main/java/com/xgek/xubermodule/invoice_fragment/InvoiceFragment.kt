package com.xgek.xubermodule.invoice_fragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.basemodule.base.BaseFragment
import com.xgek.xubermodule.R
import com.xgek.xubermodule.databinding.FragmentInvoiceLayoutBinding
import com.xgek.xubermodule.ratingFragment.RatingFragment
import com.xgek.xubermodule.xuberMainActivity.CommunicationListener
import com.xgek.xubermodule.xuberMainActivity.XuberMainActivity


class InvoiceFragment : BaseFragment<FragmentInvoiceLayoutBinding>(), InvoiceNavigator {

    private lateinit var mCommunicationListener: CommunicationListener

    companion object {
        fun newInstance(): InvoiceFragment {
            return InvoiceFragment()
        }
    }

    lateinit var mViewDataBinding: FragmentInvoiceLayoutBinding
    override fun getLayoutId(): Int = R.layout.fragment_invoice_layout

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentInvoiceLayoutBinding
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun confrimPayment() {
        mCommunicationListener.onMessage("confrimpayment")

    }


}
