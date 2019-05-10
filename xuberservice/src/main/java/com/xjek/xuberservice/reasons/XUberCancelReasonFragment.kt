package com.xjek.xuberservice.reasons

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.utils.ViewUtils
import com.xjek.xuberservice.R
import com.xjek.xuberservice.adapters.XuberReasonAdapter
import com.xjek.xuberservice.databinding.DialogReasonBinding
import com.xjek.xuberservice.interfaces.CustomClickListner
import com.xjek.xuberservice.interfaces.GetReasonsInterface
import com.xjek.xuberservice.model.ReasonModel

class XUberCancelReasonFragment : BaseDialogFragment<DialogReasonBinding>(), CustomClickListner, XUberCancelReasonNavigator {

    private lateinit var mReasonFragmentBinding: DialogReasonBinding
    private lateinit var mViewModel: XUberCancelReasonViewModel
    private var mReasonList: ArrayList<ReasonModel>? = null
    private var mReasonType: ArrayList<String>? = null
    private var mRequestId: Int? = null
    private var mReason: String? = null
    private var mXuberReasonAdapter: XuberReasonAdapter? = null
    private lateinit var getReasons: GetReasonsInterface

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mReasonFragmentBinding = viewDataBinding as DialogReasonBinding
        mViewModel = XUberCancelReasonViewModel()
        mViewModel.navigator = this
        mReasonFragmentBinding.lifecycleOwner = this
        getApiResponse()
        mViewModel.getReason(com.xjek.base.data.Constants.Reasons.SERVICE)
    }

    fun getApiResponse() {
        mViewModel.mReasonResponseData.observe(this, Observer<ReasonModel> { reasonModel ->
            if (reasonModel!!.responseData != null) {
                val reasonAdapter = XuberReasonAdapter(activity!!, this@XUberCancelReasonFragment,
                        reasonModel.responseData as List<ReasonModel.ResponseData>)
                mReasonFragmentBinding.reasonadapter = reasonAdapter
                mReasonFragmentBinding.llProgress.visibility = View.GONE
                mReasonFragmentBinding.reasonTypes.visibility = View.VISIBLE
            }
        })
    }

    override fun onListClickListner(position: Int) {
        val reason = mViewModel.mReasonResponseData.value!!.responseData!!.get(position)!!.reason
        getReasons.reasonForCancel(reason!!)
        dialog!!.cancel()
    }

    override fun getLayout(): Int = R.layout.dialog_reason


    override fun onAttach(context: Context) {
        super.onAttach(context)
        getReasons = context as GetReasonsInterface
    }

    override fun closePopup() {
        dialog!!.dismiss()
    }

    override fun getErrorMessage(error: String) {
        mReasonFragmentBinding.llProgress.visibility = View.GONE
        ViewUtils.showToast(activity!!, error, false)
    }
}