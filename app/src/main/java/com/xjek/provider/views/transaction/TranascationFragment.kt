package com.xjek.provider.views.transaction

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.xjek.base.base.BaseFragment
import com.xjek.base.extensions.observeLiveData
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentTransactionBinding
import com.xjek.provider.views.adapters.TransactionListAdapter

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

        //callGetTrancation Api
        transcationViewModel.callTranscationApi()

        //getApiResponse
        getApiResponse()
    }

    fun getApiResponse() {
        observeLiveData(transcationViewModel.transcationLiveResponse) {
            var walletTransactionList=transcationViewModel.transcationLiveResponse
            if(walletTransactionList.value==null){
                Log.e("wallet","------null")
            }else{
                Log.e("wallet","------non null")

            }
            if (transcationViewModel.transcationLiveResponse!= null) {
                var transcationlist = transcationViewModel.transcationLiveResponse.value!!.getResponseData()!!.data
                transactionListAdapter = TransactionListAdapter(activity!!, transcationlist)
                fragmentTransactionBinding.transactionListRv.adapter=transactionListAdapter
            }
        }
    }
}
