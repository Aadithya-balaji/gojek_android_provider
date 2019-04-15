package com.xjek.provider.views.pendinglist

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.xjek.base.base.BaseDialogFragment
import com.xjek.provider.R
import com.xjek.provider.databinding.ActivityDashboardBinding
import com.xjek.provider.databinding.PendingListDialogBinding
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.document.DocumentActivity
import com.xjek.provider.views.manage_services.ManageServicesActivity

class PendingListDialog : BaseDialogFragment<PendingListDialogBinding>(), PendingListNavigator {


    private lateinit var pendingListDialogBinding: PendingListDialogBinding
    private lateinit var pendginListViewModel: PendingListViewModel
    private  var  dashBoardActivity: DashBoardActivity?=null

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
        dashBoardActivity = if (context is AppCompatActivity) context as DashBoardActivity  else null

    }

    override fun initView(viewDataBinding: ViewDataBinding?) {
        pendingListDialogBinding = viewDataBinding as PendingListDialogBinding
        pendginListViewModel = PendingListViewModel()
        pendginListViewModel.navigator
    }

    override fun pickItem(view: View) {

        when (view.id) {
            R.id.tv_add_document -> {
                val intent =Intent(dashBoardActivity,DocumentActivity::class.java)
                startActivity(intent)
            }

            R.id.tv_bank_details -> {
                val intent =Intent(dashBoardActivity,ManageServicesActivity::class.java)
                startActivity(intent)
            }

            R.id.tv_add_service -> {

            }
        }
    }
}