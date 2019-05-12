package com.xjek.taxiservice.views.invoice

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.xjek.base.base.BaseActivity
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.writePreferences
import com.xjek.base.persistence.AppDatabase
import com.xjek.base.persistence.LocationPointsEntity
import com.xjek.base.utils.ViewUtils
import com.xjek.taxiservice.R
import com.xjek.taxiservice.databinding.ActivityInvoiceTaxiBinding
import com.xjek.taxiservice.model.ResponseData
import com.xjek.taxiservice.views.rating.TaxiRatingFragment
import kotlinx.android.synthetic.main.layout_status_indicators.*
import java.util.*
import kotlin.collections.ArrayList

class TaxiInvoiceActivity : BaseActivity<ActivityInvoiceTaxiBinding>(), TaxiInvoiceNavigator {

    private var activityInvoiceBinding: ActivityInvoiceTaxiBinding? = null
    private lateinit var mViewModel: TaxiInvoiceViewModel
    private var requestModel: ResponseData? = null
    private var strCheckRequestModel: String? = null

    private var points = ArrayList<LocationPointsEntity>()
    private var tempPoints = ArrayList<LatLng>()
    private var iteratePointsForApi = ArrayList<LatLng>()
    private var iteratePointsForDistanceCalc = ArrayList<LatLng>()
    private var tempPoint: LatLng? = null
    private var tempPointForDistanceCal: LatLng? = null
    private var iterationDistForApi = 50.0
    private var iterationDistForDistanceCal = 500.0

    override fun getLayoutId(): Int = R.layout.activity_invoice_taxi

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        activityInvoiceBinding = mViewDataBinding as ActivityInvoiceTaxiBinding
        mViewModel = ViewModelProviders.of(this).get(TaxiInvoiceViewModel::class.java)
        mViewModel.navigator = this
        activityInvoiceBinding!!.invoicemodel = mViewModel
        activityInvoiceBinding!!.lifecycleOwner = this
        rl_status_unselected.visibility = View.GONE
        rl_status_selected.visibility = View.VISIBLE
        mViewModel.tollCharge.value = "0"
        mViewModel.showLoading = loadingObservable as MutableLiveData<Boolean>

        writePreferences(PreferencesKey.FIRE_BASE_PROVIDER_IDENTITY, 0)

        getIntentValues()
        getApiResponse()

        points = AppDatabase.getAppDataBase(this)!!.locationPointsDao().getAllPoints() as ArrayList<LocationPointsEntity>

