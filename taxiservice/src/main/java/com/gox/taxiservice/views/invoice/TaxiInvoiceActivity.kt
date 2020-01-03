package com.gox.taxiservice.views.invoice

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.writePreferences
import com.gox.base.persistence.AppDatabase
import com.gox.base.socket.SocketListener
import com.gox.base.socket.SocketManager
import com.gox.base.utils.ViewUtils
import com.gox.taxiservice.R
import com.gox.taxiservice.databinding.ActivityInvoiceTaxiBinding
import com.gox.taxiservice.model.ResponseData
import com.gox.taxiservice.views.rating.TaxiRatingFragment
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_invoice_taxi.*
import kotlinx.android.synthetic.main.layout_status_indicators.*
import java.util.*

class TaxiInvoiceActivity : BaseActivity<ActivityInvoiceTaxiBinding>(), TaxiInvoiceNavigator {

    private lateinit var mViewModel: TaxiInvoiceViewModel

    private var isRatingShown: Boolean = false
    private var mBinding: ActivityInvoiceTaxiBinding? = null
    private var requestModel: ResponseData? = null
    //    private var strCheckRequestModel: String? = null
    private var checkRequestTimer: Timer? = null
    private var roomConnected: Boolean = false
    private var reqID: Int = 0

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

        checkRequestTimer = Timer()

        checkRequestTimer!!.schedule(object : TimerTask() {
            override fun run() = mViewModel.callTaxiCheckStatusAPI()
        }, 0, 5000)

        getApiResponse()

        SocketManager.onEvent(Constants.RoomName.RIDE_REQ, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK transport request " + it[0])
            mViewModel.callTaxiCheckStatusAPI()
        })

        SocketManager.setOnSocketRefreshListener(object : SocketListener.ConnectionRefreshCallBack {
            override fun onRefresh() {
                SocketManager.emit(Constants.RoomName.TRANSPORT_ROOM_NAME, Constants.RoomId.getTransportRoom(reqID))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mViewModel.callTaxiCheckStatusAPI()
    }

    private fun getApiResponse() {
        observeLiveData(mViewModel.paymentLiveData) {
            if (mViewModel.paymentLiveData.value != null)
                if (mViewModel.paymentLiveData.value!!.statusCode == "200")
                    openRatingDialog(requestModel)
        }
        observeLiveData(mViewModel.checkStatusTaxiLiveData) {
            if (it?.statusCode.equals("200")) {
                if (it.responseData.request != null) {
                    getIntentValues(it.responseData)
                    when (it.responseData.request.status) {
                        Constants.RideStatus.COMPLETED -> {
                            println("RRR :: inside COMPLETED = ")
                            if (it.responseData.request.paid == 1 && !isRatingShown) {
                                isRatingShown = true
                                openRatingDialog(it.responseData)
                            }
                        }

                        "" -> {
                            finish()
                        }
                    }
                } else
                    finish()
            }
        }

        /*  mViewModel.checkStatusTaxiLiveData.observe(this, androidx.lifecycle.Observer {
              if (it?.statusCode.equals("200"))
                  try {
                      when (it.responseData.request.status) {
                          Constants.RideStatus.DROPPED -> {
                              println("RRR :: inside DROPPED = ")
                              getIntentValues(it.responseData)
                              finish()
                          }
                          Constants.RideStatus.COMPLETED -> {
                              println("RRR :: inside COMPLETED = ")
                              finish()
                          }
                      }
                  } catch (e: Exception) {
                      Log.d("Catch_invoice",e.localizedMessage)
                  }
          })*/
    }

    private fun getIntentValues(strCheckRequestModel: ResponseData) {


        if (!roomConnected) {
            reqID = strCheckRequestModel.request.id
            PreferencesHelper.put(PreferencesKey.TRANSPORT_REQ_ID, reqID)
            if (reqID != 0) {
                roomConnected = true
                SocketManager.emit(Constants.RoomName.TRANSPORT_ROOM_NAME, Constants.RoomId.getTransportRoom(reqID))
            } else {
                roomConnected = false
            }
        }

        requestModel = strCheckRequestModel
        if (requestModel != null) {

            if (requestModel!!.request.payment_mode.equals(Constants.PaymentMode.CASH, true))
                tv_confirm_payment.text = resources.getString(R.string.taxi_confirm_payment)
            else tv_confirm_payment.text = resources.getString(R.string.taxi_confirm_done)

            mViewModel.pickuplocation.value = requestModel!!.request.s_address
            mViewModel.dropLocation.value = requestModel!!.request.d_address
            mViewModel.bookingId.value = requestModel!!.request.booking_id
            mViewModel.distance.value = requestModel!!.request.distance.toString() + requestModel!!.request.unit
            mViewModel.timeTaken.value = requestModel!!.request.travel_time + " mins"
            mViewModel.baseFare.value = requestModel!!.request.currency + requestModel!!.request.payment.fixed.toString()
            mViewModel.waitingCharge.value = requestModel!!.request.currency + requestModel!!.request.payment.waiting_amount.toString()
            mViewModel.discount.set("-" + requestModel!!.request.currency + requestModel!!.request.payment.discount.toString())
            mViewModel.payableAmount.set(requestModel!!.request.currency + requestModel!!.request.payment.payable.toString())
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

        if (requestModel!!.request.paid == 1 && !isRatingShown) {
            isRatingShown = true
            openRatingDialog(requestModel)
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
        bundle.putString("admin_service", data.provider_details.service.admin_service.toString())
        if (!data.request?.user?.picture.isNullOrEmpty())
            bundle.putString("profileImg", data.request.user?.picture)
        else
            bundle.putString("profileImg", "")
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

    override fun onStop() {
        super.onStop()
        checkRequestTimer?.cancel()
    }

    override fun closeInvoiceActivity() = finish()

}