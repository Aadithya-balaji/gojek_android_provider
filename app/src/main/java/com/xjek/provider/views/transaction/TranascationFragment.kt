package com.xjek.provider.views.transaction

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import com.xjek.base.base.BaseFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentTransactionBinding

class TranascationFragment:BaseFragment<FragmentTransactionBinding>(),TransactionNavigator{
    private  lateinit var  fragmentTransactionBinding:FragmentTransactionBinding
    private  lateinit var  transcationViewModel: TransactionViewModel
    private  lateinit var  linearLayoutManager: LinearLayoutManager
    override fun getLayoutId(): Int {
        return R.layout.fragment_transaction
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        fragmentTransactionBinding=mViewDataBinding as FragmentTransactionBinding
        transcationViewModel= TransactionViewModel()
        transcationViewModel.navigator=this
        linearLayoutManager=LinearLayoutManager(activity)
    }
   }