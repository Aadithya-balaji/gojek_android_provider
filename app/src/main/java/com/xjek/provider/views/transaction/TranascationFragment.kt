package com.xjek.provider.views.transaction

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.xjek.base.base.BaseFragment
import com.xjek.base.extensions.observeLiveData
import com.xjek.provider.R
import com.xjek.provider.models.TransactionDatum
import com.xjek.provider.views.adapters.TransactionListAdapter
import kotlinx.android.synthetic.main.fragment_transaction.*

class TranascationFragment : BaseFragment<FragmentTransactionBinding>(), TransactionNavigator {
    private lateinit var fragmentTransactionBinding: FragmentTransactionBinding
    private lateinit var transcationViewModel: TransactionViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var transactionListAdapter: TransactionListAdapter
    override fun getLayoutId(): Int {
        return R.layout.fragment_transaction
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {

        fragmentTransactionBinding = mViewDataBinding as FragmentTransactionBinding
        transcationViewModel = TransactionViewModel()
        transcationViewModel.navigator = this
        fragmentTransactionBinding.transctionmodel = transcationViewModel
        linearLayoutManager = LinearLayoutManager(activity)
        fragmentTransactionBinding.transactionListRv.layoutManager = linearLayoutManager
        fragmentTransactionBinding.lifecycleOwner = this

        //callGetTrancation Api
        transcationViewModel.callTranscationApi()

        //getApiResponse
        getApiResponse()
    }

    private fun getApiResponse() {
        observeLiveData(transcationViewModel.transcationLiveResponse) {
            val walletTransactionList = transcationViewModel.transcationLiveResponse
            if (walletTransactionList.value == null) {
                Log.e("wallet", "------null")
            } else {
                Log.e("wallet", "------non null")

            }

            if (it.getResponseData()?.getData() != null && it.getResponseData()?.getData()!!.isNotEmpty()) {
                contentMain.visibility = View.VISIBLE
                llEmptyView.visibility = View.GONE
            } else {
                contentMain.visibility = View.GONE
                llEmptyView.visibility = View.VISIBLE
            }

            val transcationlist: List<TransactionDatum> = transcationViewModel.transcationLiveResponse.value!!.getResponseData()!!.getData()!!
            transactionListAdapter = TransactionListAdapter(activity!!, transcationlist)
            fragmentTransactionBinding.transactionListRv.adapter = transactionListAdapter
        }

        observeLiveData(transcationViewModel.errorResponse) {
            contentMain.visibility = View.GONE
            llEmptyView.visibility = View.VISIBLE
        }
    }
}

