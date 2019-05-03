package com.xjek.provider.views.home

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.xjek.base.base.BaseFragment
import com.xjek.base.data.Constants.DEFAULT_ZOOM
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.readPreferences
import com.xjek.base.extensions.writePreferences
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentHomePageBinding
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.dashboard.DashBoardNavigator
import com.xjek.provider.views.dashboard.DashBoardViewModel
import com.xjek.provider.views.incoming_request_taxi.IncomingRequestDialog
import com.xjek.provider.views.pendinglist.PendingListDialog

class HomeFragment : BaseFragment<FragmentHomePageBinding>(),
        HomeNavigator,
        OnMapReadyCallback {

    private lateinit var mHomeDataBinding: FragmentHomePageBinding
    private lateinit var dashBoardNavigator: DashBoardNavigator
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mDashboardViewModel: DashBoardViewModel

    private lateinit var incomingRequestDialogDialog: IncomingRequestDialog

    private var mGoogleMap: GoogleMap? = null

    private var isServiceNeed: Int? = -1
    private var isDocumentNeed: Int? = -1
    private var isBankDetailNeed: Int? = -1
    private var isOnline: Boolean? = true
    private var pendingListDialog: PendingListDialog? = null

    override fun getLayoutId(): Int = R.layout.fragment_home_page

    companion object {
        var loadingProgress: MutableLiveData<Boolean>? = null
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mHomeDataBinding = mViewDataBinding as FragmentHomePageBinding
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mDashboardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        mViewModel.navigator = this
        mHomeDataBinding.homemodel = mViewModel
        mHomeDataBinding.btnChangeStatus.bringToFront()

        initializeMap()

        val activity: DashBoardActivity = activity as DashBoardActivity
        loadingProgress = activity.loadingObservable as MutableLiveData<Boolean>
        mViewModel.showLoading = loadingProgress as MutableLiveData<Boolean>
        pendingListDialog = PendingListDialog()
        incomingRequestDialogDialog = IncomingRequestDialog()
        incomingRequestDialogDialog.isCancelable = false

        if (readPreferences<Int>(PreferencesKey.IS_ONLINE) == 1) {
            mHomeDataBinding.llOffline.visibility = View.GONE
            fragmentMap.view!!.visibility = View.VISIBLE
        } else {
            mHomeDataBinding.llOffline.visibility = View.VISIBLE
            fragmentMap.view!!.visibility = View.GONE
        }

        if (readPreferences<Int>(PreferencesKey.IS_ONLINE) == 1) {
            mHomeDataBinding.llOffline.visibility = View.GONE
            fragmentMap.view!!.visibility = View.VISIBLE
        } else {
            mHomeDataBinding.llOffline.visibility = View.VISIBLE
            fragmentMap.view!!.visibility = View.GONE
        }

        getApiResponse()
    }

    private fun getApiResponse() {
        println("RRR :: HomeFragment.getApiResponse")
        observeLiveData(mDashboardViewModel.checkRequestLiveData) { checkStatusData ->
            if (checkStatusData.statusCode == "200") {
                val providerDetailsModel = checkStatusData.responseData.provider_details

                isDocumentNeed = providerDetailsModel.is_document
                isServiceNeed = providerDetailsModel.is_service
                isBankDetailNeed = providerDetailsModel.is_bankdetail

                if (providerDetailsModel.is_online == 1) {
                    isOnline = true
                    writePreferences(PreferencesKey.IS_ONLINE, 1)
                    changeView(true)
                } else {
                    isOnline = false
                    writePreferences(PreferencesKey.IS_ONLINE, 0)
                    dashBoardNavigator.updateLocation(false)
                    changeView(false)
                }

                if (isDocumentNeed == 0 || isServiceNeed == 0 || isBankDetailNeed == 0) {
                    if (pendingListDialog != null && !pendingListDialog!!.isShown()) showPendingListDialog(1)
                } else if (providerDetailsModel.status != "APPROVED") {
                    if (pendingListDialog != null && !pendingListDialog!!.isShown()) showPendingListDialog(2)
                } else if (providerDetailsModel.wallet_balance < 1) {
//                    Not implemented cos pending in backend
//                    if (pendingListDialog != null && !pendingListDialog!!.isShown()) showPendingListDialog(3)
                }
            }
        }

        try {
            observeLiveData(mViewModel.onlineStatusLiveData) {
                loadingObservable.value = false
                if (mViewModel.onlineStatusLiveData.value != null) {
                    val isOnline = mViewModel.onlineStatusLiveData.value!!.responseData?.providerStatus
                    if (isOnline.equals("1")) {
                        dashBoardNavigator.updateLocation(true)
                        changeView(true)
                    } else {
                        dashBoardNavigator.updateLocation(false)
                        changeView(false)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initializeMap() {
        fragmentMap = childFragmentManager.findFragmentById(R.id.map_home) as SupportMapFragment
        fragmentMap.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        try {
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
            mGoogleMap!!.uiSettings.isCompassEnabled = true

            val latLng = LatLng(mDashboardViewModel.latitude.value!!, mDashboardViewModel.longitude.value!!)
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, com.xjek.taxiservice.R.raw.style_json))
            mGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))

        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
        dashBoardNavigator.hideRightIcon(true)
    }

    override fun changeStatus(view: View) {
        when (view.id) {
            R.id.btn_change_status -> {
                if (mHomeDataBinding.btnChangeStatus.text.toString() == activity!!.resources.getString(R.string.offline)) {
                    loadingObservable.value = true
                    mViewModel.changeOnlineStatus("0")
                } else {
                    loadingObservable.value = true
                    mViewModel.changeOnlineStatus("1")
                }
            }
        }
    }

    override fun showErrorMessage(error: String) {
        loadingProgress!!.value = false
    }

    private fun showPendingListDialog(type: Int) {
        val pendingListDialog = PendingListDialog()
        val bundle = Bundle()
        bundle.putInt("ISDOCUMENTNEED", isDocumentNeed!!)
        bundle.putInt("ISSERVICENEED", isServiceNeed!!)
        bundle.putInt("ISBANCKDETAILNEED", isBankDetailNeed!!)
        bundle.putInt("TYPE", type)
        pendingListDialog.arguments = bundle
        pendingListDialog.show(activity!!.supportFragmentManager, "pendinglist")
        pendingListDialog.isCancelable = true
    }

    private fun changeView(isOnline: Boolean) {
        if (!isOnline) {
            mHomeDataBinding.llOffline.visibility = View.VISIBLE
            fragmentMap.view!!.visibility = View.GONE
            mHomeDataBinding.btnChangeStatus.text = activity!!.resources.getString(R.string.online)
        } else {
            mHomeDataBinding.llOffline.visibility = View.GONE
            fragmentMap.view!!.visibility = View.VISIBLE
            mHomeDataBinding.btnChangeStatus.text = activity!!.resources.getString(R.string.offline)
        }
    }
}