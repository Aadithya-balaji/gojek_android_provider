package com.gox.partner.views.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseFragment
import com.gox.base.data.PreferencesKey
import com.gox.partner.R
import com.gox.partner.databinding.FilterDialogBinding
import com.gox.partner.databinding.FragmentOrderBinding
import com.gox.partner.interfaces.ServiceTypeListener
import com.gox.partner.models.ConfigResponseData
import com.gox.partner.models.ConfigService
import com.gox.partner.views.adapters.FilterServiceListAdapter
import com.gox.partner.views.currentorder_fragment.CurrentOrderFragment
import com.gox.partner.views.dashboard.DashBoardNavigator
import com.gox.partner.views.dashboard.DashBoardViewModel
import com.gox.partner.views.pastorder_fragment.PastOrderFragment
import kotlinx.android.synthetic.main.activity_dashboard.*

class OrderFragment : BaseFragment<FragmentOrderBinding>(), OrderFragmentNavigator, ServiceTypeListener {

    lateinit var mViewDataBinding: FragmentOrderBinding
    lateinit var filterServiceListName: List<ConfigService>
    lateinit var orderFragmentViewModel: OrderFragmentViewModel
    lateinit var dashboardViewModel: DashBoardViewModel
    private lateinit var dashBoardNavigator: DashBoardNavigator
    private var selectedService: String? = ""
    private var selectedServiceTypeID: Int? = -1

    override fun getLayoutId(): Int = R.layout.fragment_order

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        orderFragmentViewModel = OrderFragmentViewModel()
        dashboardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        this.mViewDataBinding = mViewDataBinding as FragmentOrderBinding
        this.mViewDataBinding.lifecycleOwner = this
        orderFragmentViewModel.navigator = this
        this.mViewDataBinding.orderfragmentviewmodel = orderFragmentViewModel
        activity?.supportFragmentManager?.beginTransaction()?.add(R.id.container_order, PastOrderFragment())?.commit()
        val baseApiResponseString: String = BaseApplication.getCustomPreference!!.getString(PreferencesKey.BASE_CONFIG_RESPONSE, "")!!
        if (baseApiResponseString.isNotEmpty()) {
            val baseApiResponseData: ConfigResponseData = Gson().fromJson<ConfigResponseData>(baseApiResponseString
                    , ConfigResponseData::class.java)
            selectedServiceTypeID = baseApiResponseData.services.get(0).id
            filterServiceListName = baseApiResponseData.services
        }
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

    override fun opeFilterlayout() {
        if (!filterServiceListName.isNullOrEmpty()) {
            val view = DataBindingUtil.inflate<FilterDialogBinding>(LayoutInflater.from(context),
                    R.layout.filter_dialog, null, false)
            val filterServiceAdapter = FilterServiceListAdapter(dashboardViewModel, filterServiceListName,
                    this, selectedServiceTypeID!!)
            view.filterServiceListAdapter = filterServiceAdapter
            val dialog = BottomSheetDialog(activity!!)
            dialog.setContentView(view.root)
            dialog.show()
            view.applyFilter.setOnClickListener {
                mViewDataBinding.serviceNameToolbarTv.text = dashboardViewModel.selectedFilterService.value
                dashboardViewModel.selectedFilterService.value = selectedService
                dialog.dismiss()
            }
        }
    }

    override fun getServiceType(serviceType: String, serviceTypeID: Int) {
        selectedService = serviceType
        selectedServiceTypeID = serviceTypeID
    }
}

