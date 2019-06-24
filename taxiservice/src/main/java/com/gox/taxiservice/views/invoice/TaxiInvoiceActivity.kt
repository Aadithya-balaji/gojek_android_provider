package com.gox.taxiservice.views.invoice

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.gox.base.base.BaseActivity
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.writePreferences
import com.gox.base.persistence.AppDatabase
import com.gox.base.utils.ViewUtils
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.ActivityInvoiceTaxiBinding
import com.gox.taxiservice.model.ResponseData
import com.gox.taxiservice.views.rating.TaxiRatingFragment
import kotlinx.android.synthetic.main.layout_status_indicators.*
import java.util.*

class TaxiInvoiceActivity : BaseActivity<ActivityInvoiceTaxiBinding>(), TaxiInvoiceNavigator {

    private var mBinding: ActivityInvoiceTaxiBinding? = null
    private lateinit var mViewModel: TaxiInvoiceViewModel
    private var requestModel: ResponseData? = null
    private var strCheckRequestModel: String? = null

    override fun getLayoutId() = R.layout.activity_invoice_taxi

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as ActivityInvoiceTaxiBinding
        mViewModel = ViewModelProviders.of(this).get(TaxiInvoiceViewModel::class.java)
        mViewModel.navigator = this
        mBinding!!.invoicemodel = mViewModel
        mBinding!!.lifecycleOwner = this
        rl_status_unselected.visibility = View.GONE
        rl_status_selected.visibility = View.VISIBLE
        mViewModel.tollCharge.value = "0"
        mViewModel.showLoading = loadingObservable

        writePreferences(PreferencesKey.FIRE_BASE_PROVIDER_IDENTITY, 0)

        getIntentValues()
        getApiResponse()
    }

    private fun getApiResponse() {
        observeLiveData(mViewModel.paymentLiveData) {
            if (mViewModel.paymentLiveData.value != null)
                if (mViewModel.paymentLiveData.value!!.statusCode == "200") openRatingDialog(requestModel)
        }
    }

    private fun getIntentValues() {
        strCheckRequestModel = if (intent.hasExtra("ResponseData"))
            intent.getStringExtra("ResponseData") else ""

        if (!strCheckRequestModel.isNullOrEmpty()) {
            requestModel = Gson().fromJson(strCheckRequestModel, ResponseData::class.java)
            mViewModel.requestLiveData.value = requestModel
            if (requestModel != null) {
                mViewModel.pickuplocation.value = requestModel!!.request.s_address
                mViewModel.dropLocation.value = requestModel!!.request.d_address
                mViewModel.bookingId.value = requestModel!!.request.booking_id
                mViewModel.distance.value = requestModel!!.request.distance.toString() + requestModel!!.request.unit
                mViewModel.timeTaken.value = requestModel!!.request.travel_time
                mViewModel.baseFare.value = requestModel!!.request.currency + requestModel!!.request.payment.fixed.toString()
                mViewModel.waitingCharge.value = requestModel!!.request.currency + requestModel!!.request.payment.waiting_amount.toString()
                mViewModel.distanceFare.value = requestModel!!.request.currency + requestModel!!.request.payment.distance.toString()
                mViewModel.tax.value = requestModel!!.request.currency + requestModel!!.request.payment.tax.toString()
                mViewModel.tips.value = requestModel!!.request.currency + requestModel!!.request.payment.tips.toString()
                mViewModel.total.value = requestModel!!.request.currency + requestModel!!.request.payment.total.toString()
                if (requestModel!!.request.payment.toll_charge!! > 0)
                    mViewModel.tollCharge.value = requestModel!!.request.currency + requestModel!!.request.payment.toll_charge.toString()
                else mViewModel.tollCharge.value = requestModel!!.request.currency + "0"
            }

            if (mViewModel.pickuplocation.value != null && mViewModel.pickuplocation.value!!.length > 2)
                mViewModel.pickuplocation.value = requestModel!!.request.s_address
            else {
                val lat = requestModel!!.request.s_latitude
                val lon = requestModel!!.request.s_longitude
                val latLng: com.google.maps.model.LatLng?
                latLng = com.google.maps.model.LatLng(lat!!, lon!!)
                val address = getCurrentAddress(this, latLng)
                if (address.isNotEmpty()) mViewModel.pickuplocation.value = address[0].getAddressLine(0)
            }

            if (mViewModel.dropLocation.value != null && mViewModel.dropLocation.value!!.length > 2)
                mViewModel.dropLocation.value = requestModel!!.request.d_address
            else {
                val lat = requestModel!!.request.d_latitude
                val lon = requestModel!!.request.d_longitude
                val latLng: com.google.maps.model.LatLng?
                latLng = com.google.maps.model.LatLng(lat!!, lon!!)
                val address = getCurrentAddress(this, latLng)
                if (address.isNotEmpty()) mViewModel.dropLocation.value = address[0].getAddressLine(0)
            }

            if (requestModel!!.request.paid == 1) openRatingDialog(requestModel)
        }
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

    override fun openRatingDialog(data: ResponseData?) {
        AppDatabase.getAppDataBase(this)!!.locationPointsDao().deleteAllPoint()
        mViewModel.showLoading.value = false
        val bundle = Bundle()
        bundle.putString("id", data!!.request.id.toString())
        bundle.putString("admin_service_id", data.provider_details.service.admin_service_id.toString())
        bundle.putString("profileImg", data.request.user.picture.toString())
        bundle.putString("name", data.request.user.first_name + " " + data.request.user.last_name)
        bundle.putString("bookingID", data.request.booking_id)
        val ratingFragment = TaxiRatingFragment(bundle)
        ratingFragment.show(supportFragmentManager, "rating")
        ratingFragment.isCancelable = false
    }

    override fun tollCharge(amount: String) {
        mViewModel.tollCharge.value = amount
    }

    override fun showErrorMessage(error: String) {
        runOnUiThread {
            ViewUtils.showToast(this, error, false)
        }
    }

    override fun closeInvoiceActivity() = finish()

}