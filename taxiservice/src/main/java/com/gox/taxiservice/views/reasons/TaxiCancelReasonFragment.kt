package com.gox.taxiservice.views.reasons

import android.content.Context
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.ViewUtils
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.DialogTaxiReasonBinding
import com.gox.taxiservice.interfaces.CustomClickListener
import com.gox.taxiservice.interfaces.GetReasonsInterface
import com.gox.taxiservice.model.ReasonModel
import com.gox.taxiservice.views.adapter.TaxiReasonAdapter

class TaxiCancelReasonFragment : BaseDialogFragment<DialogTaxiReasonBinding>(),
        TaxiCancelReasonNavigator {

    private lateinit var mBinding: DialogTaxiReasonBinding
    private lateinit var mViewModel: TaxiCancelReasonViewModel
    private lateinit var getReasons: GetReasonsInterface

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as DialogTaxiReasonBinding
        mViewModel = ViewModelProviders.of(this).get(TaxiCancelReasonViewModel::class.java)
        mViewModel.navigator = this
        mBinding.lifecycleOwner = this
        mBinding.viewModel = mViewModel
        getApiResponse()
        mViewModel.getReason()
    }

    private fun getApiResponse() {
        mViewModel.mResponse.observe(this, Observer<ReasonModel> { reasonModel ->
            if (reasonModel!!.responseData != null) mBinding.taxiReasonAdapter =
                    TaxiReasonAdapter(mListener, reasonModel.responseData as List<ReasonModel.ResponseData>)
        })
    }

    private var mListener: CustomClickListener = object : CustomClickListener {
        override fun onListClickListener(position: Int) {
            getReasons.reasonForCancel(mViewModel.mResponse.value!!.responseData!!.get(position)!!.reason!!)
            dialog!!.cancel()
        }
    }

    override fun getLayout() = R.layout.dialog_taxi_reason

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

    override fun closePopup() = dialog!!.cancel()
}