package com.gox.partner.views.transaction

import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.gox.base.base.BaseFragment
import com.gox.base.extensions.observeLiveData
import com.gox.partner.R
import com.gox.partner.databinding.FragmentTransactionBinding
import com.gox.partner.models.TransactionDatum
import com.gox.partner.views.adapters.TransactionListAdapter
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
        transcationViewModel.showLoading=loadingObservable as MutableLiveData<Boolean>
        //getApiResponse
        getApiResponse()
    }

    private fun getApiResponse() {
        observeLiveData(transcationViewModel.transcationLiveResponse) {
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

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
            transcationViewModel.callTranscationApi()
    }
}

