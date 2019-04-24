package com.xjek.taxiservice.views.invoice

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.ViewUtils
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.xjek.base.base.BaseActivity
import com.xjek.base.extensions.observeLiveData
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.FragmentInvoiceBinding
import com.xjek.taxiservice.model.CheckRequestModel
import com.xjek.taxiservice.views.rating.RatingFragment
import com.xjek.taxiservice.views.tollcharge.TollCharge
import kotlinx.android.synthetic.main.layout_status_indicators.*

class InvoiceActivity : BaseActivity<FragmentInvoiceBinding>(), InvoiceNavigator {


    private var activityInvoiceBinding: FragmentInvoiceBinding? = null
    private lateinit var invoiceModule: InvoiceModule
    private var checkRequestModel: CheckRequestModel? = null
    private var strCheckRequestModel: String? = null

    override fun getLayoutId(): Int = R.layout.fragment_invoice


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityInvoiceBinding = mViewDataBinding as FragmentInvoiceBinding
        invoiceModule = InvoiceModule()
        invoiceModule.navigator = this
        activityInvoiceBinding!!.invoicemodel = invoiceModule
        activityInvoiceBinding!!.setLifecycleOwner(this)
        rl_status_unselected.visibility = View.GONE
        rl_status_selected.visibility = View.VISIBLE
        invoiceModule.tollCharge.value = "ADD"
        invoiceModule.showLoading = loadingObservable as MutableLiveData<Boolean>
        getIntentValues()
        getApiResponse()
    }

    fun getApiResponse() {
        observeLiveData(invoiceModule.paymentLiveData) {
            if (invoiceModule.paymentLiveData.value != null) {
                if (invoiceModule.paymentLiveData.value!!.statusCode.equals("200")) {
                    openRatingDialog()
                }
            }
        }
    }

    fun getIntentValues() {
        strCheckRequestModel = if (intent.hasExtra("strRequestModel")) intent.getStringExtra("strRequestModel") else ""
        if (!strCheckRequestModel.isNullOrEmpty()) {
            checkRequestModel = Gson().fromJson(strCheckRequestModel, CheckRequestModel::class.java)
            invoiceModule.requestLiveData.value = checkRequestModel
        }
    }


    override fun openRatingDialog() {
        val ratingFragment = RatingFragment()
        ratingFragment.show(supportFragmentManager, "rating")
        ratingFragment.isCancelable = false
    }


    override fun showTollDialog() {
        val tollChargeDialog = TollCharge()
        if (checkRequestModel != null) {
            val bundle = Bundle()
            bundle.putString("requestID", checkRequestModel!!.responseData!!.request.id.toString())
            tollChargeDialog.arguments = bundle
        }
        tollChargeDialog.show(supportFragmentManager, "tollCharge")
    }


    override fun tollCharge(amount: String) {
        invoiceModule.tollCharge.value = amount
    }

    override fun showErrorMessage(error: String) {
        com.xjek.base.utils.ViewUtils.showToast(this,error,false)
    }

}
