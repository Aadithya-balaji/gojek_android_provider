package com.gox.partner.views.history_details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.gox.base.base.BaseActivity
import com.gox.base.data.Constants
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.ActivityCurrentorderDetailLayoutBinding
import com.gox.partner.databinding.DisputeResonDialogBinding
import com.gox.partner.databinding.DisputeStatusBinding
import com.gox.partner.models.*
import com.gox.partner.utils.CommonMethods
import com.gox.partner.views.adapters.DisputeReasonListAdapter
import com.gox.partner.views.adapters.ReasonListClickListener
import com.gox.partner.views.dashboard.DashBoardViewModel
import kotlinx.android.synthetic.main.view_recepit.*

class HistoryDetailActivity : BaseActivity<ActivityCurrentorderDetailLayoutBinding>(),
        CurrentOrderDetailsNavigator {

    lateinit var mBinding: ActivityCurrentorderDetailLayoutBinding
    lateinit var mViewModel: HistoryDetailViewModel

    private lateinit var dashboardViewModel: DashBoardViewModel

    private var disputeListBinding: DisputeResonDialogBinding? = null
    private var disputeStatusBinding: DisputeStatusBinding? = null
    private var selectedDisputeData: DisputeListData? = null
    private var bottomSheetDialog: BottomSheetDialog? = null

    private var historyType: String? = ""
    private var selectedId: String? = ""
    private var serviceType: String? = ""
    private var isShowDisputeStatus: Boolean = false

    override fun getLayoutId() = R.layout.activity_currentorder_detail_layout

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mBinding = mViewDataBinding as ActivityCurrentorderDetailLayoutBinding
        mViewModel = HistoryDetailViewModel()
        dashboardViewModel = ViewModelProviders.of(this).get(DashBoardViewModel::class.java)
        this.mBinding.currentOrderDetailModel = mViewModel
        mViewModel.navigator = this
        loadingObservable.value = true
        mViewModel.showLoading = loadingObservable
        mViewDataBinding.upcmngCancelBtn.visibility = View.GONE
        getIntentValues()
        mViewModel.serviceType.value = serviceType
        if (historyType.equals("past")) {
            println("BBBB" + "   " + serviceType.toString())
            when {
                serviceType.equals("transport") -> mViewModel.getTransportHistoryDetail(selectedId.toString())
                serviceType.equals("service") -> mViewModel.getServiceHistoryDetail(selectedId.toString())
                serviceType.equals("order") -> mViewModel.getOrderHistoryDetail(selectedId.toString())
            }
        }

        apiResponse()
    }

    private fun getIntentValues() {
        historyType = if (intent != null && intent.hasExtra("history_type"))
            intent.getStringExtra("history_type") else ""
        selectedId = if (intent != null && intent.hasExtra("selected_trip_id"))
            intent.getStringExtra("selected_trip_id") else ""
        serviceType = if (intent != null && intent.hasExtra("serviceType"))
            intent.getStringExtra("serviceType") else ""
    }

    private fun apiResponse() {
        mViewModel.historyModelLiveData.observe(this,
                Observer<HistoryDetailModel> {
                    loadingObservable.value = false
                    when {
                        serviceType.equals("transport") -> setupTransportDetail(it.responseData.transport)
                        serviceType.equals("order") -> setupOrderHistoryDetail(it.responseData.order)
                        else -> setupServiceDetail(it.responseData.service)
                    }
                })

        mViewModel.disputeListData.observe(this,
                Observer<DisputeListModel> {
                    loadingObservable.value = false
                    disputeListBinding!!.llProgress.visibility = View.GONE
                    disputeListBinding!!.disputeReasonFrghomeRv.visibility = View.VISIBLE
                    Log.d("_D_Detailview", it.responseData[0].dispute_name)
                    setDisputeListData(it.responseData)
                })

        mViewModel.postDisputeLiveData.observe(this, Observer<DisputeStatus> {
            loadingObservable.value = false
            if (it.statusCode.equals("200")) {
                isShowDisputeStatus = true
                mBinding.disputeBtn.text = resources.getString(R.string.dispute_status)
                ViewUtils.showToast(this, resources.getString(R.string.dispute_created_succefully), true)
            }
        })

        mViewModel.disputeStatusLiveData.observe(this,
                Observer<DisputeStatusModel> {
                    loadingObservable.value = false
                    showDisputeStatus(it)
                })

        mViewModel.getErrorObservable().observe(this,
                Observer<String> { message ->
                    loadingObservable.value = false
                    ViewUtils.showToast(this, message, false)
                })

    }


    override fun goBack() {
        finish()
    }

    private fun showDisputeStatus(disputeStatusResponseData: DisputeStatusModel) {
        disputeStatusBinding = DataBindingUtil.inflate<DisputeStatusBinding>(LayoutInflater.from(baseContext), R.layout.dispute_status, null, false)
        disputeStatusBinding!!.disputeComment.text = (disputeStatusResponseData.responseData!!.dispute_name)
        disputeStatusBinding!!.disputeStatus.text = (disputeStatusResponseData.responseData.status)

        if (disputeStatusResponseData.responseData.status.equals("open")) {
            disputeStatusBinding!!.disputeStatus.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_dispute_open)
            disputeStatusBinding!!.disputeStatus.setTextColor(
                    ContextCompat.getColor(this, R.color.dispute_status_closed))
        } else {
            disputeStatusBinding!!.disputeStatus.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_dispute_close)
            disputeStatusBinding!!.disputeStatus.setTextColor(
                    ContextCompat.getColor(this, R.color.dispute_status_open))
        }

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(disputeStatusBinding!!.root)
        dialog.show()
    }

    override fun onClickViewReceipt() = showInvoiceAlertDialog()

    private fun showInvoiceAlertDialog() {
        val invoiceDialogView = LayoutInflater.from(this).inflate(R.layout.view_recepit, null, false)
        val builder = AlertDialog.Builder(this)
        builder.setView(invoiceDialogView)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.findViewById<ImageView>(R.id.cancel_dialog_img)!!.setOnClickListener { alertDialog.dismiss() }
        when {
            serviceType.equals("transport") -> {
                alertDialog.llServiceInvoice.visibility = View.GONE
                alertDialog.llOrderInvoice.visibility = View.GONE
                alertDialog.llTaxiInvoice.visibility = View.VISIBLE
                setUpTransportInvoice(alertDialog)

            }
            serviceType.equals("order") -> {
                alertDialog.llServiceInvoice.visibility = View.GONE
                alertDialog.llOrderInvoice.visibility = View.VISIBLE
                alertDialog.llTaxiInvoice.visibility = View.GONE
                setUpOrderInvoice(alertDialog)

            }
            serviceType.equals("service") -> {
                alertDialog.llServiceInvoice.visibility = View.VISIBLE
                alertDialog.llOrderInvoice.visibility = View.GONE
                alertDialog.llTaxiInvoice.visibility = View.GONE
                setUpServiceInvoice(alertDialog)
            }
        }
        alertDialog.show()
    }

    override fun onClickLossItem() {
    }

    override fun onClickCancelBtn() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.upcoming_ride))
        builder.setMessage(getString(R.string.desc_upcoming_ride))
        builder.setPositiveButton(getString(R.string.yes)) { dialog, which ->
            ViewUtils.showToast(baseContext, "success canceled", true)
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun setDisputeListData(disputeListData: List<DisputeListData>) {
        disputeListBinding!!.applyFilter.isEnabled = true
        disputeListBinding!!.disputeReasonListAdapter = DisputeReasonListAdapter(mViewModel, disputeListData)
        disputeListBinding!!.disputeReasonListAdapter!!.setOnClickListener(mOnAdapterClickListener)
        mViewModel.selectedDisputeModel.observe(this, Observer {
            bottomSheetDialog!!.dismiss()
            selectedDisputeData = it
            createDisputeRequest()
        })
    }

    override fun showDisputeList() {
        if (!isShowDisputeStatus) {
            disputeListBinding = DataBindingUtil.inflate(LayoutInflater.from(this),
                    R.layout.dispute_reson_dialog, null, false)
            disputeListBinding!!.applyFilter.isEnabled = false
            bottomSheetDialog = BottomSheetDialog(this)
            bottomSheetDialog!!.setContentView(disputeListBinding!!.root)
            bottomSheetDialog!!.show()
            mViewModel.getDisputeList()
        } else {
            when {
                serviceType.equals("transport") -> mViewModel.getTransPortDisputeStatus(
                        mViewModel.transportDetail.value!!.id.toString())
                serviceType.equals("order") -> mViewModel.getServiceHistoryDetail(
                        mViewModel.serviceDetail.value!!.id.toString())
                else -> mViewModel.getOrderDisputeStatus(
                        mViewModel.orderDetail.value!!.id.toString())
            }
        }
    }

    private fun createDisputeRequest() {
        if (selectedDisputeData != null) {
            mViewModel.disputeType.value = "provider"
            mViewModel.disputeName.value = selectedDisputeData?.dispute_name
            mViewModel.disputeID.value = selectedDisputeData?.id
        }

        val params = HashMap<String, String>()
        params[Constants.Dispute.DISPUTE_TYPE] = mViewModel.disputeType.value!!
        params[Constants.Dispute.DISPUTE_NAME] = mViewModel.disputeName.value!!

        when {
            serviceType.equals("transport") -> {
                mViewModel.userID.value = mViewModel.transportDetail.value!!.user!!.id.toString()
                mViewModel.providerID.value = mViewModel.transportDetail.value!!.provider_id.toString()
                mViewModel.requestID.value = mViewModel.transportDetail.value!!.id.toString()
                params[Constants.Dispute.REQUEST_ID] = mViewModel.requestID.value.toString()
                params[Constants.Dispute.PROVIDER_ID] = mViewModel.providerID.value.toString()
                params[Constants.Dispute.USER_ID] = mViewModel.userID.value!!.toString()

                mViewModel.postTaxiDispute(params)

            }
            serviceType.equals("service") -> {
                mViewModel.userID.value = mViewModel.historyDetailResponse.value!!.responseData.service.user!!.id.toString()
                mViewModel.providerID.value = mViewModel.historyDetailResponse.value!!.responseData.service.provider_id.toString()
                mViewModel.requestID.value = mViewModel.historyDetailResponse.value!!.responseData.service.id.toString()
                params[Constants.Dispute.PROVIDER_ID] = mViewModel.providerID.value.toString()
                params[Constants.Dispute.USER_ID] = mViewModel.userID.value!!.toString()
                mViewModel.postServiceDispute(params, mViewModel.requestID.value!!)

            }
            else -> {
                mViewModel.userID.value = mViewModel.historyDetailResponse.value!!.responseData.order.user!!.id.toString()
                mViewModel.providerID.value = mViewModel.historyDetailResponse.value!!.responseData.order.provider_id.toString()
                mViewModel.requestID.value = mViewModel.historyDetailResponse.value!!.responseData.order.id.toString()
                mViewModel.storeID.value = mViewModel.historyDetailResponse.value!!.responseData.order.order_invoice!!.items!![0]!!.store_id.toString()

                params[Constants.Dispute.PROVIDER_ID] = mViewModel.providerID.value.toString()
                params[Constants.Dispute.USER_ID] = mViewModel.userID.value!!.toString()
                params[Constants.Dispute.STORE_ID] = mViewModel.storeID.value.toString()
                mViewModel.postOrderDispute(params)
            }
        }
    }

    private val mOnAdapterClickListener = object : ReasonListClickListener {
        override fun reasonOnItemClick(disputeName: String) {
            mViewModel.setSelectedValue(disputeName)
        }
    }

    private fun setupTransportDetail(transPastDetail: HistoryDetailModel.ResponseData.Transport) {
        mViewModel.transportDetail.value = transPastDetail
        mBinding.currentorderdetailTitleTv.text = transPastDetail.booking_id
        mBinding.currentorderdetailDateTv.text = (CommonMethods.getLocalTimeStamp(transPastDetail.assigned_at!!, "Req_Date_Month") + "")
        mBinding.timeCurrentorderdetailTv.text = (CommonMethods.getLocalTimeStamp(transPastDetail.assigned_at,
                "Req_time") + "")
        mBinding.historydetailSrcValueTv.text = transPastDetail.s_address
        mBinding.historydetailDestValueTv.text = transPastDetail.d_address
        mBinding.historydetailStatusValueTv.text = transPastDetail.status
        mBinding.historydetailPaymentmodeValTv.text = transPastDetail.payment_mode
        mBinding.vechileTypeTv.text = (transPastDetail.ride!!.vehicle_name)
        Glide.with(this).load(transPastDetail.user!!.picture)
                .into(mBinding.providerCimgv)
        mBinding.providerNameTv.text = (transPastDetail.user.first_name + " " +
                transPastDetail.user.last_name)
        mBinding.rvUser.rating = transPastDetail.provider_rated!!.toFloat()

        if (transPastDetail.dispute != null) {
            mBinding.disputeBtn.text = getString(R.string.dispute_status)
            isShowDisputeStatus = true
        }
    }

    private fun setupServiceDetail(serviceDetail: HistoryDetailModel.ResponseData.Service) {
        mViewModel.serviceDetail.value = serviceDetail
        mBinding.currentorderdetailTitleTv.text = serviceDetail.booking_id
        mBinding.currentorderdetailDateTv.text = (CommonMethods.getLocalTimeStamp(serviceDetail.started_at!!,
                "Req_Date_Month") + "")
        mBinding.timeCurrentorderdetailTv.text = (CommonMethods.getLocalTimeStamp(serviceDetail.started_at,
                "Req_time") + "")
        mBinding.historydetailSrcValueTv.text = serviceDetail.s_address
        mBinding.historydetailStatusValueTv.text = serviceDetail.status
        mBinding.historydetailPaymentmodeValTv.text = serviceDetail.payment!!.payment_mode
        Glide.with(this).load(serviceDetail.user!!.picture).into(mBinding.providerCimgv)
        mBinding.providerNameTv.text = (serviceDetail.user.first_name + " " + serviceDetail.user.last_name)
        mBinding.rvUser.rating = serviceDetail.user.rating!!.toFloat()
        if (serviceDetail.dispute != null) {
            mBinding.disputeBtn.text = getString(R.string.dispute_status)
            isShowDisputeStatus = true
        }
    }

    private fun setupOrderHistoryDetail(orderDetail: HistoryDetailModel.ResponseData.Order) {
        mViewModel.orderDetail.value = orderDetail
        mBinding.currentorderdetailTitleTv.text = orderDetail.store_order_invoice_id
        mBinding.currentorderdetailDateTv.text = (CommonMethods.getLocalTimeStamp(orderDetail.created_at!!,
                "Req_Date_Month") + "")
        mBinding.timeCurrentorderdetailTv.text = (CommonMethods.getLocalTimeStamp(orderDetail.created_at, "Req_time") + "")
        mBinding.historydetailSrcValueTv.text = orderDetail.pickup!!.store_location
        mBinding.historydetailDestValueTv.text = orderDetail.delivery!!.flat_no + " " + orderDetail.delivery.street
        mBinding.historydetailStatusValueTv.text = orderDetail.status
        mBinding.historydetailPaymentmodeValTv.text = orderDetail.order_invoice!!.payment_mode
        Glide.with(this).load(orderDetail.user!!.picture).into(mBinding.providerCimgv)
        mBinding.providerNameTv.text = (orderDetail.user.first_name + " " + orderDetail.user.last_name)
        mBinding.rvUser.rating = orderDetail.user.rating!!.toFloat()
        if (orderDetail.dispute != null) {
            mBinding.disputeBtn.text = getString(R.string.dispute_status)
            isShowDisputeStatus = true
        }
    }

    private fun setUpTransportInvoice(alertDialog: AlertDialog) {
        alertDialog.findViewById<TextView>(R.id.tvTaxiFare)!!.text = (Constants.currency
                + mViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.fixed)
        alertDialog.findViewById<TextView>(R.id.tvTaxiHourlyFare)!!.text = (Constants.currency
                + mViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.hour)
        alertDialog.findViewById<TextView>(R.id.tvTaxiDiscount)!!.text = (Constants.currency
                + mViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.discount)
        alertDialog.findViewById<TextView>(R.id.tvTaxiTips)!!.text = (Constants.currency
                + mViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.tips)
        alertDialog.findViewById<TextView>(R.id.tvTaxiWaitingTime)!!.text = (Constants.currency
                + mViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.waiting_amount)
        alertDialog.findViewById<TextView>(R.id.tvTaxiTollCharge)!!.text = (Constants.currency
                + mViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.toll_charge)
        alertDialog.findViewById<TextView>(R.id.tvInvoiceTotal)!!.text = (Constants.currency
                + mViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.payable)
    }

    private fun setUpServiceInvoice(alertDialog: AlertDialog) {
        alertDialog.findViewById<TextView>(R.id.tvServiceFare)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.service.payment!!.fixed)
        alertDialog.findViewById<TextView>(R.id.tvServiceTax)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.service.payment!!.tax)
        alertDialog.findViewById<TextView>(R.id.tvServiceTime)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.service.payment!!.minute)
        alertDialog.findViewById<TextView>(R.id.tvServiceExtraCharge)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.service.payment!!.extra_charges)
        alertDialog.findViewById<TextView>(R.id.tvInvoiceTotal)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.service.payment!!.payable)
    }

    private fun setUpOrderInvoice(alertDialog: AlertDialog) {
        alertDialog.findViewById<TextView>(R.id.tvOrderBaseFare)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.gross)
        alertDialog.findViewById<TextView>(R.id.tvOrderTaxFare)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.tax_amount)
        alertDialog.findViewById<TextView>(R.id.tvOrderDeliveryCharge)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.delivery_amount)
        alertDialog.findViewById<TextView>(R.id.tvOrderPackCharge)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.store_package_amount)
        alertDialog.findViewById<TextView>(R.id.tvOrderPrmocode)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.promocode_amount)
        alertDialog.findViewById<TextView>(R.id.tvInvoiceTotal)!!.text = (Constants.currency +
                mViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.payable)
    }

    override fun showErrorMessage(error: String) {
        ViewUtils.showToast(this, error, false)
    }
}