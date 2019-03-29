package com.xjek.xuberservice.invoice_fragment

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import com.appoets.base.base.BaseFragment
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.FragmentInvoiceLayoutBinding
import com.xjek.xuberservice.xuberMainActivity.CommunicationListener
import com.xjek.xuberservice.xuberMainActivity.XuberMainActivity


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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is XuberMainActivity) {
            mCommunicationListener = context
        }
    }

    override fun confrimPayment() {
        mCommunicationListener.onMessage("confrimpayment")

    }


}
