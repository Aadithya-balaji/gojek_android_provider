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
import com.gox.base.chatmessage.ChatActivity
import com.gox.base.data.Constants
import com.gox.base.data.PreferencesKey
import com.gox.base.extensions.readPreferences
import com.gox.base.extensions.writePreferences
import com.gox.base.location_service.BaseLocationService
import com.gox.base.utils.ViewUtils
import com.gox.foodservice.R
import com.gox.foodservice.adapter.OrderItemListAdapter
import com.gox.foodservice.databinding.ActivtyFoodieDashboardBinding
import com.gox.foodservice.model.OrderInvoice
import com.gox.foodservice.ui.rating.FoodieRatingFragment
import com.gox.foodservice.ui.verifyotp.FoodieVerifyOtpDialog
import kotlinx.android.synthetic.main.activty_foodie_dashboard.*

class FoodieDashboardActivity : BaseActivity<ActivtyFoodieDashboardBinding>(), FoodLiveTaskServiceNavigator {

    private lateinit var mBinding: ActivtyFoodieDashboardBinding
    private lateinit var mViewModel: FoodieDashboardViewModel
    private var checkStatusApiCounter = 0

    var i = 0
    var currentStatus = ""
    var showingStoreDetail = true

    override fun getLayoutId() = R.layout.activty_foodie_dashboard

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mBinding = mViewDataBinding as ActivtyFoodieDashboardBinding
        mViewModel = ViewModelProviders.of(this).get(FoodieDashboardViewModel::class.java)
        mViewDataBinding.foodLiveTaskviewModel = mViewModel
        mViewDataBinding.orderItemListAdpter = OrderItemListAdapter(this, listOf())
        mViewModel.navigator = this
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, IntentFilter(BaseLocationService.BROADCAST))

        mViewModel.showLoading = loadingObservable
        mViewModel.showLoading.value = true
        mViewModel.callFoodieCheckRequest()

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
                println("RRR :: Navigation url = $url")
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } else ViewUtils.showToast(this, getString(R.string.no_valid_loction), false)
        }
    }

    override fun onBackPressed() {

    }

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            val location = intent!!.getParcelableExtra<Location>(BaseLocationService.EXTRA_LOCATION)
            if (location != null) {
                mViewModel.latitude.value = location.latitude
                mViewModel.longitude.value = location.longitude
                if (checkStatusApiCounter++ % 3 == 0)
                    mViewModel.callFoodieCheckRequest()
                else loadingObservable.value = false
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

                    writePreferences("RequestId", it.responseData.requests.id)
                    writePreferences("userId", it.responseData.requests.user_id)
                    writePreferences("providerId", it.responseData.requests.provider_id)
                    writePreferences("adminServiceId", it.responseData.requests.admin_service_id)
                    writePreferences("userFirstName", it.responseData.requests.user.first_name)
                    writePreferences("providerFirstName", it.responseData.provider_details.first_name)
                    writePreferences("serviceType", Constants.ModuleTypes.ORDER)

                    when (it.responseData.requests.status) {
                        "PROCESSING" -> whenProcessing()
                        "STARTED" -> whenStared()
                        "REACHED" -> whenReached()
                        "PICKEDUP" -> whenPickedUp()
                        "COMPLETED" -> whenPaid()
                    }
                }
            } else finish()
        })

        mViewModel.foodieUpdateRequestModel.observe(this, Observer {
            mViewModel.callFoodieCheckRequest()
        })
    }

    private fun whenPaid() {
        changeToFlowIconView(true)
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, true)
        setOrderId()
        setUserLocDetails()
        setItemsPricing()
        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)
        mBinding.orderStatusBtn.text = getString(R.string.payment_received)

        mBinding.iconOrderPickedUp.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
        mBinding.iconReachedRestaurant.background = ContextCompat.getDrawable(this, R.drawable.round_accent)
        mBinding.iconOrderPickedUp.imageTintList = ContextCompat.getColorStateList(this, R.color.white)

        mBinding.iconOrderDelivered.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
        mBinding.iconOrderDelivered.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconReachedRestaurant.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconOrderPickedUp.background = ContextCompat.getDrawable(this, R.drawable.round_accent)

        mBinding.iconPaymentReceived.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
        mBinding.iconPaymentReceived.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconOrderDelivered.background = ContextCompat.getDrawable(this, R.drawable.round_accent)

        mBinding.iconPaymentReceived.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
        mBinding.iconPaymentReceived.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconOrderDelivered.background = ContextCompat.getDrawable(this, R.drawable.round_accent)
        mBinding.orderStatusBtn.text = getString(R.string.payment_received)
        showingStoreDetail = false
    }

    private fun whenPickedUp() {
        changeToFlowIconView(true)
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, true)
        setOrderId()
        setUserLocDetails()
        setItemsPricing()
        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)

        mBinding.iconOrderPickedUp.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
        mBinding.iconReachedRestaurant.background = ContextCompat.getDrawable(this, R.drawable.round_accent)
        mBinding.iconOrderPickedUp.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconReachedRestaurant.imageTintList = ContextCompat.getColorStateList(this, R.color.white)

        mBinding.orderStatusBtn.text = getString(R.string.order_delivered)
        mBinding.iconOrderDelivered.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
        mBinding.iconOrderDelivered.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        mBinding.iconOrderPickedUp.background = ContextCompat.getDrawable(this, R.drawable.round_accent)
        showingStoreDetail = false
    }

    private fun whenReached() {
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
        changeToFlowIconView(true)
        setOrderId()
        setUserLocDetails()
        setItemsPricing()

        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)
        mBinding.orderStatusBtn.text = getString(R.string.order_picked_up)
        mBinding.iconOrderPickedUp.background = ContextCompat.getDrawable(this, R.drawable.round_grey)
        mBinding.iconReachedRestaurant.background = ContextCompat.getDrawable(this, R.drawable.round_accent)
        mBinding.iconOrderPickedUp.imageTintList = ContextCompat.getColorStateList(this, R.color.white)
        showingStoreDetail = false
    }

    private fun setUserLocDetails() {
        Glide.with(baseContext).load(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.picture)
                .placeholder(R.drawable.foodie_profile_placeholder).into(resturant_image)
        loc_name_tv.text = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.first_name + " " +
                mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.last_name
        loc_address_tv.text = mViewModel.foodieCheckRequestModel.value!!.responseData.requests.delivery.map_address
    }

    private fun whenProcessing() {
        changeToFlowIconView(false)
        writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
        setOrderId()
        setRestaurantDetails()
        setItemsPricing()
        setPaymentDetails(mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice)
        showingStoreDetail = true
    }

    private fun whenStared() {
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
                .placeholder(R.drawable.foodie_profile_placeholder)
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
        if (order_invoice.gross.toDouble() > 0)
            item_total_tv.text = currency + order_invoice.gross
        when {
            order_invoice.gross.toDouble() > 0 -> {
                item_total_tv.text = currency + order_invoice.gross
                item_total_lt.visibility = VISIBLE
            }
            else -> item_total_lt.visibility = GONE
        }
        when {
            order_invoice.tax_amount.toDouble() > 0 -> {
                servicetax_tv.text = currency + order_invoice.tax_amount
                service_tax_lt.visibility = VISIBLE
            }
            else -> service_tax_lt.visibility = GONE
        }
        when {
            order_invoice.delivery_amount.toDouble() > 0 -> {
                delivery_charge_tv.text = currency + order_invoice.delivery_amount
                delivery_charge_lt.visibility = VISIBLE
            }
            else -> delivery_charge_lt.visibility = GONE
        }
        when {
            order_invoice.promocode_amount.toDouble() > 0 -> {
                promocode_deduction_tv.text = currency + order_invoice.promocode_amount
                promocode_deduction_lt.visibility = VISIBLE
            }
            else -> promocode_deduction_lt.visibility = GONE
        }
        when {
            order_invoice.discount.toDouble() > 0 -> {
                discount_amount_tv.text = currency + order_invoice.discount
                discount_lt.visibility = VISIBLE
            }
            else -> discount_lt.visibility = GONE
        }
        when {
            order_invoice.wallet_amount.toDouble() > 0 -> {
                wallet_amount_tv.text = currency + order_invoice.wallet_amount
                wallet_amount_lt.visibility = VISIBLE
            }
            else -> wallet_amount_lt.visibility = GONE
        }
        when {
            order_invoice.store_package_amount.toDouble() > 0 -> {
                package_amount_tv.text = currency + order_invoice.store_package_amount
                package_amount_lt.visibility = VISIBLE
            }
            else -> package_amount_lt.visibility = GONE
        }
        when {
            order_invoice.total_amount.toDouble() > 0 -> {
                total_value_tv.text = currency + order_invoice.total_amount
                total_value_lt.visibility = VISIBLE
            }
            else -> total_value_lt.visibility = GONE
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun checkOrderDeliverStatus() {
        when (order_status_btn.text) {
            getString(R.string.started_towards_restaturant) -> mViewModel.callFoodieUpdateRequest("STARTED")
            getString(R.string.reached_restaurant) -> mViewModel.callFoodieUpdateRequest("REACHED")
            getString(R.string.order_picked_up) -> mViewModel.callFoodieUpdateRequest("PICKEDUP")
            getString(R.string.order_delivered) -> {
                val otpDialogFragment = FoodieVerifyOtpDialog.newInstance(
                        mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_otp,
                        mViewModel.foodieCheckRequestModel.value!!.responseData.requests.id,
                        mViewModel.foodieCheckRequestModel.value!!.responseData.requests.order_invoice.payable
                )
                otpDialogFragment.show(supportFragmentManager, "VerifyOtpDialog")
                otpDialogFragment.isCancelable = false
            }
            getString(R.string.payment_received) -> {
                writePreferences(PreferencesKey.CAN_SEND_LOCATION, false)
                val bundle = Bundle()
                bundle.putString("id", mViewModel.foodieCheckRequestModel.value!!.responseData.requests.id.toString())
                if (mViewModel.foodieCheckRequestModel.value!!.responseData.requests.user.picture!=null)
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
//                time_left_rl.visibility = VISIBLE
            service_flow_icon_llayout.visibility = GONE
            order_status_btn.text = getString(R.string.started_towards_restaturant)
        }
    }
}