        if (points.size > 2) {
            for (point in points) {
                val latLng = LatLng(point.lat, point.lng)
                if (latLng.latitude > 0 && latLng.longitude > 0) tempPoints.add(latLng)
            }
            locationProcessing(tempPoints)
        }
    }

    private fun getApiResponse() {
        observeLiveData(mViewModel.paymentLiveData) {
            if (mViewModel.paymentLiveData.value != null)
                if (mViewModel.paymentLiveData.value!!.statusCode.equals("200")) openRatingDialog(requestModel)
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
                mViewModel.distance.value = requestModel!!.request.payment.distance.toString()
                mViewModel.timeTaken.value = requestModel!!.request.travel_time
                mViewModel.baseFare.value = requestModel!!.request.currency + requestModel!!.request.payment.fixed.toString()
                mViewModel.waitingCharge.value = requestModel!!.request.currency + requestModel!!.request.payment.waiting_amount.toString()
                mViewModel.distanceFare.value = requestModel!!.request.currency + requestModel!!.request.distance.toString()
                mViewModel.tax.value = requestModel!!.request.currency + requestModel!!.request.payment.tax.toString()
                mViewModel.tips.value = requestModel!!.request.currency + requestModel!!.request.payment.tips.toString()
                mViewModel.total.value = requestModel!!.request.currency + requestModel!!.request.payment.total.toString()
                if (requestModel!!.request.payment.toll_charge > 0)
                    mViewModel.tollCharge.value = requestModel!!.request.currency + requestModel!!.request.payment.toll_charge.toString()
                else mViewModel.tollCharge.value = requestModel!!.request.currency + "0"
            }

            if (mViewModel.pickuplocation.value != null && mViewModel.pickuplocation.value!!.length > 2)
                mViewModel.pickuplocation.value = requestModel!!.request.s_address
            else {
                val lat = requestModel!!.request.s_latitude
                val lon = requestModel!!.request.s_longitude
                val latLng: com.google.maps.model.LatLng?
                latLng = com.google.maps.model.LatLng(lat, lon)
                val address = getCurrentAddress(this, latLng)
                if (address.isNotEmpty()) mViewModel.pickuplocation.value = address[0].getAddressLine(0)
            }

            if (mViewModel.dropLocation.value != null && mViewModel.dropLocation.value!!.length > 2)
                mViewModel.dropLocation.value = requestModel!!.request.s_address
            else {
                val lat = requestModel!!.request.d_latitude
                val lon = requestModel!!.request.d_longitude
                val latLng: com.google.maps.model.LatLng?
                latLng = com.google.maps.model.LatLng(lat, lon)
                val address = getCurrentAddress(this, latLng)
                if (address.isNotEmpty()) mViewModel.dropLocation.value = address[0].getAddressLine(0)
            }

            if (requestModel!!.request.paid == 1) openRatingDialog(requestModel)
        }
    }

    private fun getCurrentAddress(context: Context, currentLocation: com.google.maps.model.LatLng): List<Address> {
        var addresses: List<Address> = java.util.ArrayList()
        val geocoder: Geocoder
        try {
            if (Geocoder.isPresent()) {
                geocoder = Geocoder(context, Locale.getDefault())
                addresses = geocoder.getFromLocation(currentLocation.lat, currentLocation.lng, 1)
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            }
        } catch (e: Exception) {
            Log.d("EXception", "EXception" + e.message)
        }

        return addresses
    }

    override fun openRatingDialog(data: ResponseData?) {
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
        ViewUtils.showToast(this, error, false)
    }

    override fun closeInvoiceActivity() {
        finish()
    }

    private fun locationProcessing(latLng: ArrayList<LatLng>) {
        println("GGGG :: locationProcessing = " + latLng.size)
        iteratePointsForApi.add(latLng[0])

        for (i in latLng.indices) if (i < latLng.size - 1)
            iteratePointsForApi(latLng[i], latLng[i + 1])

        iteratePointsForApi.add(latLng[latLng.size - 1])
        longLog(Gson().toJson(iteratePointsForApi), "BBB")
        iteratePointsForDistanceCalc.add(latLng[0])
        for (i in iteratePointsForApi.indices)
            if (i < iteratePointsForApi.size - 1)
                iteratePointsForDistanceCal(iteratePointsForApi[i], iteratePointsForApi[i + 1])
        iteratePointsForDistanceCalc.add(latLng[latLng.size - 1])
        longLog(Gson().toJson(iteratePointsForDistanceCalc), "CCC")

        frameDistanceAPI(iteratePointsForDistanceCalc)
    }

    private fun distBt(a: LatLng, b: LatLng): Double {
        val startPoint = Location("start")
        startPoint.latitude = a.latitude
        startPoint.longitude = a.longitude

        val endPoint = Location("end")
        endPoint.latitude = b.latitude
        endPoint.longitude = b.longitude
        return startPoint.distanceTo(endPoint).toDouble()
    }

    private fun iteratePointsForApi(s: LatLng, e: LatLng) {
        var dist = distBt(s, e)
        if (dist >= iterationDistForApi) {
            iteratePointsForApi.add(e)
            tempPoint = null
        } else if (tempPoint != null) {
            dist = distBt(tempPoint!!, e)
            if (dist >= iterationDistForApi) {
                iteratePointsForApi.add(e)
                tempPoint = null
            }
        } else tempPoint = s
    }

    private fun iteratePointsForDistanceCal(s: LatLng, e: LatLng) {
        var dist = distBt(s, e)
        if (dist >= iterationDistForDistanceCal) {
            iteratePointsForDistanceCalc.add(e)
            tempPointForDistanceCal = null
        } else if (tempPointForDistanceCal != null) {
            dist = distBt(tempPointForDistanceCal!!, e)
            if (dist >= iterationDistForDistanceCal) {
                iteratePointsForDistanceCalc.add(e)
                tempPointForDistanceCal = null
            }
        } else tempPointForDistanceCal = s
    }

    private fun frameDistanceAPI(latLng: List<LatLng>): String {

        val origins = StringBuilder()
        val destinations = StringBuilder()
        origins.append(latLng[0].latitude).append(",").append(latLng[0].longitude)

        for (i in 2 until latLng.size) {
            println("RRRR :: i = $i")
            destinations.append("|").append(latLng[i].latitude).append(",").append(latLng[i].longitude)
        }

        destinations.append(latLng[latLng.size - 1].latitude).append(",").append(latLng[latLng.size - 1].longitude)
        return "https://maps.googleapis.com/maps/api/distancematrix/json" +
                "?units=imperial" +
                "&origins=" + origins +
                "&destinations=" + destinations +
                "&key=AIzaSyAdXoVw-M-g4vgEOZZK7Dc9jMUlLR5xVXI"
    }
}