package com.gox.xuberservice.invoice

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gox.base.base.BaseDialogFragment
import com.gox.base.utils.CommonMethods
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.DialogInvoiceBinding
import com.gox.xuberservice.extracharge.DialogXuperExtraCharge
import com.gox.xuberservice.interfaces.DialogCloseInterface
import com.gox.xuberservice.interfaces.GetExtraChargeInterface
import com.gox.xuberservice.model.UpdateRequest
import com.gox.xuberservice.model.XuperCheckRequest
import com.gox.xuberservice.rating.DialogXuperRating
import kotlinx.android.synthetic.main.dialog_invoice.*

class DialogXuperInvoice : BaseDialogFragment<DialogInvoiceBinding>(), XuperInvoiceNavigator, GetExtraChargeInterface, DialogCloseInterface {

    private lateinit var dialogInvoiceBinding: DialogInvoiceBinding
    private lateinit var xuperInvoiceModel: XuperInvoiceViewModel
    private var updateRequestModel: UpdateRequest? = null
    private var strUpdateRequest: String? = null
    private var isFromCheckRequest: Boolean? = false
    private var strXuperCheckRequestModel: String? = null
    private var xuperCheckRequest: XuperCheckRequest? = null
    private var shown: Boolean? = false
    private var extraChageDialog: DialogXuperExtraCharge? = null
    private var invoiceDialog: Dialog? = null
    private var timetaken: String? = ""
    private  var extraAmount:Double?=0.0


    override fun getLayout(): Int {
        return R.layout.dialog_invoice
    }


