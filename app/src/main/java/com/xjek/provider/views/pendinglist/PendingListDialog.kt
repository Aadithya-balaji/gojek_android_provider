package com.xjek.provider.views.pendinglist

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseDialogFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.PendingListDialogBinding
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.document.DocumentActivity
import com.xjek.provider.views.manage_services.ManageServicesActivity

class PendingListDialog : BaseDialogFragment<PendingListDialogBinding>(), PendingListNavigator {


    private lateinit var pendingListDialogBinding: PendingListDialogBinding
    private lateinit var pendginListViewModel: PendingListViewModel
    private var dashBoardActivity: DashBoardActivity? = null
    private var isDocumentNeed: Int? = 0
    private var isServiceNeed: Int? = 0
    private var isBankDetailNeed: Int? = 0


    override fun getLayout(): Int {
        return R.layout.pending_list_dialog
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                .WRAP_CONTENT);
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardActivity = if (context is AppCompatActivity) context as DashBoardActivity else null

    }

    override fun initView(viewDataBinding: ViewDataBinding?) {
        pendingListDialogBinding = viewDataBinding as PendingListDialogBinding
        pendginListViewModel = PendingListViewModel()
        pendginListViewModel.navigator = this
        pendingListDialogBinding.pendinglistModel = pendginListViewModel
        getBundleArugment()
    }

    fun getBundleArugment() {
        isDocumentNeed = if (arguments != null && arguments!!.containsKey("ISDOCUMENTNEED")) arguments!!.getInt("ISDOCUMENTNEED") else 0
        isServiceNeed = if (arguments != null && arguments!!.containsKey("ISSERVICENEED")) arguments!!.getInt("ISSERVICENEED") else 0
        isBankDetailNeed = if (arguments != null && arguments!!.containsKey("ISBANCKDETAILNEED")) arguments!!.getInt("ISBANCKDETAILNEED") else 0

        if (isDocumentNeed == 1 && isServiceNeed == 0 && isBankDetailNeed == 0) {
            pendingListDialogBinding.tvAddDocument.visibility=View.VISIBLE
            pendingListDialogBinding.tvAddService.visibility=View.GONE
            pendingListDialogBinding.tvBankDetails.visibility=View.GONE

        } else if (isDocumentNeed == 0 && isServiceNeed == 1 && isBankDetailNeed == 0) {
            pendingListDialogBinding.tvAddDocument.visibility=View.GONE
            pendingListDialogBinding.tvAddService.visibility=View.VISIBLE
            pendingListDialogBinding.tvBankDetails.visibility=View.GONE

        } else if (isDocumentNeed == 0 && isServiceNeed == 0 && isBankDetailNeed == 1) {
            pendingListDialogBinding.tvAddDocument.visibility=View.GONE
            pendingListDialogBinding.tvAddService.visibility=View.GONE
            pendingListDialogBinding.tvBankDetails.visibility=View.VISIBLE

        } else if (isDocumentNeed == 1 && isServiceNeed == 1 && isBankDetailNeed == 0) {
            pendingListDialogBinding.tvAddDocument.visibility=View.GONE
            pendingListDialogBinding.tvAddService.visibility=View.GONE
            pendingListDialogBinding.tvBankDetails.visibility=View.VISIBLE

        }
    }

    override fun pickItem(view: View) {

        when (view.id) {
            R.id.tv_add_document -> {
                val intent = Intent(dashBoardActivity, DocumentActivity::class.java)
                startActivity(intent)
            }

            R.id.tv_bank_details -> {

            }

            R.id.tv_add_service -> {
                val intent = Intent(dashBoardActivity, ManageServicesActivity::class.java)
                startActivity(intent)
            }
        }
    }
}