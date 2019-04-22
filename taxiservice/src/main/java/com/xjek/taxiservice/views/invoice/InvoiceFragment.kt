package com.xjek.taxiservice.views.invoice

import android.view.View
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseFragment
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.FragmentInvoiceBinding
import kotlinx.android.synthetic.main.layout_status_indicators.*

class InvoiceFragment : BaseFragment<FragmentInvoiceBinding>(), InvoiceNavigator {

    private var activityInvoiceBinding: FragmentInvoiceBinding? = null

    companion object {
        fun newInstance(): InvoiceFragment {
            return InvoiceFragment()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_invoice

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        activityInvoiceBinding = mViewDataBinding as FragmentInvoiceBinding
        val invoiceModule = InvoiceModule()
        invoiceModule.navigator = this
        activityInvoiceBinding!!.invoicemodel = invoiceModule

        rl_status_unselected.visibility = View.GONE
        rl_status_selected.visibility = View.VISIBLE
    }

    override fun openRatingDialog() {
//        val ratingFragment = RatingFragment()
//        ratingFragment.show(supportFragmentManager, "rating")
//        ratingFragment.isCancelable = false
    }

}
