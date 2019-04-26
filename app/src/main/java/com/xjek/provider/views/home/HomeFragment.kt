package com.xjek.provider.views.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.location.Location
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
import com.google.gson.Gson
import com.xjek.base.base.BaseFragment
import com.xjek.base.data.Constants.RideStatus.SEARCHING
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.readPreferences
import com.xjek.base.extensions.writePreferences
import com.xjek.base.utils.LocationCallBack
import com.xjek.base.utils.LocationUtils
import com.xjek.provider.BuildConfig
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentHomePageBinding
import com.xjek.provider.utils.location_service.BaseLocationService.EXTRA_LOCATION
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.dashboard.DashBoardNavigator
import com.xjek.provider.views.incoming_request_taxi.IncomingRequest
import com.xjek.provider.views.pendinglist.PendingListDialog
import com.xjek.taxiservice.views.main.TaxiDashboardActivity
import permissions.dispatcher.NeedsPermission
import java.util.*

class HomeFragment : BaseFragment<FragmentHomePageBinding>(),
        HomeNavigator,
        OnMapReadyCallback {

    private lateinit var mHomeDataBinding: FragmentHomePageBinding
    private lateinit var dashBoardNavigator: DashBoardNavigator
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mHomeViewModel: HomeViewModel
    private lateinit var incomingRequestDialog: IncomingRequest

    private var mGoogleMap: GoogleMap? = null
    private var currentLat: Double = 13.0561789
    private var currentLong: Double = 80.247998
    private var isServiceNeed: Int? = -1
    private var isDocumentNeed: Int? = -1
    private var isBankDetailNeed: Int? = -1
    private var isOnline: Boolean? = true
    private var pendingListDialog: PendingListDialog? = null

    private val checkRequestTimer = Timer()

    override fun getLayoutId(): Int = R.layout.fragment_home_page

    companion object {
        var loadingProgress: MutableLiveData<Boolean>? = null
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mHomeDataBinding = mViewDataBinding as FragmentHomePageBinding
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mHomeViewModel.navigator = this
        mHomeDataBinding.homemodel = mHomeViewModel
        mHomeDataBinding.btnChangeStatus.bringToFront()

        updateCurrentLocation()
        initializeMap()

        mHomeViewModel.latitude.value = currentLat
        mHomeViewModel.longitude.value = currentLong
        val activity: DashBoardActivity = activity as DashBoardActivity
        loadingProgress = activity.loadingObservable as MutableLiveData<Boolean>
        mHomeViewModel.showLoading = loadingProgress as MutableLiveData<Boolean>
        pendingListDialog = PendingListDialog()
        incomingRequestDialog = IncomingRequest()


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

        checkRequestTimer.schedule(object : TimerTask() {
            override fun run() = updateCurrentLocation()
        }, 0, 8000)

        if (BuildConfig.DEBUG) startActivity(Intent(activity, TaxiDashboardActivity::class.java))
    }

    private fun getApiResponse() {
        println("RRR :: HomeFragment.getApiResponse")
        observeLiveData(mHomeViewModel.checkRequestLiveData) { checkStatusData ->

            if (checkStatusData.statusCode.equals("200")) {
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
                    dashBoardNavigator.isNeedLocationUpdate(false)
                    changeView(false)
                }

                if (isDocumentNeed == 0 || isServiceNeed == 0 || isBankDetailNeed == 0) {
                    if (pendingListDialog != null && !pendingListDialog!!.isShown())
                        showPendingListDialog(1)
                } else if (!providerDetailsModel.status.equals("APPROVED")) {
                    if (pendingListDialog != null && !pendingListDialog!!.isShown())
                        showPendingListDialog(2)
                } else if (providerDetailsModel.wallet_balance < 1) {
                    //for now  in api side not implmented the low balance
                    //if (pendingListDialog != null && !pendingListDialog!!.isShown())
                    //showPendingListDialog(3)
                }

                if (checkStatusData.responseData.requests.isNotEmpty())
                    when (checkStatusData.responseData.requests[0].request.status) {
                        SEARCHING -> if (!incomingRequestDialog.isShown()) {
                            val bundle = Bundle()
                            val strRequest = Gson().toJson(checkStatusData)
                            bundle.putString("requestModel", strRequest)
                            incomingRequestDialog.arguments = bundle
                            incomingRequestDialog.show(activity!!.supportFragmentManager, "incomingRequest")
                        }
                        else -> when (checkStatusData.responseData.requests[0].service.admin_service_name) {
                            "TRANSPORT" -> gotoTaxiModule()
                            "SERVICE" -> gotoXuberModule()
                            "ORDER" -> gotoFoodieModule()
                        }
                    }
            }
        }

        try {//     GetOnlineStatus
            observeLiveData(mHomeViewModel.onlineStatusLiveData) {
                loadingObservable.value = false
                if (mHomeViewModel.onlineStatusLiveData.value != null) {
                    val isOnline = mHomeViewModel.onlineStatusLiveData.value!!.responseData?.providerStatus
                    if (isOnline.equals("1")) {
                        dashBoardNavigator.isNeedLocationUpdate(true)
                        changeView(true)
                    } else {
                        dashBoardNavigator.isNeedLocationUpdate(false)
                        changeView(false)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private fun updateCurrentLocation() {
        try {
            println("RRR :: HomeFragment.updateCurrentLocation")
            LocationUtils.getLastKnownLocation(activity!!.applicationContext, object : LocationCallBack.LastKnownLocation {
                override fun onSuccess(location: Location?) {
                    currentLat = location!!.latitude
                    currentLong = location.longitude
                    mHomeViewModel.getRequest()
                }

                override fun onFailure(messsage: String?) {
                    currentLat = 13.0561789
                    currentLong = 80.247998
                    mHomeViewModel.getRequest()
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initializeMap() {
        fragmentMap = childFragmentManager.findFragmentById(R.id.map_home) as SupportMapFragment
        fragmentMap.getMapAsync(this@HomeFragment)
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        try {
            mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = true
            mGoogleMap!!.uiSettings.isCompassEnabled = true

            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, com.xjek.taxiservice.R.raw.style_json))
            val latlng = LatLng(currentLat, currentLong)
            val location = CameraUpdateFactory.newLatLngZoom(latlng, 15f)
            mGoogleMap!!.animateCamera(location)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
        dashBoardNavigator.hideRightIcon(true)
    }

    override fun gotoTaxiModule() {
        startActivity(Intent(activity!!, TaxiDashboardActivity::class.java))
        dashBoardNavigator.isNeedLocationUpdate(false)
    }

    override fun gotoFoodieModule() {
    }

    override fun gotoXuberModule() {
    }

    override fun changeStatus(view: View) {
        when (view.id) {
            R.id.btn_change_status -> {
                if (mHomeDataBinding.btnChangeStatus.text.toString() == activity!!.resources.getString(R.string.offline)) {
                    loadingObservable.value = true
                    mHomeViewModel.changeOnlineStatus("0")
                } else {
                    loadingObservable.value = true
                    mHomeViewModel.changeOnlineStatus("1")
                }
            }
        }
    }

    override fun showErrormessage(error: String) {
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

    private val mBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {
            println("RRR MyReceiver.onReceive")
            val location = intent!!.getParcelableExtra<Location>(EXTRA_LOCATION)
            if (location != null) {
                currentLat = location.latitude
                currentLong = location.longitude

                mHomeViewModel.latitude.value = currentLat
                mHomeViewModel.longitude.value = currentLong

                mHomeViewModel.getRequest()
            }
        }
    }
}
