package com.xjek.provider.views.order

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.xjek.base.base.BaseFragment
import com.xjek.base.data.PreferencesHelper
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.provider.R
import com.xjek.provider.databinding.FilterDialogBinding
import com.xjek.provider.databinding.FragmentOrderBinding
import com.xjek.provider.interfaces.ServiceTypeListener
import com.xjek.provider.models.ConfigResponseModel
import com.xjek.provider.views.adapters.FilterServiceListAdapter
import com.xjek.provider.views.currentorder_fragment.CurrentOrderFragment
import com.xjek.provider.views.dashboard.DashBoardNavigator
import com.xjek.provider.views.dashboard.DashBoardViewModel
import com.xjek.provider.views.pastorder_fragment.PastOrderFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class OrderFragment : BaseFragment<FragmentOrderBinding>(), OrderFragmentNavigator ,ServiceTypeListener{

    lateinit var mViewDataBinding: FragmentOrderBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<View>
    val preference = PreferencesHelper
    lateinit var filterServiceListName: List<ConfigResponseModel.ResponseData.Service>
    lateinit var orderFragmentViewModel: OrderFragmentViewModel
    lateinit var dashboardViewModel: DashBoardViewModel
    private lateinit var dashBoardNavigator: DashBoardNavigator
    private  var selectedService:String?=""
    private  var selectedServiceTypeID:Int?=-1


    override fun getLayoutId(): Int = R.layout.fragment_order
    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        orderFragmentViewModel = OrderFragmentViewModel()
        dashboardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        this.mViewDataBinding = mViewDataBinding as FragmentOrderBinding
        this.mViewDataBinding.setLifecycleOwner(this)
        orderFragmentViewModel.navigator = this
        this.mViewDataBinding.orderfragmentviewmodel = orderFragmentViewModel
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.container_order, PastOrderFragment())?.commit()
        val baseApiResponseString: String = readPreferences(PreferencesKey.BASE_CONFIG_RESPONSE, "")!! as String
        val baseApiResponsedata: ConfigResponseModel.ResponseData = Gson().fromJson<ConfigResponseModel.ResponseData>(baseApiResponseString
                , ConfigResponseModel.ResponseData::class.java)
        selectedServiceTypeID=baseApiResponsedata.services.get(0).id
        filterServiceListName = baseApiResponsedata.services

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
        dashBoardNavigator.hideRightIcon(true)
        dashBoardNavigator.showLogo(true)
        dashBoardNavigator.getInstance().tbr_home.visibility = View.GONE
    }

    override fun goToCurrentOrder() {
        mViewDataBinding.pastorderBtn.background = context?.let {
            ContextCompat.getDrawable(it
                    , R.drawable.custom_roundcorner_unselectedorder)
        }

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container_order, CurrentOrderFragment())?.commit()

    }

    override fun goToPastOrder() {

        mViewDataBinding.pastorderBtn.background = context?.let {
            ContextCompat.getDrawable(it
                    , R.drawable.custom_roundcorner_selectedorder)
        }

        activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container_order, PastOrderFragment())?.commit()

    }

    override fun goToUpcomingOrder() {

    }

    override fun onResume() {
        super.onResume()
    }

    override fun opeFilterlayout() {
        val view = DataBindingUtil.inflate<FilterDialogBinding>(LayoutInflater.from(context), R.layout.filter_dialog, null, false)
        val filterServiceAdapter= FilterServiceListAdapter(dashboardViewModel, filterServiceListName,this,selectedServiceTypeID!!)
        view.filterServiceListAdapter=filterServiceAdapter
        val dialog = BottomSheetDialog(activity!!)
        dialog.setContentView(view.root)
        dialog.show()
        view.applyFilter.setOnClickListener(View.OnClickListener {
            mViewDataBinding.serviceNameToolbarTv.setText(dashboardViewModel.selectedFilterService.value)
            dashboardViewModel.selectedFilterService.value=selectedService
            dialog.dismiss()
        })

    }


    override fun getServiceType(serviceType: String,serviceTypeID:Int) {
        selectedService=serviceType
        selectedServiceTypeID=serviceTypeID
    }
}

