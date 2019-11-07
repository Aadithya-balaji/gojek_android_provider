package com.gox.xuberservice.invoice

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.base.utils.CommonMethods
import com.gox.base.utils.ViewUtils
import com.gox.xuberservice.R
import com.gox.xuberservice.databinding.DialogInvoiceBinding
import com.gox.xuberservice.extracharge.XUberExtraChargeDialog
import com.gox.xuberservice.interfaces.DialogCloseInterface
import com.gox.xuberservice.interfaces.GetExtraChargeInterface
import com.gox.xuberservice.model.UpdateRequest
import com.gox.xuberservice.model.XuperCheckRequest
import com.gox.xuberservice.rating.DialogXuberRating
import kotlinx.android.synthetic.main.dialog_invoice.*

class XUberInvoiceDialog : BaseDialogFragment<DialogInvoiceBinding>(),
        XuperInvoiceNavigator, GetExtraChargeInterface, DialogCloseInterface {

    private lateinit var mBinding: DialogInvoiceBinding

    private lateinit var xUberInvoiceModel: XUberInvoiceViewModel
    private var updateRequestModel: UpdateRequest? = null
    private var strUpdateRequest: String? = null
    private var isFromCheckRequest: Boolean? = false
    private var strXUberCheckRequestModel: String? = null
    private var xUberCheckRequest: XuperCheckRequest? = null
    private var shown: Boolean? = false
    private var extraChargeDialogDialog: XUberExtraChargeDialog? = null
    private var invoiceDialog: Dialog? = null
    private var timeTaken: String? = ""
    private var extraAmount: Double? = 0.0
    var paymentType = ""
    var paid = 0

    override fun getLayout() = R.layout.dialog_invoice

    override fun onStart() {
        super.onStart()
        dialog!!.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
        getBundleValues()
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as DialogInvoiceBinding
        xUberInvoiceModel = XUberInvoiceViewModel()
        xUberInvoiceModel.navigator = this
        mBinding.invoicemodel = xUberInvoiceModel
        mBinding.lifecycleOwner = this
        xUberInvoiceModel.showLoading = loadingObservable as MutableLiveData<Boolean>
        updateViews()
        getApiResponse()
        invoiceDialog = dialog
    }

    fun getApiResponse() {
        xUberInvoiceModel.invoiceLiveData.observe(this, Observer<UpdateRequest> { updateRequest ->
            xUberInvoiceModel.showLoading.value = false
            if (updateRequest!!.statusCode.equals("200")) {
                Log.e("Dialog", "------------")
                showRating()
            }
        })
    }

    override fun showRating() {
        val ratingDialog = DialogXuberRating()
        val bundle = Bundle()
        bundle.putBoolean("isFromCheckRequest", isFromCheckRequest!!)
        if(isFromCheckRequest!!){
            bundle.putString("strCheckReq", strXUberCheckRequestModel)
        }else{
            bundle.putString("updateRequestModel", strUpdateRequest)
        }
        ratingDialog.arguments = bundle
        ratingDialog.show(activity!!.supportFragmentManager, "ratingDialog")
        ratingDialog.isCancelable = false
        if (invoiceDialog != null) invoiceDialog!!.dismiss()
    }



    private fun getBundleValues() {
        isFromCheckRequest = if (arguments!!.containsKey("fromCheckReq")) arguments!!.getBoolean("fromCheckReq") else false
        if (isFromCheckRequest == true) {
            strXUberCheckRequestModel = if (arguments!!.containsKey("strCheckReq"))
                arguments!!.getString("strCheckReq") else ""
            if (!strXUberCheckRequestModel.isNullOrEmpty())
                xUberCheckRequest = Gson().fromJson(strXUberCheckRequestModel, XuperCheckRequest::class.java)
        } else {
            strUpdateRequest = if (arguments!!.containsKey("strUpdateReq"))
                arguments!!.getString("strUpdateReq") else ""
            if (!strUpdateRequest.isNullOrEmpty())
                updateRequestModel = Gson().fromJson(strUpdateRequest, UpdateRequest::class.java)
        }
    }

    private fun updateViews() {
        val currency = readPreferences<String>(PreferencesKey.CURRENCY_SYMBOL)
        if (isFromCheckRequest == false) {
            xUberInvoiceModel.rating.value = String.format(resources.getString(R.string.xuper_rating_user), updateRequestModel!!.responseData!!.user!!.rating!!.toDouble())
            xUberInvoiceModel.serviceName.value = updateRequestModel!!.responseData!!.service!!.service_name
            xUberInvoiceModel.userImage.value = updateRequestModel!!.responseData!!.user!!.picture.toString()
            xUberInvoiceModel.totalAmount.value = updateRequestModel!!.responseData!!.payment!!.payable.toString()
            xUberInvoiceModel.requestID.value = updateRequestModel!!.responseData!!.id.toString()
            try {
                xUberInvoiceModel.tvAdditionalCharge.value = updateRequestModel!!.responseData!!.payment!!.extra_charges.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            xUberInvoiceModel.userName.value = updateRequestModel!!.responseData!!.user!!.first_name +
                    " " + updateRequestModel!!.responseData!!.user!!.last_name
            timeTaken = CommonMethods.getTimeDifference(updateRequestModel!!.responseData!!.started_at!!,
                    updateRequestModel!!.responseData!!.finished_at!!, "")
        } else {
            xUberInvoiceModel.rating.value = String.format(resources.getString(R.string.xuper_rating_user), xUberCheckRequest!!.responseData!!.requests!!.user!!.rating!!.toDouble())
            xUberInvoiceModel.serviceName.value = xUberCheckRequest!!.responseData!!.requests!!.service!!.service_name
            xUberInvoiceModel.userImage.value = xUberCheckRequest!!.responseData!!.requests!!.user!!.picture.toString()
            xUberInvoiceModel.totalAmount.value = xUberCheckRequest!!.responseData!!.requests!!.payment!!.payable.toString()
            try {
                xUberInvoiceModel.tvAdditionalCharge.value = xUberCheckRequest!!.responseData!!.requests!!.payment!!.extra_charges.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            xUberInvoiceModel.requestID.value = xUberCheckRequest!!.responseData!!.requests!!.id.toString()
            xUberInvoiceModel.userName.value = xUberCheckRequest!!.responseData!!.requests!!.user!!.first_name +
                    " " + xUberCheckRequest!!.responseData!!.requests!!.user!!.last_name
            timeTaken = CommonMethods.getTimeDifference(xUberCheckRequest!!.responseData!!.requests!!.started_at!!,
                    xUberCheckRequest!!.responseData!!.requests!!.finished_at!!, "")
        }

        if (isFromCheckRequest == false) {
            paymentType = updateRequestModel!!.responseData!!.payment_mode
            paid = updateRequestModel!!.responseData!!.paid?:0
        }else{
            paymentType = xUberCheckRequest!!.responseData!!.requests!!.payment_mode
            paid = xUberCheckRequest!!.responseData!!.requests!!.paid?:0
        }

        xUberInvoiceModel.paymentType = paymentType

        if(paymentType.equals(Constants.PaymentMode.CARD,true) || paymentType.equals("")){
            /*tvXuperConfirmPayment.visibility = View.GONE
            tvWaitingForPayment.visibility = View.VISIBLE*/
            tvXuperConfirmPayment.text = activity!!.resources.getString(R.string.xuber_done)
        }else {
            tvXuperConfirmPayment.text = activity!!.resources.getString(R.string.confrim_payment)
            /*tvXuperConfirmPayment.visibility = View.VISIBLE
            tvWaitingForPayment.visibility = View.GONE*/
        }

        tvXuperTime.text = timeTaken
        Glide.with(this)
                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .error(R.drawable.ic_user_place_holder))
                .load(xUberInvoiceModel.userImage.value)
                .into(mBinding.ivUserImg)

        mBinding.tvAmountToBePaid.text = "$currency ${xUberInvoiceModel.totalAmount.value}"
        mBinding.tvXuperService.text = xUberInvoiceModel.serviceName.value
        mBinding.tvAdditionalCharge.text = "${getText(R.string.xuper_label_additional_charge)} $currency ${xUberInvoiceModel.tvAdditionalCharge.value}"
    }

    override fun showErrorMessage(error: String) {
        xUberInvoiceModel.showLoading.value = false
        ViewUtils.showToast(activity!!, error, false)
    }

    fun isShown() = shown!!

    override fun submit() {
        if(paymentType.equals(Constants.PaymentMode.CARD,true) || paymentType.equals("")){
            showRating()
        }else{
            if (paid == 0)
                xUberInvoiceModel.callConfirmPaymentApi()
            else
                showRating()
        }
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

    override fun getExtraCharge(extraCharge: String, extraAmtNotes: String) {
        xUberInvoiceModel.extraCharge.value = extraCharge.replace("$", "")
        if (!xUberInvoiceModel.extraCharge.value.isNullOrEmpty()) {
            var totalAmount = xUberInvoiceModel.totalAmount.value!!.toDouble()
            val extAmount = if (!xUberInvoiceModel.extraCharge.value!!.equals("null", true) &&
                    xUberInvoiceModel.extraCharge.value!!.isNotEmpty())
                xUberInvoiceModel.extraCharge.value!!.toDouble() else 0.0
            totalAmount -= extraAmount!!
            extraAmount = extAmount
            val total = totalAmount + extAmount
            xUberInvoiceModel.totalAmount.value = String.format("%.2f", total)
        }
        if (extraAmtNotes.isNotEmpty()) xUberInvoiceModel.extraChargeNotes.value = extraAmtNotes
    }

    override fun showExtraChargePage() {
        invoiceDialog!!.hide()
        extraChargeDialogDialog = XUberExtraChargeDialog.newInstance(this)
        extraChargeDialogDialog!!.show(childFragmentManager, "extraRate")
    }

    override fun hideDialog(isNeedToHide: Boolean) {
        if (!isNeedToHide) invoiceDialog!!.show()
    }
}