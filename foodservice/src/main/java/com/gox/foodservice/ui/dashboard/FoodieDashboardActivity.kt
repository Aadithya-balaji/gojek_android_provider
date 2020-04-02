package com.gox.foodservice.ui.dashboard

import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.gox.base.base.BaseActivity
import com.gox.base.base.BaseApplication
import com.gox.base.chatmessage.ChatActivity
import com.gox.base.data.Constants
import com.gox.base.data.Constants.ModuleTypes.ORDER
import com.gox.base.data.Constants.RideStatus.COMPLETED
import com.gox.base.data.Constants.RideStatus.PICKED_UP
import com.gox.base.data.Constants.RideStatus.PROCESSING
import com.gox.base.data.Constants.RideStatus.REACHED
import com.gox.base.data.Constants.RideStatus.STARTED
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.base.extensions.writePreferences
import com.gox.base.location_service.BaseLocationService
import com.gox.base.socket.SocketListener
import com.gox.base.socket.SocketManager
import com.gox.base.utils.Utils
import com.gox.base.utils.ViewUtils
import com.gox.foodservice.R
import com.gox.foodservice.adapter.OrderItemListAdapter
import com.gox.foodservice.databinding.ActivtyFoodieDashboardBinding
import com.gox.foodservice.model.OrderInvoice
import com.gox.foodservice.ui.rating.FoodieRatingFragment
import com.gox.foodservice.ui.verifyotp.FoodieVerifyOtpDialog
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activty_foodie_dashboard.*
import org.json.JSONObject
import java.util.*

class FoodieDashboardActivity : BaseActivity<ActivtyFoodieDashboardBinding>(), FoodLiveTaskServiceNavigator {

    private lateinit var mBinding: ActivtyFoodieDashboardBinding
    private lateinit var mViewModel: FoodieDashboardViewModel
    private var checkStatusApiCounter = 0

    private var currentStatus = ""
    private var showingStoreDetail = true
    private var roomConnected: Boolean = false
    private var reqID: Int = 0
    private var checkRequestTimer: Timer? = null

    override fun getLayoutId() = R.layout.activty_foodie_dashboard

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mBinding = mViewDataBinding as ActivtyFoodieDashboardBinding
        mViewModel = ViewModelProviders.of(this).get(FoodieDashboardViewModel::class.java)
        mViewDataBinding.foodLiveTaskviewModel = mViewModel
        mViewDataBinding.orderItemListAdpter = OrderItemListAdapter(this, listOf())
        mViewModel.navigator = this
        checkRequestTimer = Timer()

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver, IntentFilter(BaseLocationService.BROADCAST))

        mViewModel.showLoading = loadingObservable
        mViewModel.showLoading.value = true
