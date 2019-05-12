package com.xjek.provider.views.pastorder_fragment

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentPastOrdersBinding
import com.xjek.provider.models.HistoryModel
import com.xjek.provider.views.adapters.PastOrdersAdapter
import com.xjek.xjek.ui.pastorder_fragment.PastOrderNavigator
import com.xjek.xjek.ui.pastorder_fragment.PastOrderViewModel

class PastOrderFragment : BaseFragment<FragmentPastOrdersBinding>(), PastOrderNavigator {


    private lateinit var mViewDataBinding: FragmentPastOrdersBinding
    private lateinit var mViewModel: PastOrderViewModel
    override fun getLayoutId(): Int = R.layout.fragment_past_orders

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentPastOrdersBinding
        val pastOrderViewModel = PastOrderViewModel()
        pastOrderViewModel.navigator = this
        mViewDataBinding.pastfragmentviewmodel = pastOrderViewModel


        pastOrderViewModel.transportHistoryResponse.observe(this@PastOrderFragment,
                Observer<HistoryModel> {
                    pastOrderViewModel.loadingProgress.value = false
                    hideLoading()
                   /* if (it.responseData.type.equals("transport", true) && !it.responseData.transport.isEmpty()) {
                        setTransportHistoryAdapter(it.responseData, "transport")
                    } else if (it.responseData.type.equals("service", true) && !it.responseData.service.isEmpty()) {
                        setTransportHistoryAdapter(it.responseData, "service")
                    } else if (!it.responseData.data.isEmpty()) {
                        setTransportHistoryAdapter(it.responseData.data, "order")
                    } else {
                        this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE

                        this.mViewDataBinding.pastOrdersfrgRv.visibility = View.GONE
                    }
*/
                })

        pastOrderViewModel.errorResponse.observe(this@PastOrderFragment, Observer<String> { error ->
            hideLoading()
            this.mViewDataBinding.emptyViewLayout.visibility = View.VISIBLE
            Log.d("_D", error + "")
//            ViewUtils.showToast(activity as Context, error, false)

        })


    }

   /* private fun setTransportHistoryAdapter(transportHistoryresponseData: List<Transport>, servicetype: String) {
        this.mViewDataBinding.pastOrdersAdapter = PastOrdersAdapter(activity,
                transportHistoryresponseData, servicetype)

    }
*/
    override fun gotoDetailPage() {
    }
}