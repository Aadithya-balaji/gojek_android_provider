package com.bee.courierservice.reasons

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.bee.courierservice.R
import com.bee.courierservice.adapters.CourierReasonAdapter
import com.bee.courierservice.databinding.CDialogReasonBinding
import com.bee.courierservice.interfaces.CustomClickListener
import com.bee.courierservice.interfaces.GetReasonsInterface
import com.bee.courierservice.model.ReasonModel
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.ViewUtils

class CourierCancelReasonFragment : BaseDialogFragment<CDialogReasonBinding>(),
        CustomClickListener, CourierCancelReasonNavigator {

    private lateinit var mReasonFragmentBinding: CDialogReasonBinding
    private lateinit var mViewModel: CourierCancelReasonViewModel
    private lateinit var getReasons: GetReasonsInterface

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mReasonFragmentBinding = viewDataBinding as CDialogReasonBinding
        mViewModel = CourierCancelReasonViewModel()
        mViewModel.navigator = this
        mReasonFragmentBinding.lifecycleOwner = this
        getApiResponse()
        mViewModel.getReason()
    }

    fun getApiResponse() =
            mViewModel.mReasonResponseData.observe(this, Observer<ReasonModel> { reasonModel ->
                if (reasonModel!!.responseData != null) {
                    val reasonAdapter = CourierReasonAdapter(this,
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

    override fun getLayout()= R.layout.c_dialog_reason

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