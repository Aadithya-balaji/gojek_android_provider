package com.xjek.xuberservice.reasons

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.xjek.base.base.BaseDialogFragment
import com.xjek.xuberservice.R
import com.xjek.xuberservice.adapters.ReasonAdapter
import com.xjek.xuberservice.databinding.DialogReasonBinding
import com.xjek.xuberservice.interfaces.CustomClickListner
import com.xjek.xuberservice.interfaces.GetReasonsInterface
import com.xjek.xuberservice.model.ReasonModel

class ReasonFragment : BaseDialogFragment<DialogReasonBinding>(), CustomClickListner {

    private lateinit var mReasonFragmentBinding: DialogReasonBinding
    private lateinit var mViewModel: ReasonViewModel
    private lateinit var reasonViewModel: ReasonViewModel
    private var mReasonList: ArrayList<ReasonModel>? = null
    private var mReasonType: ArrayList<String>? = null
    private var mRequestId: Int? = null
    private var mReason: String? = null
    private var mReasonAdapter: ReasonAdapter? = null
    private lateinit var getReasons: GetReasonsInterface

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mReasonFragmentBinding = viewDataBinding as DialogReasonBinding
        mViewModel = ReasonViewModel()
        mReasonFragmentBinding.lifecycleOwner = this
        getApiResponse()
        mViewModel.getReason(com.xjek.base.data.Constants.Reasons.SERVICE)
    }

    fun getApiResponse() {
        mViewModel.mReasonResponseData.observe(this, object : Observer<ReasonModel> {
            override fun onChanged(reasonModel: ReasonModel?) {
                if (reasonModel!!.responseData != null) {
                    val reasonAdapter = ReasonAdapter(activity!!, this@ReasonFragment, reasonModel.responseData as List<ReasonModel.ResponseData>)
                    mReasonFragmentBinding.reasonadapter = reasonAdapter
                }
            }

        })
    }

    override fun onListClickListner(position: Int) {
        val reason = mViewModel.mReasonResponseData.value!!.responseData!!.get(position)!!.reason
        getReasons.reasonForCancel(reason!!)
        dialog!!.cancel()
    }

    override fun getLayout(): Int {
        return R.layout.dialog_reason
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getReasons = context as GetReasonsInterface
    }
}
