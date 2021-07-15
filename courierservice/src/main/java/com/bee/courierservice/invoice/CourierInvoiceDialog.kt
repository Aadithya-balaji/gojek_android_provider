package com.bee.courierservice.invoice

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.bee.courierservice.R
import com.bee.courierservice.databinding.CDialogInvoiceBinding
import com.bee.courierservice.extracharge.CourierExtraChargeDialog
import com.bee.courierservice.interfaces.DialogCloseInterface
import com.bee.courierservice.interfaces.GetExtraChargeInterface
import com.bee.courierservice.model.UpdateRequest
import com.bee.courierservice.model.CourierCheckRequest
import com.bee.courierservice.model.PaymentModel
import com.bee.courierservice.rating.DialogCourierRating
import com.bee.courierservice.xuberMainActivity.CourierDashboardViewModel
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants
import com.gox.base.utils.ViewUtils
import kotlinx.android.synthetic.main.c_dialog_invoice.*
import java.util.*

class CourierInvoiceDialog : BaseDialogFragment<CDialogInvoiceBinding>(),
        CourierInvoiceNavigator, GetExtraChargeInterface, DialogCloseInterface {

    private lateinit var mBinding: CDialogInvoiceBinding
    private lateinit var xUberInvoiceModel: CourierInvoiceViewModel
    private lateinit var courierCheckRequest: CourierDashboardViewModel
    private var updateRequestModel: UpdateRequest? = null
    private var strUpdateRequest: String? = null
    private var isFromCheckRequest: Boolean? = false
    private var strXUberCheckRequestModel: String? = null
    private var xUberCheckRequest: CourierCheckRequest? = null
    private var shown: Boolean? = false
    private var extraChargeDialogDialog: CourierExtraChargeDialog? = null
    private var invoiceDialog: Dialog? = null
    private var timeTaken: String? = ""
    private var extraAmount: Double? = 0.0
    var paymentType = ""
    var paid = 0

    override fun getLayout() = R.layout.c_dialog_invoice

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
        mBinding = viewDataBinding as CDialogInvoiceBinding
        xUberInvoiceModel = CourierInvoiceViewModel()
//        courierCheckRequest = ViewModelProviders.of(this).get(CourierDashboardViewModel::class.java)
//            courierCheckRequest.xUberCheckRequest.observe(this, Observer<CourierCheckRequest> {
//                getIntentValues(it)
//            })

        xUberInvoiceModel.navigator = this
        mBinding.invoicemodel = xUberInvoiceModel
        mBinding.lifecycleOwner = this
        xUberInvoiceModel.showLoading = loadingObservable as MutableLiveData<Boolean>
//        getBundleValues()
//        updateViews()
        getApiResponse()
        invoiceDialog = dialog
        getIntentValues(xUberCheckRequest!!)
//        getIntentValues()

    }

    fun getApiResponse() {
        xUberInvoiceModel.paymentLiveData.observe(this, Observer<PaymentModel> { updateRequest ->
            xUberInvoiceModel.showLoading.value = false
            if (updateRequest!!.statusCode.equals("200")) {
                Log.e("Dialog", "------------")
                showRating()
            }
        })
        xUberInvoiceModel.invoiceUpdateRequest.observe(this, Observer {
            xUberInvoiceModel.showLoading.value = false
            if(it.statusCode.equals("200")){
                dismiss()
//                9884138378 mr.sridhar
            }
        })

//        xUberInvoiceModel.checkStatusTaxiLiveData.observe(this, Observer<CourierCheckRequest> {  it:CourierCheckRequest ->
//            xUberInvoiceModel.showLoading.value = false
//            System.out.println("1---> "+Gson().toJson(it))
//            getIntentValues(it)
//        })
    }

    override fun showRating() {
        val ratingDialog = DialogCourierRating()
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
        dismiss()
    }



    private fun getBundleValues() {
        isFromCheckRequest = if (arguments!!.containsKey("isFromCheckRequest")) arguments!!.getBoolean("isFromCheckRequest") else false
        if (isFromCheckRequest == true) {
            strXUberCheckRequestModel = if (arguments!!.containsKey("strCheckReq"))
                arguments!!.getString("strCheckReq") else ""
            if (!strXUberCheckRequestModel.isNullOrEmpty()){
                xUberCheckRequest = Gson().fromJson(strXUberCheckRequestModel, CourierCheckRequest::class.java)
            }
        } else {
            strUpdateRequest = if (arguments!!.containsKey("strUpdateReq"))
                arguments!!.getString("strUpdateReq") else ""
            if (!strUpdateRequest.isNullOrEmpty())
                updateRequestModel = Gson().fromJson(strUpdateRequest, UpdateRequest::class.java)
        }
    }

