package com.gox.xuberservice.reasons

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.ViewUtils
import com.gox.xuberservice.R
import com.gox.xuberservice.adapters.XuberReasonAdapter
import com.gox.xuberservice.databinding.DialogReasonBinding
import com.gox.xuberservice.interfaces.CustomClickListner
import com.gox.xuberservice.interfaces.GetReasonsInterface
import com.gox.xuberservice.model.ReasonModel

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
        mViewModel.getReason(com.gox.base.data.Constants.ModuleTypes.SERVICE)
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