    override fun onStart() {
        super.onStart()
        getDialog()!!.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog)
        getBundleValues()
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        dialogInvoiceBinding = viewDataBinding as DialogInvoiceBinding
        xuperInvoiceModel = XuperInvoiceViewModel()
        xuperInvoiceModel.navigator = this
        dialogInvoiceBinding.invoicemodel = xuperInvoiceModel
        dialogInvoiceBinding.setLifecycleOwner(this)
        xuperInvoiceModel.showProgress = loadingObservable as MutableLiveData<Boolean>
        updateUi()
        getApiResponse()
        invoiceDialog = dialog
    }

    fun getApiResponse() {
        xuperInvoiceModel.invoiceLiveData.observe(this, object : androidx.lifecycle.Observer<UpdateRequest> {
            override fun onChanged(updateRequest: UpdateRequest?) {
                xuperInvoiceModel.showProgress.value = false
                if (updateRequest!!.statusCode.equals("200")) {
                    Log.e("Dialog","------------")
                    val ratingDialog = DialogXuperRating()
                    val strupdateRequest = Gson().toJson(updateRequest)
                    val bundle = Bundle()
                    bundle.putBoolean("isFromCheckRequest", false)
                    bundle.putString("updateRequestModel", strupdateRequest)
                    ratingDialog.arguments = bundle
                    ratingDialog.show(activity!!.supportFragmentManager, "ratingDialog")
                    ratingDialog.isCancelable = true
                    if (invoiceDialog != null) {
                        invoiceDialog!!.dismiss()
                    }
                }
            }

        })
    }

    fun getBundleValues() {
        isFromCheckRequest = if (arguments!!.containsKey("fromCheckReq")) arguments!!.getBoolean("fromCheckReq") else false
        if (isFromCheckRequest == true) {
            strXuperCheckRequestModel = if (arguments!!.containsKey("strCheckReq")) arguments!!.getString("strCheckReq") else ""
            if (!strXuperCheckRequestModel.isNullOrEmpty())
                xuperCheckRequest = Gson().fromJson(strXuperCheckRequestModel, XuperCheckRequest::class.java)
        } else {
            strUpdateRequest = if (arguments!!.containsKey("strUpdateReq")) arguments!!.getString("strUpdateReq") else ""
            if (!strUpdateRequest.isNullOrEmpty())
                updateRequestModel = Gson().fromJson(strUpdateRequest, UpdateRequest::class.java)
        }
    }

    fun updateUi() {
        if (isFromCheckRequest == false) {
            xuperInvoiceModel.rating.value =String.format(resources.getString(R.string.xuper_rating_user),updateRequestModel!!.responseData!!.user!!.rating!!.toDouble())
            xuperInvoiceModel.serviceName.value = updateRequestModel!!.responseData!!.service!!.service_name
            xuperInvoiceModel.userImage.value = updateRequestModel!!.responseData!!.user!!.picture.toString()
            xuperInvoiceModel.totalAmount.value = updateRequestModel!!.responseData!!.payment!!.payable.toString()
            xuperInvoiceModel.requestID.value = updateRequestModel!!.responseData!!.id.toString()
            xuperInvoiceModel.userName.value = updateRequestModel!!.responseData!!.user!!.first_name + " " + updateRequestModel!!.responseData!!.user!!.last_name
            timetaken = CommonMethods.getTimeDifference(updateRequestModel!!.responseData!!.started_at!!, updateRequestModel!!.responseData!!.finished_at!!, "")

        } else {
            xuperInvoiceModel.rating.value = String.format(resources.getString(R.string.xuper_rating_user),xuperCheckRequest!!.responseData!!.requests!!.user!!.rating!!.toDouble())
            xuperInvoiceModel.serviceName.value = xuperCheckRequest!!.responseData!!.requests!!.service!!.service_name
            xuperInvoiceModel.userImage.value = xuperCheckRequest!!.responseData!!.requests!!.user!!.picture.toString()
            xuperInvoiceModel.totalAmount.value = xuperCheckRequest!!.responseData!!.requests!!.payment!!.payable.toString()
            xuperInvoiceModel.requestID.value = xuperCheckRequest!!.responseData!!.requests!!.id.toString()
            xuperInvoiceModel.userName.value = xuperCheckRequest!!.responseData!!.requests!!.user!!.first_name + " " + xuperCheckRequest!!.responseData!!.requests!!.user!!.last_name
            timetaken = CommonMethods.getTimeDifference(xuperCheckRequest!!.responseData!!.requests!!.started_at!!, xuperCheckRequest!!.responseData!!.requests!!.finished_at!!, "")
        }
        tvXuperTime.setText(timetaken)
        Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .placeholder(R.drawable.ic_profile_placeholder)
                        .error(R.drawable.ic_profile_placeholder))
                .load(xuperInvoiceModel.userImage.value)
                .into(dialogInvoiceBinding.ivUserImg)
        dialogInvoiceBinding.tvAmountToBePaid.setText(xuperInvoiceModel.totalAmount.value)
        dialogInvoiceBinding.tvXuperService.setText(xuperInvoiceModel.serviceName.value)
    }

    override fun showErrorMessage(error: String) {
        xuperInvoiceModel.showProgress.value = false
        com.gox.base.utils.ViewUtils.showToast(activity!!, error, false)
    }

    fun isShown(): Boolean {
        return shown!!
    }

    override fun submit() {
        xuperInvoiceModel.callConfirmPaymentApi()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (shown == false) {
                this.shown = true
                super.show(manager, tag)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (shown == false) {
            this.shown = true
            return super.show(transaction, tag)
        }
        return -1
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        this.shown = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        this.shown = false
    }

    override fun getExtraCharge(extraChareg: String, extraAmtNotes: String) {
        xuperInvoiceModel.extraCharge.value = extraChareg.replace("$", "")
        if (!xuperInvoiceModel.extraCharge.value.isNullOrEmpty()) {
            var totalAmount = xuperInvoiceModel.totalAmount.value!!.toDouble()
            val extAmount = xuperInvoiceModel.extraCharge.value!!.toDouble()
            totalAmount=totalAmount-extraAmount!!
            extraAmount=extAmount
            val total = totalAmount + extAmount
            xuperInvoiceModel.totalAmount.value = total.toString()
        }
        if (!extraAmtNotes.isNullOrEmpty()) {
            xuperInvoiceModel.extraChargeNotes.value = extraAmtNotes
        }
    }

    override fun showExtraChargePage() {
        invoiceDialog!!.hide()
        extraChageDialog = DialogXuperExtraCharge.newInstance(this, this)
        extraChageDialog!!.show(childFragmentManager, "extraRate")


    }

    override fun hideDialog(isNeedtoHide: Boolean) {
        if (isNeedtoHide == false) {
            invoiceDialog!!.show()
        }
    }


}