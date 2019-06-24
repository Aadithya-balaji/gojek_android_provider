package com.gox.partner.views.pendinglist

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.gox.base.base.BaseDialogFragment
import com.gox.base.data.Constants
import com.gox.base.extensions.observeLiveData
import com.gox.partner.R
import com.gox.partner.databinding.PendingListDialogBinding
import com.gox.partner.views.dashboard.DashBoardActivity
import com.gox.partner.views.manage_bank_details.ManageBankDetailsActivity
import com.gox.partner.views.manage_documents.ManageDocumentsActivity
import com.gox.partner.views.manage_payment.ManagePaymentActivity
import com.gox.partner.views.manage_services.ManageServicesActivity

class PendingListDialog : BaseDialogFragment<PendingListDialogBinding>(), PendingListNavigator {

    private lateinit var mBinding: PendingListDialogBinding
    private lateinit var mViewModel: PendingListViewModel

    private var dashBoardActivity: DashBoardActivity? = null
    private var isDocumentNeed: Int? = 0
    private var isServiceNeed: Int? = 0
    private var isBankDetailNeed: Int? = 0
    private var dialogType: String? = ""
    private var shown: Boolean? = false

    override fun getLayout() = R.layout.pending_list_dialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardActivity = if (context is AppCompatActivity) context as DashBoardActivity else null
    }

    override fun initView(viewDataBinding: ViewDataBinding, view: View) {
        mBinding = viewDataBinding as PendingListDialogBinding
        mViewModel = ViewModelProviders.of(this).get(PendingListViewModel::class.java)
        mViewModel.navigator = this
        mBinding.pendinglistModel = mViewModel

        observeLiveData(mViewModel.getVerificationObservable()) {
            dialogType = it.getDialogType()
            isServiceNeed = it.isService
            isDocumentNeed = it.isDocument
            isBankDetailNeed = it.isBankDetail
            updateView()
        }

    }

    private fun updateView() {
        when (dialogType) {
            Constants.ProviderStatus.PENDING -> {
                mBinding.llDocPending.visibility = View.VISIBLE
                mBinding.llLowBalance.visibility = View.GONE
                mBinding.llWaiting.visibility = View.GONE
            }

            Constants.ProviderStatus.WAITING -> {
                mBinding.llDocPending.visibility = View.GONE
                mBinding.llLowBalance.visibility = View.GONE
                mBinding.llWaiting.visibility = View.VISIBLE
            }

            Constants.ProviderStatus.LOW_BALANCE -> {
                mBinding.llDocPending.visibility = View.GONE
                mBinding.llLowBalance.visibility = View.VISIBLE
                mBinding.llWaiting.visibility = View.GONE
            }

            else -> dismiss()
        }


        mBinding.tvAddService.visibility = (if (isServiceNeed == 0) View.VISIBLE else View.GONE)
        mBinding.tvAddDocument.visibility = (if (isDocumentNeed == 0) View.VISIBLE else View.GONE)
        mBinding.tvBankDetails.visibility = (if (isBankDetailNeed == 0) View.VISIBLE else View.GONE)
    }

    fun isShown() = shown!!

    override fun pickItem(view: View) {
        when (view.id) {
            R.id.tv_add_document -> startActivity(Intent(dashBoardActivity, ManageDocumentsActivity::class.java))

            R.id.tv_bank_details -> startActivity(Intent(dashBoardActivity, ManageBankDetailsActivity::class.java))

            R.id.tv_add_service -> startActivity(Intent(dashBoardActivity, ManageServicesActivity::class.java))

            R.id.btn_call_admin -> startActivity(Intent(dashBoardActivity, ManagePaymentActivity::class.java))
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            if (shown == false) {
                this.shown = true
                super.show(manager, tag)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (shown == false) {
            this.shown = true
            return super.show(transaction, tag)
        }
        return -1
    }

    override fun dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss()
        this.shown = false
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        this.shown = false
    }
}