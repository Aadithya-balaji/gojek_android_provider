package com.xjek.xuberservice.invoice

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.xjek.base.base.BaseDialogFragment
import com.xjek.xuberservice.R
import com.xjek.xuberservice.databinding.DialogInvoiceBinding
import com.xjek.xuberservice.model.UpdateRequest
import com.xjek.xuberservice.rating.DialogXuperRating

class DialogXuperInvoice : BaseDialogFragment<DialogInvoiceBinding>(), XuperInvoiceNavigator {

    private lateinit var dialogInvoiceBinding: DialogInvoiceBinding
    private lateinit var xuperInvoiceModel: XuperInvoiceViewModel
    private var updateRequestModel: UpdateRequest? = null
    private var strUpdateRequest: String? = null


    override fun getLayout(): Int {
        return R.layout.dialog_invoice
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogInvoiceBinding = viewDataBinding as DialogInvoiceBinding
        xuperInvoiceModel = XuperInvoiceViewModel()
        dialogInvoiceBinding.invoicemodel = xuperInvoiceModel
        xuperInvoiceModel.showProgress = loadingObservable as MutableLiveData<Boolean>
        getBundleValues()
        updateUi()
        getApiResponse()
    }

    fun getApiResponse() {
        xuperInvoiceModel.invoiceLiveData.observe(this, object : androidx.lifecycle.Observer<UpdateRequest> {
            override fun onChanged(updateRequest: UpdateRequest?) {
                xuperInvoiceModel.showProgress.value = false
                if (updateRequest!!.statusCode.equals("200")) {
                    val ratingDialog = DialogXuperRating()
                    ratingDialog.show(childFragmentManager, "ratingDialog")
                }
            }

        })
    }

    fun getBundleValues() {
        strUpdateRequest = if (arguments!!.containsKey("updateRequest")) arguments!!.getString("updateRequest") else ""
        updateRequestModel = Gson().fromJson("updateRequest", UpdateRequest::class.java)

    }

    fun updateUi() {
        xuperInvoiceModel.rating.value = updateRequestModel!!.responseData!!.user_rated.toString()
        xuperInvoiceModel.serviceName.value = updateRequestModel!!.responseData!!.service!!.service_name
        xuperInvoiceModel.userImage.value = updateRequestModel!!.responseData!!.user!!.picture.toString()
        xuperInvoiceModel.totalAmount.value = updateRequestModel!!.responseData!!.payment!!.payable.toString()
        xuperInvoiceModel.requestID.value = updateRequestModel!!.responseData!!.id.toString()

        Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder))
                .load(xuperInvoiceModel.userImage.value)
                .into(dialogInvoiceBinding.ivUserImg)
        dialogInvoiceBinding.tvAmountToBePaid.setText(xuperInvoiceModel.totalAmount.value)
        dialogInvoiceBinding.rbUserRating.ratingNum = xuperInvoiceModel.rating.value!!.toFloat()
        dialogInvoiceBinding.tvXuperService.setText(xuperInvoiceModel.serviceName.value)

    }

    override fun showErrorMessage(error: String) {
        xuperInvoiceModel.showProgress.value = false
        com.xjek.base.utils.ViewUtils.showToast(activity!!, error, false)
    }

    override fun submit() {
        xuperInvoiceModel.callConfirmPaymentApi()
    }

}