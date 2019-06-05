package com.gox.partner.views.currentorder_fragment

import android.util.Log
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
import com.gox.xjek.ui.currentorder_fragment.CurrentOrderNavigator

class CurrentOrderFragment : BaseFragment<FragmentCurrentOrdersBinding>(), CurrentOrderNavigator {


    lateinit var mViewDataBinding: FragmentCurrentOrdersBinding

    override fun getLayoutId(): Int = R.layout.fragment_current_orders

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        this.mViewDataBinding = mViewDataBinding as FragmentCurrentOrdersBinding
        val currentOrderViewModel = ViewModelProviders.of(this).get(CurrentOrderViewModel::class.java)
        val userDashboardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        currentOrderViewModel.navigator = this
        this.mViewDataBinding.currentorderviewmodel = currentOrderViewModel

        currentOrderViewModel.getTransportCurrentHistory(userDashboardViewModel
                .selectedFilterService.value!!.toLowerCase())

        loadingObservable.value = true

        currentOrderViewModel.transportCurrentHistoryResponse.observe(this@CurrentOrderFragment,
                Observer<TransportHistory> {
                    currentOrderViewModel.loadingProgress.value = false
                    loadingObservable.value = false

                    if (!it.responseData.transport.isEmpty()) {
                        setCurrentTransportHistoryAdapter(it.responseData)
                    } else {
                        this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
                        this.mViewDataBinding.currentOrdersfrgRv.visibility = View.GONE
                    }

                })

        currentOrderViewModel.errorResponse.observe(this@CurrentOrderFragment, Observer<String> { error ->
            loadingObservable.value = false
            this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE

            Log.d("_D", error + "")
//            ViewUtils.showLog(activity as Context, error, false)

        })


    }

    private fun setCurrentTransportHistoryAdapter(responseData: TransportHistory.TransportResponseData) {
        this.mViewDataBinding.currrentOrdersAdapter = CurrentOrdersAdapter(activity,
                responseData.transport)
    }


    override fun goToDetailPage() {
    }

}