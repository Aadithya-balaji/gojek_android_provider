package com.xjek.provider.views.pastorder_fragment

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentPastOrdersBinding
import com.xjek.provider.model.TransportHistory
import com.xjek.provider.views.adapters.PastOrdersAdapter
import com.xjek.provider.views.dashboard.DashBoardViewModel
import com.xjek.xjek.ui.pastorder_fragment.PastOrderNavigator
import com.xjek.xjek.ui.pastorder_fragment.PastOrderViewModel

class PastOrderFragment : BaseFragment<FragmentPastOrdersBinding>(), PastOrderNavigator {


    lateinit var mViewDataBinding: FragmentPastOrdersBinding
    override fun getLayoutId(): Int = R.layout.fragment_past_orders

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentPastOrdersBinding
        val pastOrderViewModel = PastOrderViewModel()
        val userDashboardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        pastOrderViewModel.navigator = this
        mViewDataBinding.pastfragmentviewmodel = pastOrderViewModel

        pastOrderViewModel.getTransportPastHistory(userDashboardViewModel.selectedFilterService.value!!.toLowerCase())
        loadingObservable.value = true


        pastOrderViewModel.transportHistoryResponse.observe(this@PastOrderFragment,
                Observer<TransportHistory> {
                    pastOrderViewModel.loadingProgress.value = false
                    loadingObservable.value = false
                    if (!it.responseData.transport.isEmpty()) {
                        setTransportHistoryAdapter(it.responseData)
                    } else {
                        this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
                        this.mViewDataBinding.pastOrdersfrgRv.visibility = View.GONE
                    }

                })

        pastOrderViewModel.errorResponse.observe(this@PastOrderFragment, Observer<String> { error ->
            loadingObservable.value = false
            this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
            Log.d("_D", error + "")
//            ViewUtils.showToast(activity as Context, error, false)

        })


    }

    private fun setTransportHistoryAdapter(transportHistoryresponseData: TransportHistory.TransportResponseData) {
        this.mViewDataBinding.pastOrdersAdapter = PastOrdersAdapter(activity,
                transportHistoryresponseData.transport)

    }

    override fun gotoDetailPage() {
    }

}