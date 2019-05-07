package com.xjek.taxiservice.views.reasons

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.xjek.base.base.BaseDialogFragment
import com.xjek.base.utils.ViewUtils
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.DialogTaxiReasonBinding
import com.xjek.taxiservice.interfaces.CustomClickListener
import com.xjek.taxiservice.interfaces.GetReasonsInterface
import com.xjek.taxiservice.model.ReasonModel
import com.xjek.taxiservice.views.adapter.TaxiReasonAdapter

class TaxiCancelReasonFragment : BaseDialogFragment<DialogTaxiReasonBinding>(),
        TaxiCancelReasonNavigator {

    private lateinit var mReasonFragmentBinding: DialogTaxiReasonBinding
    private lateinit var mViewModelTaxiCancel: TaxiCancelReasonViewModel
    private lateinit var mViewModel: TaxiCancelReasonViewModel
    private var mReasonList: ArrayList<ReasonModel>? = null
    private var mReasonType: ArrayList<String>? = null
    private var mRequestId: Int? = null
    private var mReason: String? = null
    private var mTaxiReasonAdapter: TaxiReasonAdapter? = null
    private lateinit var getReasons: GetReasonsInterface

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mReasonFragmentBinding = viewDataBinding as DialogTaxiReasonBinding
        mViewModelTaxiCancel = TaxiCancelReasonViewModel()
        mReasonFragmentBinding.lifecycleOwner = this
        getApiResponse()
        mViewModelTaxiCancel.getReason()
    }

    private fun getApiResponse() {
        mViewModelTaxiCancel.mResponse.observe(this, Observer<ReasonModel> { reasonModel ->
            if (reasonModel!!.responseData != null) mReasonFragmentBinding.taxiReasonAdapter =
                    TaxiReasonAdapter(activity!!, mListener, reasonModel.responseData as List<ReasonModel.ResponseData>)
        })
    }

    internal var mListener: CustomClickListener = object : CustomClickListener {
        override fun onListClickListener(position: Int) {
            getReasons.reasonForCancel(mViewModelTaxiCancel.mResponse.value!!.responseData!!.get(position)!!.reason!!)
            dialog!!.cancel()
        }
    }

    override fun getLayout(): Int = R.layout.dialog_taxi_reason

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getReasons = context as GetReasonsInterface
    }

    override fun showErrorMessage(errorMessage: String) {
        try {
            ViewUtils.showToast(context!!, errorMessage, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun closePopup() {

    }
}
