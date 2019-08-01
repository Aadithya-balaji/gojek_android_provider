/*
package com.gox.partner.views.currentorder_fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseFragment
import com.gox.partner.R
import com.gox.partner.databinding.FragmentCurrentOrdersBinding
import com.gox.partner.models.TransportHistory
import com.gox.partner.views.adapters.CurrentOrdersAdapter
import com.gox.partner.views.dashboard.DashBoardViewModel

class CurrentOrderFragment : BaseFragment<FragmentCurrentOrdersBinding>(), CurrentOrderNavigator {

    lateinit var mBinding: FragmentCurrentOrdersBinding

    override fun getLayoutId() = R.layout.fragment_current_orders

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mBinding = mViewDataBinding as FragmentCurrentOrdersBinding
        val mViewModel = ViewModelProviders.of(this).get(CurrentOrderViewModel::class.java)
        val userDashboardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        mViewModel.navigator = this
        this.mBinding.currentorderviewmodel = mViewModel

        mViewModel.getTransportCurrentHistory(userDashboardViewModel
                .selectedFilterService.value!!.toLowerCase())

        loadingObservable.value = true

        mViewModel.transportCurrentHistoryResponse.observe(this@CurrentOrderFragment,
                Observer<TransportHistory> {
                    mViewModel.showLoading.value = false
                    loadingObservable.value = false

                    if (it.responseData.transport.isNotEmpty())
                        setCurrentTransportHistoryAdapter(it.responseData)
                    else {
                        this.mBinding.emptyViewLayout.visibility = View.VISIBLE
                        this.mBinding.currentOrdersfrgRv.visibility = View.GONE
                    }

                })

        mViewModel.errorResponse.observe(this@CurrentOrderFragment, Observer<String> { error ->
            loadingObservable.value = false
            this.mBinding.emptyViewLayout.visibility = View.VISIBLE
        })
    }

    private fun setCurrentTransportHistoryAdapter(responseData: TransportHistory.TransportResponseData) {
        this.mBinding.currrentOrdersAdapter = CurrentOrdersAdapter(activity,
                responseData.transport)
    }
}*/
