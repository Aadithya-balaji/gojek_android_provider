package com.xjek.taxiservice.views.invoice

import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.ActivityInvoiceTaxiBinding
import com.xjek.taxiservice.model.ResponseData
import com.xjek.taxiservice.views.rating.TaxiRatingFragment
import com.xjek.taxiservice.views.tollcharge.TollChargeDialog
import kotlinx.android.synthetic.main.layout_status_indicators.*

class TaxiTaxiInvoiceActivity : BaseActivity<ActivityInvoiceTaxiBinding>(), TaxiInvoiceNavigator {

    private var activityInvoiceBinding: ActivityInvoiceTaxiBinding? = null
    private lateinit var mViewModelTaxi: TaxiInvoiceViewModel
    private var requestModel: ResponseData? = null
    private var strCheckRequestModel: String? = null

    override fun getLayoutId(): Int = R.layout.activity_invoice_taxi

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityInvoiceBinding = mViewDataBinding as ActivityInvoiceTaxiBinding
        mViewModelTaxi = TaxiInvoiceViewModel()
        mViewModelTaxi.navigator = this
        activityInvoiceBinding!!.invoicemodel = mViewModelTaxi
        activityInvoiceBinding!!.lifecycleOwner = this
        rl_status_unselected.visibility = View.GONE
        rl_status_selected.visibility = View.VISIBLE
        mViewModelTaxi.tollCharge.value = "ADD"
        mViewModelTaxi.showLoading = loadingObservable as MutableLiveData<Boolean>
        getIntentValues()
        getApiResponse()
    }

    private fun getApiResponse() {
        observeLiveData(mViewModelTaxi.paymentLiveData) {
            if (mViewModelTaxi.paymentLiveData.value != null)
                if (mViewModelTaxi.paymentLiveData.value!!.statusCode.equals("200")) openRatingDialog(requestModel)
        }
    }

    private fun getIntentValues() {
        strCheckRequestModel = if (intent.hasExtra("ResponseData"))
            intent.getStringExtra("ResponseData") else ""

        if (!strCheckRequestModel.isNullOrEmpty()) {
            requestModel = Gson().fromJson(strCheckRequestModel, ResponseData::class.java)
            mViewModelTaxi.requestLiveData.value = requestModel
            if (requestModel != null) {
                mViewModelTaxi.pickuplocation.value = requestModel!!.request.s_address
                mViewModelTaxi.dropLocation.value = requestModel!!.request.d_address
                mViewModelTaxi.bookingId.value = requestModel!!.request.booking_id
                mViewModelTaxi.distance.value = requestModel!!.request.payment.distance.toString()
                mViewModelTaxi.timeTaken.value = requestModel!!.request.travel_time
                mViewModelTaxi.baseFare.value = requestModel!!.provider_details.service.base_fare
                mViewModelTaxi.distanceFare.value = requestModel!!.request.distance.toString()
                mViewModelTaxi.tax.value = requestModel!!.request.payment.tax.toString()
                mViewModelTaxi.tips.value = requestModel!!.request.payment.tips.toString()
                if (requestModel!!.request.payment.toll_charge > 0)
                    mViewModelTaxi.tollCharge.value = requestModel!!.request.payment.toll_charge.toString()
            }

            if (requestModel!!.request.paid == 1) openRatingDialog(requestModel)
        }
    }

    override fun openRatingDialog(data: ResponseData?) {
        mViewModelTaxi.showLoading.value = false
        val bundle = Bundle()
        bundle.putString("id", data!!.request.provider_id.toString())
        bundle.putString("admin_service_id", data.provider_details.service.admin_service_id.toString())
        bundle.putString("profileImg", data.request.user.picture.toString())
        bundle.putString("name", data.request.user.first_name + " " + data.request.user.last_name)
        bundle.putString("bookingID", data.request.booking_id)
        val ratingFragment = TaxiRatingFragment(bundle)
        ratingFragment.show(supportFragmentManager, "rating")
        ratingFragment.isCancelable = false
    }

    override fun showTollDialog() {
        val tollChargeDialog = TollChargeDialog()
        if (requestModel != null) {
            val bundle = Bundle()
            bundle.putString("requestID", requestModel!!.request.id.toString())
            tollChargeDialog.arguments = bundle
        }
        tollChargeDialog.show(supportFragmentManager, "tollCharge")
    }

    override fun tollCharge(amount: String) {
        mViewModelTaxi.tollCharge.value = amount
    }

    override fun showErrorMessage(error: String) {
        com.xjek.base.utils.ViewUtils.showToast(this, error, false)
    }
}