//    private fun updateViews() {
//        val currency = readPreferences<String>(PreferencesKey.CURRENCY_SYMBOL)
//        if (isFromCheckRequest == false) {
//            xUberInvoiceModel.rating.value = String.format(resources.getString(R.string.xuper_rating_user), updateRequestModel!!.responseData!!.user!!.rating!!.toDouble())
//            xUberInvoiceModel.serviceName.value = updateRequestModel!!.responseData!!.service!!.service_name
//            xUberInvoiceModel.userImage.value = updateRequestModel!!.responseData!!.user!!.picture.toString()
//            xUberInvoiceModel.totalAmount.value = updateRequestModel!!.responseData!!.payment!!.payable.toString()
//            xUberInvoiceModel.requestID.value = updateRequestModel!!.responseData!!.id.toString()
//            try {
//                xUberInvoiceModel.tvAdditionalCharge.value = updateRequestModel!!.responseData!!.payment!!.extra_charges.toString()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            xUberInvoiceModel.userName.value = updateRequestModel!!.responseData!!.user!!.first_name +
//                    " " + updateRequestModel!!.responseData!!.user!!.last_name
//            timeTaken = CommonMethods.getTimeDifference(updateRequestModel!!.responseData!!.started_at!!,
//                    updateRequestModel!!.responseData!!.finished_at!!, "")
//        } else {
//            xUberInvoiceModel.rating.value = String.format(resources.getString(R.string.xuper_rating_user), xUberCheckRequest!!.responseData!!.request!!.user!!.rating!!.toDouble())
//            xUberInvoiceModel.serviceName.value = xUberCheckRequest!!.responseData!!.request!!.admin_service
//            if(!xUberCheckRequest!!.responseData!!.request!!.user!!.picture.isNullOrEmpty())
//            xUberInvoiceModel.userImage.value = xUberCheckRequest!!.responseData!!.request!!.user!!.picture.toString()
//            else
//                xUberInvoiceModel.userImage.value = ""
////            xUberInvoiceModel.totalAmount.value = xUberCheckRequest!!.responseData!!.request!!.payment!!.payable.toString()
////            try {
////                xUberInvoiceModel.tvAdditionalCharge.value = xUberCheckRequest!!.responseData!!.request!!.payment!!.extra_charges.toString()
////            } catch (e: Exception) {
////                e.printStackTrace()
////            }
//            xUberInvoiceModel.requestID.value = xUberCheckRequest!!.responseData!!.request!!.id.toString()
//            xUberInvoiceModel.userName.value = xUberCheckRequest!!.responseData!!.request!!.user!!.first_name +
//                    " " + xUberCheckRequest!!.responseData!!.request!!.user!!.last_name
////            timeTaken = CommonMethods.getTimeDifference(xUberCheckRequest!!.responseData!!.requests!!.started_at!!,
////                    xUberCheckRequest!!.responseData!!.request!!.finished_at!!, "")
//        }
//
//        if (isFromCheckRequest == false) {
//            paymentType = updateRequestModel!!.responseData!!.payment_mode
//            paid = updateRequestModel!!.responseData!!.paid?:0
//        }else{
//            paymentType = xUberCheckRequest!!.responseData!!.request!!.payment_mode
//            paid = xUberCheckRequest!!.responseData!!.request!!.paid?:0
//        }
//
//        xUberInvoiceModel.paymentType = paymentType
//
//        if(paymentType.equals(Constants.PaymentMode.CARD,true) || paymentType.equals("")){
//            /*tvXuperConfirmPayment.visibility = View.GONE
//            tvWaitingForPayment.visibility = View.VISIBLE*/
//            tvXuperConfirmPayment.text = activity!!.resources.getString(R.string.xuber_done)
//        }else {
//            tvXuperConfirmPayment.text = activity!!.resources.getString(R.string.confrim_payment)
//            /*tvXuperConfirmPayment.visibility = View.VISIBLE
//            tvWaitingForPayment.visibility = View.GONE*/
//        }
//
//        tvXuperTime.text = timeTaken
//        Glide.with(this)
//                .applyDefaultRequestOptions(com.bumptech.glide.request.RequestOptions()
//                        .centerCrop()
//                        .placeholder(R.drawable.ic_user_place_holder)
//                        .error(R.drawable.ic_user_place_holder))
//                .load(xUberInvoiceModel.userImage.value)
//                .into(mBinding.ivUserImg)
//
//        mBinding.tvAmountToBePaid.text = "$currency ${xUberInvoiceModel.totalAmount.value}"
//        mBinding.tvXuperService.text = xUberInvoiceModel.serviceName.value
//        mBinding.tvAdditionalCharge.text = "${getText(R.string.xuper_label_additional_charge)} $currency ${xUberInvoiceModel.tvAdditionalCharge.value}"
//    }


    private fun getIntentValues(courierCheckRequest:CourierCheckRequest) {
       val requestModel:CourierCheckRequest = courierCheckRequest
        if (requestModel != null) {
            if (requestModel!!.responseData.request.delivery.payment.payment_mode.equals(Constants.PaymentMode.CASH, true)) {
                if (requestModel!!.responseData.request.payment_by.equals("SENDER", true))
                    mBinding.tvConfirmPayment.text = resources.getString(R.string.taxi_confirm_payment)
                else
                    mBinding.tvConfirmPayment.text = resources.getString(R.string.taxi_reached_and_delivered)
            }
            else { mBinding.tvConfirmPayment.text = resources.getString(R.string.taxi_confirm_done)
            }
            xUberInvoiceModel.bookingId.value = requestModel!!.responseData.request.booking_id
            if(requestModel!!.responseData.request.payment_by.equals("SENDER", true).not()){
                if (requestModel!!.responseData.request.calculator.equals("DISTANCE", true)) {
                    mBinding.tvInvoiceLabelWeight.visibility = GONE
                    mBinding.tvInvoiceWeight.visibility = GONE
                    mBinding.llweight.visibility = GONE
                    xUberInvoiceModel.labelDistance.value = "Distance Fare for ${(requestModel.responseData.request.delivery.distance!!) / 1000} ${requestModel.responseData.request.delivery.unit}"
                    xUberInvoiceModel.distance.value =requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.distance.toString()
                } else if (requestModel!!.responseData.request.calculator.equals("WEIGHT", true)) {
                    mBinding.tvLabelDistanceTravelled.visibility = GONE
                    mBinding.tvDistance.visibility = GONE
                    mBinding.lldistance.visibility = GONE
                    xUberInvoiceModel.labelWeight.value = "Delivery Weight fare for ${requestModel.responseData.request.delivery.weight} Kg"
                    xUberInvoiceModel.timeTaken.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.weight.toString()
                } else if (requestModel!!.responseData.request.calculator.equals("WEIGHTDISTANCE", true) || requestModel!!.responseData.request.calculator.equals("DISTANCEWEIGHT", true)) {
                    xUberInvoiceModel.labelDistance.value = "Distance Fare for ${(requestModel.responseData.request.delivery.distance!!) / 1000} ${requestModel.responseData.request.delivery.unit}"
                    xUberInvoiceModel.distance.value =requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.distance.toString()
                    xUberInvoiceModel.labelWeight.value = "Delivery Weight fare for ${requestModel.responseData.request.delivery.weight} Kg"
                    xUberInvoiceModel.timeTaken.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.weight.toString()
                }
                xUberInvoiceModel.baseFare.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.fixed.toString()
                xUberInvoiceModel.waitingCharge.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.waiting_amount.toString()
                xUberInvoiceModel.discount.set("-" + requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.discount.toString())
                xUberInvoiceModel.payableAmount.set(requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.payable.toString())
                xUberInvoiceModel.tax.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.tax.toString()
                xUberInvoiceModel.tips.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.tips.toString()
                xUberInvoiceModel.total.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.total.toString()
                if (requestModel!!.responseData.request.delivery.payment.toll_charge!! > 0)
                    xUberInvoiceModel.tollCharge.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.delivery.payment.toll_charge.toString()
                else xUberInvoiceModel.tollCharge.value = requestModel!!.responseData.request.currency + "0"
            }else{
                if (requestModel!!.responseData.request.calculator.equals("DISTANCE", true)) {
                    mBinding.tvInvoiceLabelWeight.visibility = GONE
                    mBinding.tvInvoiceWeight.visibility = GONE
                    mBinding.llweight.visibility = GONE
                    xUberInvoiceModel.labelDistance.value = requestModel.responseData.request.payment.distance_fare_text.toString()
                    xUberInvoiceModel.distance.value =requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.distance.toString()
                } else if (requestModel!!.responseData.request.calculator.equals("WEIGHT", true)) {
                    mBinding.tvLabelDistanceTravelled.visibility = GONE
                    mBinding.tvDistance.visibility = GONE
                    mBinding.lldistance.visibility = GONE
                    xUberInvoiceModel.labelWeight.value = requestModel.responseData.request.payment.weight_fare_text
                    xUberInvoiceModel.timeTaken.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.weight.toString()
                } else if (requestModel!!.responseData.request.calculator.equals("WEIGHTDISTANCE", true) || requestModel!!.responseData.request.calculator.equals("DISTANCEWEIGHT", true)) {
                    xUberInvoiceModel.labelDistance.value = requestModel.responseData.request.payment.distance_fare_text.toString()
                    xUberInvoiceModel.distance.value =requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.distance.toString()
                    xUberInvoiceModel.labelWeight.value = requestModel.responseData.request.payment.weight_fare_text
                    xUberInvoiceModel.timeTaken.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.weight.toString()
                }
                xUberInvoiceModel.baseFare.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.fixed.toString()
                xUberInvoiceModel.waitingCharge.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.waiting_amount.toString()
                xUberInvoiceModel.discount.set("-" + requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.discount.toString())
                xUberInvoiceModel.payableAmount.set(requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.payable.toString())
                xUberInvoiceModel.tax.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.tax.toString()
                xUberInvoiceModel.tips.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.tips.toString()
                xUberInvoiceModel.total.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.total.toString()
                if (requestModel!!.responseData.request.payment.toll_charge!! > 0)
                    xUberInvoiceModel.tollCharge.value = requestModel!!.responseData.request.currency + requestModel!!.responseData.request.payment.toll_charge.toString()
                else xUberInvoiceModel.tollCharge.value = requestModel!!.responseData.request.currency + "0"
            }
        }

//        if (requestModel!!.responseData.request.paid == 1 && !isRatingShown) {
//            isRatingShown = true
//            openRatingDialog(requestModel)
//        }

    }


    private fun getCurrentAddress(context: Context, currentLocation: com.google.maps.model.LatLng): List<Address> {
        var addresses: List<Address> = ArrayList()
        val geoCoder: Geocoder
        try {
            if (Geocoder.isPresent()) {
                geoCoder = Geocoder(context, Locale.getDefault())
                addresses = geoCoder.getFromLocation(currentLocation.lat, currentLocation.lng, 1)
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            }
        } catch (e: Exception) {
            Log.d("EXception", "EXception" + e.message)
        }

        return addresses
    }

    override fun showErrorMessage(error: String) {
        xUberInvoiceModel.showLoading.value = false
        ViewUtils.showToast(activity!!, error, false)
    }

    fun isShown() = shown!!

    override fun submit() {
        if(xUberCheckRequest!!.responseData.request.payment_by.equals("SENDER",true)){
            xUberInvoiceModel.updatePayment(xUberCheckRequest!!.responseData.request.delivery.id.toString(),"SENDER")
        }else{
            xUberInvoiceModel.updatePayment(xUberCheckRequest!!.responseData.request.delivery.id.toString(),"RECEIVER")
//            xUberCheckRequest!!.responseData.request.let { request ->
//            xUberInvoiceModel.confirmPayment(request.delivery.payment.payment_mode,request.id)
//        }
        }
        
      /* if(paymentType.equals(Constants.PaymentMode.CARD,true) || paymentType.equals("")){
           showRating()
       }else{
           xUberCheckRequest!!.responseData.request.let { request ->
               xUberInvoiceModel.confirmPayment(request.delivery.first().payment.payment_mode,request.id)
           }
      }*/
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
//        xUberInvoiceModel.extraCharge.value = extraCharge.replace("₦", "")
//        if (!xUberInvoiceModel.extraCharge.value.isNullOrEmpty()) {
//            var totalAmount = xUberInvoiceModel.totalAmount.value!!.toDouble()
//            val extAmount = if (!xUberInvoiceModel.extraCharge.value!!.equals("null", true) &&
//                    xUberInvoiceModel.extraCharge.value!!.isNotEmpty())
//                xUberInvoiceModel.extraCharge.value!!.toDouble() else 0.0
//            totalAmount -= extraAmount!!
//            extraAmount = extAmount
//            val total = totalAmount + extAmount
//            xUberInvoiceModel.totalAmount.value = String.format("%.2f", total)
//        }
//        if (extraAmtNotes.isNotEmpty()) xUberInvoiceModel.extraChargeNotes.value = extraAmtNotes
    }

    override fun showExtraChargePage() {
        invoiceDialog!!.hide()
        extraChargeDialogDialog = CourierExtraChargeDialog.newInstance(this)
        extraChargeDialogDialog!!.show(childFragmentManager, "extraRate")
    }

    override fun hideDialog(isNeedToHide: Boolean) {
        if (!isNeedToHide) invoiceDialog!!.show()
    }
}