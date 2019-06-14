package com.gox.partner.views.transaction

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.gox.base.base.BaseFragment
import com.gox.base.extensions.observeLiveData
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.FragmentTransactionBinding
import com.gox.partner.models.WalletTransaction
import com.gox.partner.views.adapters.TransactionListAdapter
import kotlinx.android.synthetic.main.fragment_transaction.*

class TransactionFragment : BaseFragment<FragmentTransactionBinding>(), TransactionNavigator {

    private lateinit var mBinding: FragmentTransactionBinding
    private lateinit var mViewModel: TransactionViewModel

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var transactionListAdapter: TransactionListAdapter

    override fun getLayoutId() = R.layout.fragment_transaction

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as FragmentTransactionBinding
        mViewModel = TransactionViewModel()
        mViewModel.navigator = this
        mBinding.transctionmodel = mViewModel
        linearLayoutManager = LinearLayoutManager(activity)
        mBinding.transactionListRv.layoutManager = linearLayoutManager
        mBinding.lifecycleOwner = this
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>
        getApiResponse()
    }

    private fun getApiResponse() {
        observeLiveData(mViewModel.transactionLiveResponse) {
            if (it.responseData.data.isNotEmpty()) {
                contentMain.visibility = View.VISIBLE
                llEmptyView.visibility = View.GONE
            } else {
                contentMain.visibility = View.GONE
                llEmptyView.visibility = View.VISIBLE
            }

            val transactionList: List<WalletTransaction.ResponseData.Data> =
                    mViewModel.transactionLiveResponse.value!!.responseData.data
            transactionListAdapter = TransactionListAdapter(activity!!, transactionList)
            mBinding.transactionListRv.adapter = transactionListAdapter
        }

        observeLiveData(mViewModel.errorResponse) {
            ViewUtils.showToast(activity!!, it, false)
            contentMain.visibility = View.GONE
            llEmptyView.visibility = View.VISIBLE
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser)
            mViewModel.callTransactionApi()
    }
}