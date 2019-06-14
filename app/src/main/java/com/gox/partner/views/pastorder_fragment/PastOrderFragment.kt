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

class PastOrderFragment : BaseFragment<FragmentPastOrdersBinding>(),
        PastOrderNavigator, ServiceTypeListener {

    private lateinit var mBinding: FragmentPastOrdersBinding
    private lateinit var mViewModel: PastOrderViewModel

    private lateinit var dashBoardViewModel: DashBoardViewModel

    override fun getLayoutId(): Int = R.layout.fragment_past_orders

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mBinding = mViewDataBinding as FragmentPastOrdersBinding
        mViewModel = PastOrderViewModel()
        mViewModel.navigator = this
        mViewDataBinding.pastfragmentviewmodel = mViewModel
        dashBoardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        if (mViewModel.selectedServiceType.value.isNullOrEmpty()) {
            dashBoardViewModel.selectedFilterService.value = "transport"
        }
        dashBoardViewModel.selectedFilterService.observe(activity!!, Observer<String> {
            loadingObservable.value = true
            mViewModel.getTransportPastHistory(it.toLowerCase())
            mViewModel.selectedServiceType.value = it.toLowerCase()
        })

        mViewModel.historyResponseLiveData.observe(this@PastOrderFragment,
                Observer<HistoryModel> {
                    loadingObservable.value = false
                    mViewModel.taxiList.value?.clear()
                    mViewModel.serviceList.value?.clear()
                    mViewModel.orderList.value?.clear()

                    if (it.responseData!!.type.equals("transport", true)
                            && it.responseData.transport!!.isNotEmpty()) {
                        if (!it.responseData.transport.isNullOrEmpty()) {
                            this.mBinding.emptyViewLayout.visibility = View.GONE
                            mViewModel.taxiList.value!!.addAll(it.responseData.transport)
                            settHistoryAdapter(activity!! as DashBoardActivity, mViewModel.taxiList.value!!,
                                    ArrayList(), ArrayList())
                        } else this.mBinding.emptyViewLayout.visibility = View.VISIBLE
                    } else if (it.responseData.type.equals("service", true)
                            && it.responseData.service!!.isNotEmpty()) {
                        if (!it.responseData.service.isNullOrEmpty()) {
                            this.mBinding.emptyViewLayout.visibility = View.GONE
                            mViewModel.serviceList.value!!.addAll(it.responseData.service)
                            settHistoryAdapter(activity!! as DashBoardActivity, ArrayList(),
                                    mViewModel.serviceList.value!!, ArrayList())
                        } else this.mBinding.emptyViewLayout.visibility = View.VISIBLE
                    } else if (!it.responseData.order!!.isEmpty()) {
                        if (!it.responseData.order.isNullOrEmpty()) {
                            this.mBinding.emptyViewLayout.visibility = View.GONE
                            mViewModel.orderList.value!!.addAll(it.responseData.order)
                            settHistoryAdapter(activity!! as DashBoardActivity, ArrayList(),
                                    ArrayList(), mViewModel.orderList.value!!)
                        } else this.mBinding.emptyViewLayout.visibility = View.VISIBLE

                    } else this.mBinding.emptyViewLayout.visibility = View.VISIBLE

                })
    }

    private fun settHistoryAdapter(activity: DashBoardActivity,
                                   transportList: ArrayList<HistoryModel.ResponseData.Transport>,
                                   serviceList: ArrayList<HistoryModel.ResponseData.Service>,
                                   orderList: ArrayList<HistoryModel.ResponseData.Order>) {
        mBinding.pastOrdersAdapter = PastOrdersAdapter(activity, transportList, orderList,
                serviceList, mViewModel.selectedServiceType.value!!)
        mBinding.pastOrdersAdapter!!.notifyDataSetChanged()
    }

    override fun getServiceType(serviceType: String, serviceTypeID: Int) {

    }

    override fun gotoDetailPage() {
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        Log.e("Past", "-----$isVisibleToUser")
        super.setUserVisibleHint(isVisibleToUser)
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        Log.e("Past", "----- Foodie$menuVisible")
        super.setMenuVisibility(menuVisible)

    }

}