package com.xjek.provider.views.pastorder_fragment

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentPastOrdersBinding
import com.xjek.provider.interfaces.ServiceTypeListener
import com.xjek.provider.models.HistoryModel
import com.xjek.provider.views.adapters.PastOrdersAdapter
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.dashboard.DashBoardViewModel
import com.xjek.xjek.ui.pastorder_fragment.PastOrderNavigator
import com.xjek.xjek.ui.pastorder_fragment.PastOrderViewModel

class PastOrderFragment : BaseFragment<FragmentPastOrdersBinding>(), PastOrderNavigator, ServiceTypeListener {
    private lateinit var mViewDataBinding: FragmentPastOrdersBinding
    private lateinit var mViewModel: PastOrderViewModel
    private lateinit var dashBoardViewModel: DashBoardViewModel
    private lateinit var pastOrderViewModel: PastOrderViewModel
    private var serviceType: String = ""


    override fun getLayoutId(): Int = R.layout.fragment_past_orders

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentPastOrdersBinding
        pastOrderViewModel = PastOrderViewModel()
        pastOrderViewModel.navigator = this
        mViewDataBinding.pastfragmentviewmodel = pastOrderViewModel
        dashBoardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        if (pastOrderViewModel.selectedServiceType.value.isNullOrEmpty()) {
            dashBoardViewModel.selectedFilterService.value = "transport"
        }
        dashBoardViewModel.selectedFilterService.observe(activity!!, Observer<String> {
            loadingObservable.value = true
            pastOrderViewModel.getTransportPastHistory(it.toLowerCase())
            pastOrderViewModel.selectedServiceType.value = it.toLowerCase()
        })

        pastOrderViewModel.historyResponseLiveData.observe(this@PastOrderFragment,
                Observer<HistoryModel> {
                    loadingObservable.value = false
                    if (it.responseData!!.type.equals("transport", true) && !it.responseData!!.transport!!.isEmpty()) {
                        if (!it.responseData.transport.isNullOrEmpty()) {
                            pastOrderViewModel.taxiList.value!!.addAll(it.responseData.transport)
                            settHistoryAdapter(activity!! as DashBoardActivity, pastOrderViewModel.taxiList.value!!, ArrayList<HistoryModel.ResponseData.Service>(), ArrayList<HistoryModel.ResponseData.Order>(), pastOrderViewModel.selectedServiceType.value!!)
                        }
                    } else if (it.responseData.type.equals("service", true) && !it.responseData!!.service!!.isEmpty()) {
                        if (!it.responseData.service.isNullOrEmpty()) {
                            pastOrderViewModel.serviceList.value!!.addAll(it.responseData.service)
                            settHistoryAdapter(activity!! as DashBoardActivity, ArrayList(), pastOrderViewModel.serviceList.value!!, ArrayList(), pastOrderViewModel.selectedServiceType.value!!)
                        }
                    } else if (!it.responseData.order!!.isEmpty()) {
                        if (!it.responseData.order.isNullOrEmpty()) {
                            pastOrderViewModel.orderList.value!!.addAll(it.responseData.order)
                            settHistoryAdapter(activity!! as DashBoardActivity, ArrayList<HistoryModel.ResponseData.Transport>(), ArrayList<HistoryModel.ResponseData.Service>(), pastOrderViewModel.orderList.value!!, pastOrderViewModel.selectedServiceType.value!!)
                        }

                    } else {
                        this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
                    }

                })
    }

    fun settHistoryAdapter(activity: DashBoardActivity, transportList: ArrayList<HistoryModel.ResponseData.Transport>, serviceList: ArrayList<HistoryModel.ResponseData.Service>, orderList: ArrayList<HistoryModel.ResponseData.Order>, serviceType: String) {
        mViewDataBinding.pastOrdersAdapter = PastOrdersAdapter(activity, transportList, orderList, serviceList, pastOrderViewModel.selectedServiceType.value!!)
        mViewDataBinding.pastOrdersAdapter!!.notifyDataSetChanged()
    }


    override fun getServiceType(serviceType: String,selectedID:Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun gotoDetailPage() {
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        Log.e("Past", "-----" + isVisibleToUser)
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        Log.e("Past", "----- Foodie" + menuVisible)
        super.setMenuVisibility(menuVisible)

    }


}