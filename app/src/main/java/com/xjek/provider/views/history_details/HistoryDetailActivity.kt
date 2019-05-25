package com.xjek.provider.views.history_details

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
import com.xjek.base.base.BaseActivity
import com.xjek.base.utils.Constants
import com.xjek.base.utils.ViewUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityCurrentorderDetailLayoutBinding
import com.xjek.provider.databinding.DisputeResonDialogBinding
import com.xjek.provider.databinding.DisputeStatusBinding
import com.xjek.provider.models.*
import com.xjek.provider.utils.CommanMethods
import com.xjek.provider.views.adapters.DisputeReasonListAdapter
import com.xjek.provider.views.adapters.ReasonListClicklistner
import com.xjek.provider.views.dashboard.DashBoardViewModel
import kotlinx.android.synthetic.main.view_recepit.*


class HistoryDetailActivity : BaseActivity<ActivityCurrentorderDetailLayoutBinding>(),
        CurrentOrderDetailsNavigator {
    lateinit var mViewDataBinding: ActivityCurrentorderDetailLayoutBinding
    lateinit var dashboardViewModel: DashBoardViewModel
    private var mselectedDisputeName: String? = null
    private var selectedDiputeData: DisputeListData? = null
    lateinit var historyDetailViewModel: HistoryDetailViewModel
    private var historyType: String? = ""
    private var selectedId: String? = ""
    private var serviceType: String? = ""
    private var disputeListBinding: DisputeResonDialogBinding? = null
    private var disputeStatusBinding: DisputeStatusBinding? = null
    private var bottomSheetDialog: BottomSheetDialog? = null
    private var isShowDisputeStatus: Boolean = false


    // Get Layout
    override fun getLayoutId(): Int = R.layout.activity_currentorder_detail_layout

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityCurrentorderDetailLayoutBinding
        historyDetailViewModel = HistoryDetailViewModel()
        dashboardViewModel = ViewModelProviders.of(this).get(DashBoardViewModel::class.java)
        this.mViewDataBinding.currentOrderDetailModel = historyDetailViewModel
        historyDetailViewModel.navigator = this
        loadingObservable.value = true
        mViewDataBinding.upcmngCancelBtn.visibility = View.GONE
        //Get Intent Values
        getIntentValues()
        historyDetailViewModel.serviceType.value = serviceType
        if (historyType.equals("past")) {
            println("BBBB" + "   " + serviceType.toString())
            if (serviceType.equals("transport"))
                historyDetailViewModel.getTransportHistoryDetail(selectedId.toString())
            else if (serviceType.equals("service"))
                historyDetailViewModel.getServiceHistoryDetail(selectedId.toString())
            else if (serviceType.equals("order"))
                historyDetailViewModel.getOrderHistoryDetail(selectedId.toString())
        }

        // Get Api Response
        apiResponse()

    }

    fun getIntentValues() {
        historyType = if (intent != null && intent.hasExtra("history_type")) intent.getStringExtra("history_type") else ""
        selectedId = if (intent != null && intent.hasExtra("selected_trip_id")) intent.getStringExtra("selected_trip_id") else ""
        serviceType = if (intent != null && intent.hasExtra("serviceType")) intent.getStringExtra("serviceType") else ""
    }


    fun apiResponse() {
        historyDetailViewModel.historyModelLiveData.observe(this@HistoryDetailActivity,
                Observer<HistoryDetailModel> {
                    loadingObservable.value = false
                    if (serviceType.equals("transport"))
                        setupTransportDetail(it.responseData.transport)
                    else if (serviceType.equals("order"))
                        setupOrderHistoryDetail(it.responseData.order)
                    else
                        setupServiceDetail(it.responseData.service)
                })

        historyDetailViewModel.disputeListData.observe(this@HistoryDetailActivity,
                Observer<DisputeListModel> {
                    loadingObservable.value = false
                    disputeListBinding!!.llProgress.visibility = View.GONE
                    disputeListBinding!!.disputeReasonFrghomeRv.visibility = View.VISIBLE
                    Log.d("_D_Detailview", it.responseData[0].dispute_name)
                    setDisputeListData(it.responseData)
                })


        historyDetailViewModel.postDisputeLiveData.observe(this@HistoryDetailActivity, Observer<DisputeStatus> {
            loadingObservable.value = false
            if (it.statusCode.equals("200")) {
                isShowDisputeStatus = true
                mViewDataBinding.disputeBtn.setText(resources.getString(R.string.dispute_status))
                ViewUtils.showToast(this@HistoryDetailActivity, resources.getString(R.string.dispute_created_succefully), true)
            }
        })


        historyDetailViewModel.disputeStatusLiveData.observe(this@HistoryDetailActivity,
                Observer<DisputeStatusModel> {
                    loadingObservable.value = false
                    showDisputeStatus(it)
                })


        historyDetailViewModel.getErrorObservable().observe(this@HistoryDetailActivity,
                Observer<String> { message ->
                    loadingObservable.value = false
                    ViewUtils.showToast(this@HistoryDetailActivity, message, false)
                })

    }


    override fun goBack() {
        finish()
    }

    override fun onClickDispute() {
        getDisputeList()
    }

    private fun showDisputeStatus(disputeStatusResponseData: DisputeStatusModel) {
        disputeStatusBinding = DataBindingUtil.inflate<DisputeStatusBinding>(LayoutInflater.from(baseContext), R.layout.dispute_status, null, false)
        disputeStatusBinding!!.disputeComment.text = (disputeStatusResponseData.responseData!!.dispute_name)
        disputeStatusBinding!!.disputeStatus.text = (disputeStatusResponseData.responseData.status)

        if (disputeStatusResponseData.responseData.status.equals("open")) {
            disputeStatusBinding!!.disputeStatus.background = ContextCompat.getDrawable(this@HistoryDetailActivity, R.drawable.bg_dispute_open)
            disputeStatusBinding!!.disputeStatus.setTextColor(ContextCompat.getColor(this@HistoryDetailActivity, R.color.dispute_status_closed))
        } else {
            disputeStatusBinding!!.disputeStatus.background = ContextCompat.getDrawable(this@HistoryDetailActivity, R.drawable.bg_dispute_close)
            disputeStatusBinding!!.disputeStatus.setTextColor(ContextCompat.getColor(this@HistoryDetailActivity, R.color.dispute_status_open))
        }

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(disputeStatusBinding!!.root)
        dialog.show()
    }

    override fun onClickViewRecepit() {

        showInvoiceAlertDialog()
    }

    private fun showInvoiceAlertDialog() {
        val invoiceDialogView = LayoutInflater.from(this).inflate(R.layout.view_recepit, null, false);
        val builder = AlertDialog.Builder(this)
        builder.setView(invoiceDialogView)
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.findViewById<ImageView>(R.id.cancel_dialog_img)!!.setOnClickListener { alertDialog.dismiss() }
        if (serviceType.equals("transport")) {
            alertDialog.llServiceInvoice.visibility = View.GONE
            alertDialog.llOrderInvoice.visibility = View.GONE
            alertDialog.llTaxiInvoice.visibility = View.VISIBLE
            setUpTransportInvoice(alertDialog)

        } else if (serviceType.equals("order")) {
            alertDialog.llServiceInvoice.visibility = View.GONE
            alertDialog.llOrderInvoice.visibility = View.VISIBLE
            alertDialog.llTaxiInvoice.visibility = View.GONE
            setUpOrderInvoice(alertDialog)

        } else if (serviceType.equals("service")) {
            alertDialog.llServiceInvoice.visibility = View.VISIBLE
            alertDialog.llOrderInvoice.visibility = View.GONE
            alertDialog.llTaxiInvoice.visibility = View.GONE
            setUpServiceInvoice(alertDialog)
        }
        alertDialog.show()
    }


    override fun onClickLossItem() {
//        val inflate = DataBindingUtil.inflate<LossitemCommentDialogBinding>(LayoutInflater.from(baseContext)
//                , R.layout.lossitem_comment_dialog, null, false)
//
//
//        val dialog = BottomSheetDialog(this)
//        dialog.setContentView(inflate.root)
//        dialog.show()
//
//        inflate.sendBtn.setOnClickListener {
//            loadingObservable.value = true
//            val lostitem = inflate.lossitemEt.text
//            historyDetailViewModel.addLossItem(transpotResponseData.id, lostitem)
//            dialog.dismiss()
//
//        }
    }


    override fun onClickCancelBtn() {
        val builder = AlertDialog.Builder(this@HistoryDetailActivity)
        builder.setTitle("Upcoming Ride")
        builder.setMessage("Are you want to cancel the request?")
        builder.setPositiveButton("YES") { dialog, which ->
            //api call
            ViewUtils.showToast(baseContext, "success canceled", true)

        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    private fun getDisputeList() {
        historyDetailViewModel.getDisputeList()
    }

    private fun setDisputeListData(disputeListData: List<DisputeListData>) {
        disputeListBinding!!.applyFilter.isEnabled = true
        disputeListBinding!!.disputeReasonListAdapter = DisputeReasonListAdapter(historyDetailViewModel, disputeListData)
        disputeListBinding!!.disputeReasonListAdapter!!.setOnClickListener(mOnAdapterClickListener)
        historyDetailViewModel.selectedDisputeModel.observe(this, Observer {
            bottomSheetDialog!!.dismiss()
            selectedDiputeData = it
            createDisputeRequest()
        })
    }

    override fun showDisputeList() {
        if (isShowDisputeStatus == false) {
            disputeListBinding = DataBindingUtil.inflate(LayoutInflater.from(this@HistoryDetailActivity), R.layout.dispute_reson_dialog, null, false)
            disputeListBinding!!.applyFilter.isEnabled = false
            bottomSheetDialog = BottomSheetDialog(this@HistoryDetailActivity)
            bottomSheetDialog!!.setContentView(disputeListBinding!!.root)
            bottomSheetDialog!!.show()
            historyDetailViewModel.getDisputeList()
        } else {
            if (serviceType.equals("transport")) {
                historyDetailViewModel.getTransPortDisputeStatus(historyDetailViewModel.transportDetail.value!!.id.toString())
            } else if (serviceType.equals("order")) {
                historyDetailViewModel.getServiceHistoryDetail(historyDetailViewModel.serviceDetail.value!!.id.toString())
            } else {
                historyDetailViewModel.getOrderDisputeStatus(historyDetailViewModel.orderDetaIL.value!!.id.toString())
            }
        }
    }

    private fun createDisputeRequest() {
        if (selectedDiputeData != null) {
            historyDetailViewModel.disputeType.value = "provider"
            historyDetailViewModel.disputeName.value = selectedDiputeData?.let { it.dispute_name }
            historyDetailViewModel.disputeID.value = selectedDiputeData?.let { it.id }
        }

        val params = HashMap<String, String>()
        params.put(com.xjek.base.data.Constants.Dispute.DIPUSTE_TYPE, historyDetailViewModel.disputeType.value!!)
        params.put(com.xjek.base.data.Constants.Dispute.DISPUTE_NAME, historyDetailViewModel.disputeName.value!!)

        if (serviceType.equals("transport")) {
            historyDetailViewModel.userID.value = historyDetailViewModel.transportDetail.value!!.user!!.id.toString()
            historyDetailViewModel.providerID.value = historyDetailViewModel.transportDetail.value!!.provider_id.toString()
            historyDetailViewModel.requestID.value = historyDetailViewModel.transportDetail.value!!.id.toString()
            params.put(com.xjek.base.data.Constants.Dispute.REQUEST_ID, historyDetailViewModel.requestID.value.toString())
            params.put(com.xjek.base.data.Constants.Dispute.PROVIDER_ID, historyDetailViewModel.providerID.value.toString())
            params.put(com.xjek.base.data.Constants.Dispute.USER_ID, historyDetailViewModel.userID.value!!.toString())

            historyDetailViewModel.postTaxiDispute(params)

        } else if (serviceType.equals("service")) {
            historyDetailViewModel.userID.value = historyDetailViewModel.historyDetailResponse.value!!.responseData.service.user!!.id.toString()
            historyDetailViewModel.providerID.value = historyDetailViewModel.historyDetailResponse.value!!.responseData.service.provider_id.toString()
            historyDetailViewModel.requestID.value = historyDetailViewModel.historyDetailResponse.value!!.responseData.service.id.toString()
            params.put(com.xjek.base.data.Constants.Dispute.PROVIDER_ID, historyDetailViewModel.providerID.value.toString())
            params.put(com.xjek.base.data.Constants.Dispute.USER_ID, historyDetailViewModel.userID.value!!.toString())
            historyDetailViewModel.postServiceDispute(params, historyDetailViewModel.requestID.value!!)

        } else {
            historyDetailViewModel.userID.value = historyDetailViewModel.historyDetailResponse.value!!.responseData.order.user!!.id.toString()
            historyDetailViewModel.providerID.value = historyDetailViewModel.historyDetailResponse.value!!.responseData.order.provider_id.toString()
            historyDetailViewModel.requestID.value = historyDetailViewModel.historyDetailResponse.value!!.responseData.order.id.toString()
            historyDetailViewModel.storeID.value = historyDetailViewModel.historyDetailResponse.value!!.responseData.order.order_invoice!!.items!!.get(0)!!.store_id.toString()

            params.put(com.xjek.base.data.Constants.Dispute.PROVIDER_ID, historyDetailViewModel.providerID.value.toString())
            params.put(com.xjek.base.data.Constants.Dispute.USER_ID, historyDetailViewModel.userID.value!!.toString())
            params.put(com.xjek.base.data.Constants.Dispute.STORE_ID, historyDetailViewModel.storeID.value.toString())
            historyDetailViewModel.postOrderDispute(params)

        }


    }


    private val mOnAdapterClickListener = object : ReasonListClicklistner {
        override fun reasonOnItemClick(disputeName: String) {
            historyDetailViewModel.setSelectedValue(disputeName)
        }
    }


    private fun setupTransportDetail(transPastDetail: HistoryDetailModel.ResponseData.Transport) {
        historyDetailViewModel.transportDetail.value = transPastDetail
        mViewDataBinding.currentorderdetailTitleTv.text = transPastDetail.booking_id
        mViewDataBinding.currentorderdetailDateTv.text = (CommanMethods.getLocalTimeStamp(transPastDetail.assigned_at!!, "Req_Date_Month") + "")
        mViewDataBinding.timeCurrentorderdetailTv.text = (CommanMethods.getLocalTimeStamp(transPastDetail.assigned_at!!, "Req_time") + "")
        mViewDataBinding.historydetailSrcValueTv.text = transPastDetail.s_address
        mViewDataBinding.historydetailDestValueTv.text = transPastDetail.d_address
        mViewDataBinding.historydetailStatusValueTv.text = transPastDetail.status
        mViewDataBinding.historydetailPaymentmodeValTv.text = transPastDetail.payment_mode
        mViewDataBinding.vechileTypeTv.text = (transPastDetail.ride!!.vehicle_name)
        Glide.with(this@HistoryDetailActivity).load(transPastDetail.user!!.picture)
                .into(mViewDataBinding.providerCimgv)
        mViewDataBinding.providerNameTv.text = (transPastDetail.user.first_name + " " +
                transPastDetail.user.last_name)
        mViewDataBinding.rvUser.rating = transPastDetail.provider_rated!!.toFloat()

        if (transPastDetail.dispute != null) {
            mViewDataBinding.disputeBtn.text = getString(R.string.dispute_status)
            isShowDisputeStatus = true
        }
    }

    private fun setupServiceDetail(serviceDetail: HistoryDetailModel.ResponseData.Service) {
        historyDetailViewModel.serviceDetail.value = serviceDetail
        mViewDataBinding.currentorderdetailTitleTv.text = serviceDetail.booking_id
        mViewDataBinding.currentorderdetailDateTv.text = (CommanMethods.getLocalTimeStamp(serviceDetail.started_at!!,
                "Req_Date_Month") + "")
        mViewDataBinding.timeCurrentorderdetailTv.text = (CommanMethods.getLocalTimeStamp(serviceDetail.started_at, "Req_time") + "")
        mViewDataBinding.historydetailSrcValueTv.text = serviceDetail.s_address
        mViewDataBinding.historydetailStatusValueTv.text = serviceDetail.status
        mViewDataBinding.historydetailPaymentmodeValTv.text = serviceDetail.payment!!.payment_mode
        Glide.with(this@HistoryDetailActivity).load(serviceDetail.user!!.picture)
                .into(mViewDataBinding.providerCimgv)
        mViewDataBinding.providerNameTv.text = (serviceDetail.user.first_name + " " +
                serviceDetail.user.last_name)
        mViewDataBinding.rvUser.rating = serviceDetail.user!!.rating!!.toFloat()
        if (serviceDetail.dispute != null) {
            mViewDataBinding.disputeBtn.text = getString(R.string.dispute_status)
            isShowDisputeStatus = true
        }
    }

    private fun setupOrderHistoryDetail(orderDetail: HistoryDetailModel.ResponseData.Order) {
        historyDetailViewModel.orderDetaIL.value = orderDetail
        mViewDataBinding.currentorderdetailTitleTv.text = orderDetail.store_order_invoice_id
        mViewDataBinding.currentorderdetailDateTv.text = (CommanMethods.getLocalTimeStamp(orderDetail.created_at!!,
                "Req_Date_Month") + "")
        mViewDataBinding.timeCurrentorderdetailTv.text = (CommanMethods.getLocalTimeStamp(orderDetail.created_at!!, "Req_time") + "")
        mViewDataBinding.historydetailSrcValueTv.text = orderDetail.pickup!!.store_location
        mViewDataBinding.historydetailDestValueTv.text = orderDetail.delivery!!.flat_no + " " + orderDetail.delivery.street
        mViewDataBinding.historydetailStatusValueTv.text = orderDetail.status
        mViewDataBinding.historydetailPaymentmodeValTv.text = orderDetail.order_invoice!!.payment_mode
        Glide.with(this@HistoryDetailActivity).load(orderDetail.user!!.picture).into(mViewDataBinding.providerCimgv)
        mViewDataBinding.providerNameTv.text = (orderDetail.user.first_name + " " + orderDetail.user.last_name)
        mViewDataBinding.rvUser.rating = orderDetail.user!!.rating!!.toFloat()
        if (orderDetail.dispute != null) {
            mViewDataBinding.disputeBtn.text = getString(R.string.dispute_status)
            isShowDisputeStatus = true
        }

    }


    fun setUpTransportInvoice(alertDialog: AlertDialog) {
        alertDialog.findViewById<TextView>(R.id.tvTaxiFare)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.fixed)
        alertDialog.findViewById<TextView>(R.id.tvTaxiHourlyFare)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.hour)
        alertDialog.findViewById<TextView>(R.id.tvTaxiDiscount)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.discount)
        alertDialog.findViewById<TextView>(R.id.tvTaxiTips)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.tips)
        alertDialog.findViewById<TextView>(R.id.tvTaxiWaitingTime)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.waiting_amount)
        alertDialog.findViewById<TextView>(R.id.tvTaxiTollCharge)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.toll_charge)
        alertDialog.findViewById<TextView>(R.id.tvInvoiceTotal)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.transport.payment!!.payable)
    }


    fun setUpServiceInvoice(alertDialog: AlertDialog) {
        alertDialog.findViewById<TextView>(R.id.tvServiceFare)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.service.payment!!.fixed)
        alertDialog.findViewById<TextView>(R.id.tvServiceTax)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.service.payment!!.tax)
        alertDialog.findViewById<TextView>(R.id.tvServiceTime)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.service.payment!!.minute)
        alertDialog.findViewById<TextView>(R.id.tvServiceExtraCharge)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.service.payment!!.extra_charges)
        alertDialog.findViewById<TextView>(R.id.tvInvoiceTotal)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.service.payment!!.payable
                )
    }

    fun setUpOrderInvoice(alertDialog: AlertDialog) {
        alertDialog.findViewById<TextView>(R.id.tvOrderBaseFare)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.gross)
        alertDialog.findViewById<TextView>(R.id.tvOrderTaxFare)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.tax_amount)
        alertDialog.findViewById<TextView>(R.id.tvOrderDeliveryCharge)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.delivery_amount)
        alertDialog.findViewById<TextView>(R.id.tvOrderPackCharge)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.store_package_amount)
        alertDialog.findViewById<TextView>(R.id.tvOrderPrmocode)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.promocode_amount)
        alertDialog.findViewById<TextView>(R.id.tvInvoiceTotal)!!.text = (Constants.currency + historyDetailViewModel.historyModelLiveData.value!!.responseData.order.order_invoice!!.payable)

    }

    override fun showErrorMessage(error: String) {
        ViewUtils.showToast(this@HistoryDetailActivity, error, false)
    }


}