//        mViewModel.callFoodieCheckRequest()

        checkRequestTimer!!.schedule(object : TimerTask() {
            override fun run() {
                mViewModel.callFoodieCheckRequest()
            }
        }, 0, 5000)

        SocketManager.onEvent(Constants.RoomName.ORDER_REQ, Emitter.Listener {
            Log.e("SOCKET", "SOCKET_SK ORDER request " + it[0])
            mViewModel.callFoodieCheckRequest()
        })



        SocketManager.setOnSocketRefreshListener(object : SocketListener.ConnectionRefreshCallBack {
            override fun onRefresh() {
                SocketManager.emit(Constants.RoomName.ORDER_ROOM_NAME, Constants.RoomId.getOrderRoom(reqID))
            }
        })

        checkRequestResponse()
        checkRatingReq()

        chat_img.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        call_img.setOnClickListener {
            val phoneNumber = if (showingStoreDetail)
                mViewModel.foodieCheckRequestModel.value!!.responseData.requests.pickup.contact_number
            else mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.mobile
            if (phoneNumber.isNotEmpty()) startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
            else ViewUtils.showToast(this, getString(R.string.no_valid_mobile), false)
        }

        loc_txt.setOnClickListener {
            val latitude: String
            val longitude: String
            if (showingStoreDetail) {
                latitude = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.pickup.latitude
                longitude = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.pickup.longitude
            } else {
                latitude = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.delivery.latitude
                longitude = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.delivery.longitude
            }
            if (latitude.isNotEmpty() && longitude.isNotEmpty()) {
                val url = "http://maps.google.com/maps?saddr=" + mViewModel.latitude.value + "," +
                        mViewModel.longitude.value + "&daddr=" + latitude + "," + longitude
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } else ViewUtils.showToast(this, getString(R.string.no_valid_loction), false)
        }
    }

    override fun onBackPressed() {

    }

    override fun onResume() {
        super.onResume()
        mViewModel.callFoodieCheckRequest()
    }

    override fun onDestroy() {
        super.onDestroy()
        checkRequestTimer?.cancel()
    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            if (location != null) {
                mViewModel.latitude.value = location.latitude
                mViewModel.longitude.value = location.longitude

                if (roomConnected) {
                    val locationObj = JSONObject()
                    locationObj.put("latitude", location.latitude)
                    locationObj.put("longitude", location.longitude)
                    locationObj.put("room", Constants.RoomId.getOrderRoom(reqID))
                    SocketManager.emit("send_location", locationObj)
                }
            }
        }
    }

    private fun checkRatingReq() {
        mViewModel.foodieRatingRequestModel.observe(this, Observer {
            mViewModel.showLoading.value = false
            if (it?.responseData != null && it.responseData.isEmpty()) finish()
        })
    }

    private fun checkRequestResponse() {
        mViewModel.foodieCheckRequestModel.observe(this, Observer {
            mViewModel.showLoading.value = false
            if (it?.responseData?.requests != null) {
                if (currentStatus != it.responseData.requests.status) {
                    mViewModel.orderId.value = it.responseData.requests.id
                    currentStatus = it.responseData.requests.status

                    if (!roomConnected) {
                        reqID = it.responseData.requests.id
                        PreferencesHelper.put(PreferencesKey.ORDER_REQ_ID, reqID)
                        Handler().postDelayed({
                            if (reqID != 0) {
                                SocketManager.emit(Constants.RoomName.ORDER_ROOM_NAME, Constants.RoomId.getOrderRoom(reqID))
                            }
                        }, 1000)
                    }

                    writePreferences(Constants.Chat.ADMIN_SERVICE, ORDER)
                    writePreferences(Constants.Chat.REQUEST_ID, it.responseData.requests.id)
                    writePreferences(Constants.Chat.USER_ID, it.responseData.requests.user_id)
                    writePreferences(Constants.Chat.PROVIDER_ID, it.responseData.requests.provider_id)
                    writePreferences(Constants.Chat.USER_NAME, it.responseData.requests.user.first_name
                            + " " + it.responseData.requests.user.first_name)
                    writePreferences(Constants.Chat.PROVIDER_NAME, it.responseData.provider_details.first_name
                            + " " + it.responseData.provider_details.last_name)

                    when (it.responseData.requests.status) {
                        PROCESSING -> whenProcessing()
                        STARTED -> whenStared()
                        REACHED -> whenReached()
                        PICKED_UP -> whenPickedUp()
                        COMPLETED -> whenPaid()
                    }
                }
            } else finish()
        })

        mViewModel.foodieUpdateRequestModel.observe(this, Observer {
            mViewModel.callFoodieCheckRequest()
        })
    }

    private fun whenPaid() {
        chat_img.visibility = VISIBLE
        changeToFlowIconView(true)
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, true)
        setOrderId()
        setUserLocDetails()
        setItemsPricing()
        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)
        mBinding.orderStatusBtn.text = getString(R.string.payment_received)

        mBinding.iconOrderPickedUp.background = getDrawable(R.drawable.round_grey)
        mBinding.iconReachedRestaurant.background = getDrawable(R.drawable.round_accent)
        mBinding.iconOrderPickedUp.imageTintList = ContextCompat.getColorStateList(this, R.color.white)

        mBinding.iconOrderDelivered.background = getDrawable(R.drawable.round_grey)
        mBinding.iconOrderDelivered.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconReachedRestaurant.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconOrderPickedUp.background = getDrawable(R.drawable.round_accent)

        mBinding.iconPaymentReceived.background = getDrawable(R.drawable.round_grey)
        mBinding.iconPaymentReceived.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconOrderDelivered.background = getDrawable(R.drawable.round_accent)

        mBinding.iconPaymentReceived.background = getDrawable(R.drawable.round_grey)
        mBinding.iconPaymentReceived.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconOrderDelivered.background = getDrawable(R.drawable.round_accent)
        mBinding.orderStatusBtn.text = getString(R.string.payment_received)
        showingStoreDetail = false
    }

    private fun whenPickedUp() {
        chat_img.visibility = VISIBLE
        changeToFlowIconView(true)
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, true)
        setOrderId()
        setUserLocDetails()
        setItemsPricing()
        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)

        mBinding.iconOrderPickedUp.background = getDrawable(R.drawable.round_grey)
        mBinding.iconReachedRestaurant.background = getDrawable(R.drawable.round_accent)
        mBinding.iconOrderPickedUp.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconReachedRestaurant.imageTintList = ContextCompat.getColorStateList(this, R.color.white)

        mBinding.orderStatusBtn.text = getString(R.string.order_delivered)
        mBinding.iconOrderDelivered.background = getDrawable(R.drawable.round_grey)
        mBinding.iconOrderDelivered.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconOrderPickedUp.background = getDrawable(R.drawable.round_accent)
        showingStoreDetail = false
        if(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.leave_at_door==1)
         ViewUtils.showAlert(this,getString(R.string.door_step_delivery))

    }

    private fun whenReached() {
        chat_img.visibility = VISIBLE
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
        changeToFlowIconView(true)
        setOrderId()
        setUserLocDetails()
        setItemsPricing()

        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)
        mBinding.orderStatusBtn.text = getString(R.string.order_picked_up)
        mBinding.iconOrderPickedUp.background = getDrawable(R.drawable.round_grey)
        mBinding.iconReachedRestaurant.background = getDrawable(R.drawable.round_accent)
        mBinding.iconOrderPickedUp.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        showingStoreDetail = false
    }

    private fun setUserLocDetails() {
        Glide.with(baseContext).load(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.picture)
                .placeholder(R.drawable.ic_user_place_holder).into(resturant_image)
        loc_name_tv.text = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.first_name + " " +
                mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.last_name
        loc_address_tv.text = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.delivery.map_address
    }

    private fun whenProcessing() {
        chat_img.visibility = GONE
        changeToFlowIconView(false)
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
        setOrderId()
        setRestaurantDetails()
        setItemsPricing()
        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)
        showingStoreDetail = true
    }

    private fun whenStared() {
        chat_img.visibility = VISIBLE
        changeToFlowIconView(true)
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
        setOrderId()
        setRestaurantDetails()
        setItemsPricing()
        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)
        showingStoreDetail = true
    }

    private fun setOrderId() {
        top_trnx_id.text = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.store_order_invoice_id
        order_id_tv.text = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.store_order_invoice_id
    }

    private fun setRestaurantDetails() {
        Glide
                .with(baseContext)
                .load(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.stores_details.picture)
                .placeholder(R.drawable.ic_user_place_holder)
                .into(resturant_image)

        loc_name_tv.text = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.stores_details.store_name
        loc_address_tv.text = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.stores_details.store_location
    }

    private fun setItemsPricing() {
        mBinding.orderItemListAdpter!!.itemList = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice.items
        mBinding.orderItemListAdpter!!.notifyDataSetChanged()
    }

    private fun setPaymentDetails(order_invoice: OrderInvoice) {
        val currency = readPreferences<String>(PreferencesKey.CURRENCY_SYMBOL)

        var itemTotal = 0.0

        order_invoice.items.forEach {
            itemTotal += it.total_item_price
        }

        when {
            itemTotal > 0 -> {
                item_total_tv.text = Utils.getNumberFormat()?.format(itemTotal) ?: ""
                item_total_lt.visibility = VISIBLE
            }
            else -> item_total_lt.visibility = GONE
        }
        when {
            order_invoice.tax_amount > 0 -> {
                servicetax_tv.text = Utils.getNumberFormat()?.format(order_invoice.tax_amount) ?: ""
                service_tax_lt.visibility = VISIBLE
            }
            else -> service_tax_lt.visibility = GONE
        }
        when {
            order_invoice.delivery_amount > 0 -> {
                delivery_charge_tv.text = Utils.getNumberFormat()?.format(order_invoice.delivery_amount)
                        ?: ""
                delivery_charge_lt.visibility = VISIBLE
            }
            else -> delivery_charge_lt.visibility = GONE
        }
        when {
            order_invoice.promocode_amount > 0 -> {
                promocode_deduction_tv.text = "-".plus(Utils.getNumberFormat()?.format(order_invoice.promocode_amount)
                        ?: "0.00")
                promocode_deduction_lt.visibility = VISIBLE
            }
            else -> promocode_deduction_lt.visibility = GONE
        }
        when {
            order_invoice.discount > 0 -> {
                discount_amount_tv.text = "-".plus(Utils.getNumberFormat()?.format(order_invoice.discount)
                        ?: "0:00")
                discount_lt.visibility = VISIBLE
            }
            else -> discount_lt.visibility = GONE
        }
        when {
            order_invoice.wallet_amount > 0 -> {
                wallet_amount_tv.text = Utils.getNumberFormat()?.format(order_invoice.wallet_amount)
                        ?: ""
                wallet_amount_lt.visibility = VISIBLE
            }
            else -> wallet_amount_lt.visibility = GONE
        }
        when {
            order_invoice.store_package_amount > 0 -> {
                package_amount_tv.text = Utils.getNumberFormat()?.format(order_invoice.store_package_amount)
                        ?: ""
                package_amount_lt.visibility = VISIBLE
            }
            else -> package_amount_lt.visibility = GONE
        }
        when {
            order_invoice.payable > 0 -> {
                total_value_tv.text = Utils.getNumberFormat()?.format(order_invoice.payable) ?: ""
                total_value_lt.visibility = VISIBLE
            }
            else -> total_value_lt.visibility = GONE
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun checkOrderDeliverStatus() {
        when (order_status_btn.text.toString()) {
            getString(R.string.started_towards_restaturant) -> mViewModel.callFoodieUpdateRequest("STARTED")
            getString(R.string.reached_restaurant) -> mViewModel.callFoodieUpdateRequest("REACHED")
            getString(R.string.order_picked_up) -> mViewModel.callFoodieUpdateRequest("PICKEDUP")
            getString(R.string.order_delivered) -> {
                if (BaseApplication.getCustomPreference!!.getBoolean(PreferencesKey.ORDER_OTP, false))
                    showOTPDialog()
                else
                    mViewModel.callFoodieDeliveryRequest()
            }
            getString(R.string.payment_received) -> {
                writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
                val bundle = Bundle()
                bundle.putString("id", mViewModel.foodieCheckRequestModel.value!!.responseData.requests.id.toString())
                bundle.putString("profileImg", mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.picture)
                bundle.putString("name", mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.first_name + " " +
                        mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.last_name)
                bundle.putString("bookingID", mViewModel.foodieCheckRequestModel.value!!.responseData.requests.store_order_invoice_id)
                val ratingFragment = FoodieRatingFragment(bundle)
                ratingFragment.show(supportFragmentManager, "rating")
                ratingFragment.isCancelable = false
            }
        }
    }

    private fun showOTPDialog() {
        val otpDialogFragment = FoodieVerifyOtpDialog.newInstance(
                mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_otp,
                mViewModel.foodieCheckRequestModel.value!!.responseData.requests.id,
                mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice.payable
        )
        otpDialogFragment.show(supportFragmentManager, "VerifyOtpDialog")
        otpDialogFragment.isCancelable = false
    }

    override fun showErrorMessage(error: String) {
        ViewUtils.showToast(this, error, false)
    }

    private fun changeToFlowIconView(flow: Boolean) = when (flow) {
        true -> {
            time_left_rl.visibility = GONE
            service_flow_icon_llayout.visibility = VISIBLE
            order_status_btn.text = getString(R.string.reached_restaurant)
        }
        false -> {
            /*      time_left_rl.visibility = VISIBLE       */
            service_flow_icon_llayout.visibility = GONE
            order_status_btn.text = getString(R.string.started_towards_restaturant)
        }
    }
}