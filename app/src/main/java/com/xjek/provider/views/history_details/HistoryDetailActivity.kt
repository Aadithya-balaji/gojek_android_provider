package com.xjek.provider.views.history_details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import com.xjek.provider.model.*
import com.xjek.provider.models.LoginResponseModel
import com.xjek.provider.utils.CommanMethods
import com.xjek.provider.views.adapters.DisputeReasonListAdapter
import com.xjek.provider.views.adapters.ReasonListClicklistner
import com.xjek.provider.views.dashboard.DashBoardViewModel


class HistoryDetailActivity : BaseActivity<ActivityCurrentorderDetailLayoutBinding>(),
        CurrentOrderDetailsNavigator {


    lateinit var mViewDataBinding: ActivityCurrentorderDetailLayoutBinding
    lateinit var transpotResponseData: HistoryDetailTransport
    lateinit var dashboardViewModel: DashBoardViewModel
    private var mselectedDisputeName: String? = null

    override fun getLayoutId(): Int = R.layout.activity_currentorder_detail_layout
    lateinit var historyDetailViewModel: HistoryDetailViewModel

    override fun initView(mViewDataBinding: ViewDataBinding?) {

        Log.d("_D_EXT", "LOG" + intent.extras.get("selected_trip_id")!!)

        val selectedId = intent.extras.get("selected_trip_id")!!
        val history_type = intent.extras.get("history_type")!!

        this.mViewDataBinding = mViewDataBinding as ActivityCurrentorderDetailLayoutBinding
        historyDetailViewModel = HistoryDetailViewModel()
        dashboardViewModel = ViewModelProviders.of(this).get(DashBoardViewModel::class.java)

        this.mViewDataBinding.currentOrderDetailModel = historyDetailViewModel
        historyDetailViewModel.navigator = this
        loadingObservable.value = true
        if (history_type.equals("upcoming")) {
            historyDetailViewModel.getUpcomingHistoryDeatail(selectedId.toString())
        } else if (history_type.equals("past")) {
            historyDetailViewModel.getHistoryDeatail(selectedId.toString())
        }



        historyDetailViewModel.historyDetailResponse.observe(this@HistoryDetailActivity,
                Observer<HistoryDetailModel> {
                    loadingObservable.value = false
                    Log.d("_D_Detailview", it.responseData.transport.booking_id)
                    transpotResponseData = it.responseData.transport
                    setHistoryDetail()
                })

        historyDetailViewModel.historyUpcomingDetailResponse.observe(this@HistoryDetailActivity,
                Observer<HistoryDetailModel> {
                    loadingObservable.value = false
                    Log.d("_D_Detailview", it.responseData.transport.booking_id)
                    transpotResponseData = it.responseData.transport
                    setUpcomingHistoryDetail()
                })

        historyDetailViewModel.disputeListData.observe(this@HistoryDetailActivity,
                Observer<DisputeListModel> {
                    loadingObservable.value = false
                    Log.d("_D_Detailview", it.responseData[0].dispute_name)
                    setDisputeListData(it.responseData)
                })
        historyDetailViewModel.addDisputeResponse.observe(this@HistoryDetailActivity,
                Observer<LoginResponseModel.ResponseData> {
                    loadingObservable.value = false
                    mViewDataBinding.disputeBtn.text = getString(R.string.dispute_status)
                    ViewUtils.showToast(this@HistoryDetailActivity, "Dispute Created Sucessfully", true)
                })
        historyDetailViewModel.addLostItemResponse.observe(this@HistoryDetailActivity,
                Observer<LoginResponseModel.ResponseData> {
                    loadingObservable.value = false
                    ViewUtils.showToast(this@HistoryDetailActivity, "LossItem Created Sucessfully", true)

                })


        historyDetailViewModel.disputeStatusResponse.observe(this@HistoryDetailActivity,
                Observer<DisputeStatusModel> {
                    loadingObservable.value = false
                    showDisputeStatus(it.responseData)
                })


        historyDetailViewModel.getErrorObservable().observe(this@HistoryDetailActivity,
                Observer<String> { message ->
                    loadingObservable.value = false
                    ViewUtils.showToast(this@HistoryDetailActivity, message, false)
                })


    }


    private fun setHistoryDetail() {
        mViewDataBinding.upcmngCancelBtn.visibility = View.GONE
        mViewDataBinding.currentorderdetailTitleTv.text = transpotResponseData.booking_id
        mViewDataBinding.currentorderdetailDateTv.text = (CommanMethods.getLocalTimeStamp(transpotResponseData.assigned_at,
                "Req_Date_Month") + "")
        mViewDataBinding.timeCurrentorderdetailTv.text = (CommanMethods.getLocalTimeStamp(transpotResponseData
                .assigned_at, "Req_time") + "")
        mViewDataBinding.historydetailSrcValueTv.text = transpotResponseData.s_address
        mViewDataBinding.historydetailDestValueTv.text = transpotResponseData.d_address
        mViewDataBinding.historydetailStatusValueTv.text = transpotResponseData.status
        mViewDataBinding.historydetailPaymentmodeValTv.text = transpotResponseData.payment_mode
        mViewDataBinding.vechileTypeTv.text = (transpotResponseData.provider_vehicle.vehicle_model + "("
                + transpotResponseData.provider_vehicle.vehicle_no + ")")
        Glide.with(this@HistoryDetailActivity).load(transpotResponseData.provider.picture)
                .into(mViewDataBinding.providerCimgv)
        mViewDataBinding.providerNameTv.text = (transpotResponseData.provider.first_name + " " +
                transpotResponseData.provider.last_name)

        mViewDataBinding.rvUser.rating = transpotResponseData.provider_rated.toFloat()

        if (transpotResponseData.dispute != null) {
            mViewDataBinding.disputeBtn.text = getString(R.string.dispute_status)
        }

        if (transpotResponseData.lost_item != null) {
            mViewDataBinding.historydetailLossItemImgv.visibility = View.GONE
            mViewDataBinding.lostItemTitle.text = "Lost Item Created"
            mViewDataBinding.lostItemList.text = transpotResponseData.lost_item.lost_item_name
            mViewDataBinding.lostItemStatusTv.text = transpotResponseData.lost_item.status

        } else {
            mViewDataBinding.lostItemStatusTv.visibility = View.GONE
        }
    }

    private fun setUpcomingHistoryDetail() {
        mViewDataBinding.bottomLayout.visibility = View.GONE
        mViewDataBinding.statusLayout.visibility = View.GONE
        mViewDataBinding.itemLayout.visibility = View.GONE
        mViewDataBinding.llUserName.visibility = View.GONE
        mViewDataBinding.idHistrydetailCommentValTv.visibility = View.GONE

        mViewDataBinding.currentorderdetailTitleTv.text = transpotResponseData.booking_id
        mViewDataBinding.currentorderdetailDateTv.text = (CommanMethods.getLocalTimeStamp(transpotResponseData.assigned_at,
                "Req_Date_Month") + "")
        mViewDataBinding.timeCurrentorderdetailTv.text = (CommanMethods.getLocalTimeStamp(transpotResponseData
                .assigned_at, "Req_time") + "")
        mViewDataBinding.historydetailSrcValueTv.text = transpotResponseData.s_address
        mViewDataBinding.historydetailDestValueTv.text = transpotResponseData.d_address
        mViewDataBinding.historydetailStatusValueTv.text = transpotResponseData.status
        mViewDataBinding.historydetailPaymentmodeValTv.text = transpotResponseData.payment_mode
        mViewDataBinding.vechileTypeTv.text = (transpotResponseData.provider_vehicle.vehicle_model + "("
                + transpotResponseData.provider_vehicle.vehicle_no + ")")


    }

    override fun goBack() {

        finish()
    }

    override fun onClickDispute() {

        if (mViewDataBinding.disputeBtn.text.equals(getString(R.string.dispute_status))) {

            historyDetailViewModel.getDisputeStatus(transpotResponseData.id)

        } else {
            getDisputeList()
        }


    }

    private fun showDisputeStatus(disputeStatusResponseData: DisputeStatusData) {
        val inflate = DataBindingUtil.inflate<DisputeStatusBinding>(LayoutInflater.from(baseContext)
                , R.layout.dispute_status, null, false)
        inflate.disputeTitle.text = (disputeStatusResponseData.dispute_title)
        inflate.disputeComment.text = (disputeStatusResponseData.comments)
        inflate.disputeStatus.text = (disputeStatusResponseData.status)

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(inflate.root)
        dialog.show()
    }

    override fun onClickViewRecepit() {

        showInvoiceAlertDialog()
    }

    private fun showInvoiceAlertDialog() {
        val invoiceDialogView = LayoutInflater.from(this).inflate(R.layout.view_recepit,
                null, false);

        val builder = AlertDialog.Builder(this)
        builder.setView(invoiceDialogView)

        //finally creating the alert dialog and displaying it
        val alertDialog = builder.create()
        alertDialog.show()
        alertDialog.findViewById<ImageView>(R.id.cancel_dialog_img)!!
                .setOnClickListener { alertDialog.dismiss() }

        alertDialog.findViewById<TextView>(R.id.basefare_tv)!!.text = (Constants.currency +
                transpotResponseData.service_type.base_fare)
        alertDialog.findViewById<TextView>(R.id.wallet_tv)!!.text = (Constants.currency +
                transpotResponseData.payment.wallet)
        alertDialog.findViewById<TextView>(R.id.hourlyfare_tv)!!.text = (Constants.currency
                + transpotResponseData.payment.hour)
        alertDialog.findViewById<TextView>(R.id.taxfare_tv)!!.text = (Constants.currency
                + transpotResponseData.payment.tax)
        alertDialog.findViewById<TextView>(R.id.disscount_applied_tv)!!.text = (Constants.currency
                + transpotResponseData.payment.discount)
        alertDialog.findViewById<TextView>(R.id.tips_tv)!!.text = (Constants.currency
                + transpotResponseData.payment.tips)
        alertDialog.findViewById<TextView>(R.id.total_charge_value_tv)!!.text = (Constants.currency
                + transpotResponseData.payment.total)



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
        val inflate = DataBindingUtil.inflate<DisputeResonDialogBinding>(LayoutInflater.from(baseContext)
                , R.layout.dispute_reson_dialog, null, false)
        inflate.disputeReasonListAdapter = DisputeReasonListAdapter(historyDetailViewModel, disputeListData)
        inflate.disputeReasonListAdapter!!.setOnClickListener(mOnAdapterClickListener)
        //disputeReasonListAdapter.setOnClickListener(mOnAdapterClickListener)


        historyDetailViewModel.getSelectedValue().observe(this, Observer {
            mselectedDisputeName = it
        })
        inflate.applyFilter.setOnClickListener {
            loadingObservable.value = true

            createDisputeRequest()
        }

        val dialog = BottomSheetDialog(this)
        dialog.setContentView(inflate.root)
        dialog.show()
    }

    private fun createDisputeRequest() {

        historyDetailViewModel.addDispute(transpotResponseData.id.toString()
                , transpotResponseData.provider_id.toString(), mselectedDisputeName)
    }

    private val mOnAdapterClickListener = object : ReasonListClicklistner {
        override fun reasonOnItemClick(disputeName: String) {
            historyDetailViewModel.setSelectedValue(disputeName)
        }


    }
}


