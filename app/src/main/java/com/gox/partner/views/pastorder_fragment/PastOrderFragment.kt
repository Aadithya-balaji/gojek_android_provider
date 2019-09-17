package com.gox.partner.views.pastorder_fragment

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gox.base.base.BaseFragment
import com.gox.base.data.Constants
import com.gox.partner.R
import com.gox.partner.databinding.FragmentPastOrdersBinding
import com.gox.partner.models.HistoryModel
import com.gox.partner.models.TransportResponseData
import com.gox.partner.utils.Constant
import com.gox.partner.views.adapters.PastOrdersAdapter
import com.gox.partner.views.dashboard.DashBoardViewModel

class PastOrderFragment : BaseFragment<FragmentPastOrdersBinding>(), PastOrderNavigator {
    lateinit var mViewDataBinding: FragmentPastOrdersBinding
    override fun getLayoutId(): Int = R.layout.fragment_past_orders;
    var transportResponseData: TransportResponseData = TransportResponseData(0, mutableListOf(), mutableListOf(), mutableListOf(), "")
    private var offset = 0
    private var loadMore = true

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentPastOrdersBinding
        val pastOrderViewModel = PastOrderViewModel()
        val userDashboardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        pastOrderViewModel.navigator = this
        mViewDataBinding.pastfragmentviewmodel = pastOrderViewModel
        pastOrderViewModel.getTransportPastHistory(userDashboardViewModel.selectedFilterService.value!!.toLowerCase()
                , offset.toString())
        loadingObservable.value = true
        pastOrderViewModel.historyResponseLiveData.observe(this@PastOrderFragment, Observer<HistoryModel> {

        })



        pastOrderViewModel.historyResponseLiveData.observe(this@PastOrderFragment,
                Observer<HistoryModel> {
                    loadingObservable.value = false
                    if (it.responseData!!.type.equals(Constants.ModuleTypes.TRANSPORT, true)
                            && !it.responseData.transport!!.isEmpty()) {
                        loadMore = true
                        offset += 10
                        transportResponseData.transport.addAll(it.responseData.transport)
                        setTransportHistoryAdapter(Constants.ModuleTypes.TRANSPORT)
                    } else if (it.responseData.type.equals(Constants.ModuleTypes.SERVICE, true)
                            && !it.responseData.service.isEmpty()) {
                        loadMore = true
                        offset += 10
                        transportResponseData.service.addAll(it.responseData.service)
                        setTransportHistoryAdapter(Constants.ModuleTypes.SERVICE)
                    } else if (it.responseData.type.equals(Constants.ModuleTypes.ORDER, true) && !it.responseData.order.isEmpty()) {
                        loadMore = true
                        offset += 10
                        transportResponseData.order.addAll(it.responseData.order)
                        setTransportHistoryAdapter(Constants.ModuleTypes.ORDER)
                    }
                    when (transportResponseData.order.size + transportResponseData.service.size + transportResponseData.transport.size > 0) {
                        false -> {
                            this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
                            this.mViewDataBinding.pastOrdersfrgRv.visibility = View.GONE
                        }
                    }
                })

        pastOrderViewModel.errorResponse.observe(this@PastOrderFragment, Observer<String> { error ->
            loadingObservable.value = false
            when (transportResponseData.order.size + transportResponseData.service.size + transportResponseData.transport.size > 0) {
                false -> {
                    this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
                    this.mViewDataBinding.pastOrdersfrgRv.visibility = View.GONE
                }
            }
            Log.d("_D", error + "")
//            ViewUtils.showToast(activity as Context, error, false)
        })

        mViewDataBinding.pastOrdersfrgRv.addOnScrollListener(object : PaginationScrollListener(mViewDataBinding.pastOrdersfrgRv.layoutManager as LinearLayoutManager) {
            override fun isLastPage() = false
            override fun isLoading() = false
            override fun loadMoreItems() {
                when (loadMore) {
                    true -> {
                        pastOrderViewModel.getTransportPastHistory(userDashboardViewModel.selectedFilterService.value!!.toLowerCase(), offset.toString())
                        loadMore = false
                    }
                }
            }
        })
    }

    private fun setTransportHistoryAdapter(servicetype: String) {
        this.mViewDataBinding.pastOrdersAdapter = PastOrdersAdapter(activity, transportResponseData, servicetype)
    }

    override fun gotoDetailPage() {

    }
}
