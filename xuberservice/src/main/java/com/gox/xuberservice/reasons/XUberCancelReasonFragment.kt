package com.gox.xuberservice.reasons

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.ViewUtils
import com.gox.xuberservice.R
import com.gox.xuberservice.adapters.XUberReasonAdapter
import com.gox.xuberservice.databinding.DialogReasonBinding
import com.gox.xuberservice.interfaces.CustomClickListener
import com.gox.xuberservice.interfaces.GetReasonsInterface
import com.gox.xuberservice.model.ReasonModel

class XUberCancelReasonFragment : BaseDialogFragment<DialogReasonBinding>(),
        CustomClickListener, XUberCancelReasonNavigator {

    private lateinit var mReasonFragmentBinding: DialogReasonBinding
    private lateinit var mViewModel: XUberCancelReasonViewModel
    private lateinit var getReasons: GetReasonsInterface

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mReasonFragmentBinding = viewDataBinding as DialogReasonBinding
        mViewModel = XUberCancelReasonViewModel()
        mViewModel.navigator = this
        mReasonFragmentBinding.lifecycleOwner = this
        getApiResponse()
        mViewModel.getReason()
    }

    fun getApiResponse() =
            mViewModel.mReasonResponseData.observe(this, Observer<ReasonModel> { reasonModel ->
                if (reasonModel!!.responseData != null) {
                    val reasonAdapter = XUberReasonAdapter(this,
                            reasonModel.responseData as List<ReasonModel.ResponseData>)
                    mReasonFragmentBinding.reasonadapter = reasonAdapter
                    mReasonFragmentBinding.llProgress.visibility = View.GONE
                    mReasonFragmentBinding.reasonTypes.visibility = View.VISIBLE
                }
            })

    override fun onListClickListener(position: Int) {
        getReasons.reasonForCancel(mViewModel.mReasonResponseData.value!!.responseData!![position]!!.reason!!)
        dialog!!.cancel()
    }

    override fun getLayout()= R.layout.dialog_reason

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getReasons = context as GetReasonsInterface
    }

    override fun closePopup() = dialog!!.dismiss()

    override fun getErrorMessage(error: String) {
        mReasonFragmentBinding.llProgress.visibility = View.GONE
        ViewUtils.showToast(activity!!, error, false)
    }
}