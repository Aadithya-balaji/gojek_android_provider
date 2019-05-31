package com.gox.partner.views.pastorder_fragment

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseFragment
import com.gox.partner.R
import com.gox.partner.databinding.FragmentPastOrdersBinding
import com.gox.partner.interfaces.ServiceTypeListener
import com.gox.partner.models.HistoryModel
import com.gox.partner.views.adapters.PastOrdersAdapter
import com.gox.partner.views.dashboard.DashBoardActivity
import com.gox.partner.views.dashboard.DashBoardViewModel
import com.gox.xjek.ui.pastorder_fragment.PastOrderNavigator
import com.gox.xjek.ui.pastorder_fragment.PastOrderViewModel

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
                    pastOrderViewModel.taxiList.value?.clear()
                    pastOrderViewModel.serviceList.value?.clear()
                    pastOrderViewModel.orderList.value?.clear()

                    if (it.responseData!!.type.equals("transport", true) && !it.responseData!!.transport!!.isEmpty()) {
                        if (!it.responseData.transport.isNullOrEmpty()) {
                            this.mViewDataBinding.emptyViewLayout.visibility = View.GONE
                            pastOrderViewModel.taxiList.value!!.addAll(it.responseData.transport)
                            settHistoryAdapter(activity!! as DashBoardActivity, pastOrderViewModel.taxiList.value!!, ArrayList<HistoryModel.ResponseData.Service>(), ArrayList<HistoryModel.ResponseData.Order>(), pastOrderViewModel.selectedServiceType.value!!)
                        }else{
                            this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
                        }
                    } else if (it.responseData.type.equals("service", true) && !it.responseData!!.service!!.isEmpty()) {
                        if (!it.responseData.service.isNullOrEmpty()) {
                            this.mViewDataBinding.emptyViewLayout.visibility = View.GONE
                            pastOrderViewModel.serviceList.value!!.addAll(it.responseData.service)
                            settHistoryAdapter(activity!! as DashBoardActivity, ArrayList(), pastOrderViewModel.serviceList.value!!, ArrayList(), pastOrderViewModel.selectedServiceType.value!!)
                        }else{
                            this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
                        }
                    } else if (!it.responseData.order!!.isEmpty()) {
                        if (!it.responseData.order.isNullOrEmpty()) {
                            this.mViewDataBinding.emptyViewLayout.visibility = View.GONE
                            pastOrderViewModel.orderList.value!!.addAll(it.responseData.order)
                            settHistoryAdapter(activity!! as DashBoardActivity, ArrayList<HistoryModel.ResponseData.Transport>(), ArrayList<HistoryModel.ResponseData.Service>(), pastOrderViewModel.orderList.value!!, pastOrderViewModel.selectedServiceType.value!!)
                        }else{
                            this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
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