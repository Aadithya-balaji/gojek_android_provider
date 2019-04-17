package com.xjek.provider.views.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.xjek.base.base.BaseFragment
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.observeLiveData
import com.xjek.base.extensions.readPreferences
import com.xjek.base.extensions.writePreferences
import com.xjek.base.utils.LocationCallBack
import com.xjek.base.utils.LocationUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentHomePageBinding
import com.xjek.provider.utils.location_service.LocationUpdatesService.BROADCAST
import com.xjek.provider.utils.location_service.LocationUpdatesService.EXTRA_LOCATION
import com.xjek.provider.views.dashboard.DashBoardActivity
import com.xjek.provider.views.dashboard.DashBoardNavigator
import com.xjek.provider.views.pendinglist.PendingListDialog
import com.xjek.taxiservice.views.main.ActivityTaxiMain
import permissions.dispatcher.NeedsPermission

class HomeFragment : BaseFragment<FragmentHomePageBinding>(),
        Home_Navigator,
        OnMapReadyCallback,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener,
        LocationSource.OnLocationChangedListener {

    private lateinit var mHomeDataBinding: FragmentHomePageBinding

    override fun getLayoutId(): Int = R.layout.fragment_home_page
    private lateinit var dashBoardNavigator: DashBoardNavigator
    private lateinit var fragmentMap: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private var currentLat: Double = 13.0561789
    private var currentLong: Double = 80.247998
    private var isDocumentNeed: Int? = -1
    private var isServiceNeed: Int? = -1
    private var isBankdetailNeed: Int? = -1
    private var isOnline: Boolean? = true
    private lateinit var mHomeViewModel: HomeViewModel

    companion object {
        var loadingProgress: MutableLiveData<Boolean>? = null
    }

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mHomeDataBinding = mViewDataBinding as FragmentHomePageBinding
        mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java);
        mHomeViewModel.navigator = this
        mHomeDataBinding.homemodel = mHomeViewModel
        mHomeDataBinding.btnChangeStatus.bringToFront()

        updateCurrentLocation()
        initalizeMap()

        mHomeViewModel.latitude.value = currentLat
        mHomeViewModel.longitude.value = currentLong
        callCheckRequestApi()
        val activity: DashBoardActivity = activity as DashBoardActivity
        loadingProgress = activity.loadingObservable as MutableLiveData<Boolean>
        mHomeViewModel.showLoading = loadingProgress as MutableLiveData<Boolean>

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

    fun callCheckRequestApi() {
        mHomeViewModel.getRequest()
    }

    private fun getApiResponse() {
        observeLiveData(mHomeViewModel.checkRequestLiveData) {
            if (mHomeViewModel.checkRequestLiveData.value!!.getStatusCode().equals("200")) {
                val providerDetailsModel =
                        mHomeViewModel.checkRequestLiveData.value!!.getResponseData()!!.getProviderDetails()

                if (providerDetailsModel != null) {
                    isDocumentNeed = providerDetailsModel.getIsDocument()
                    isServiceNeed = providerDetailsModel.getIsService()
                    isBankdetailNeed = providerDetailsModel.getIsBankdetail()
                    if (providerDetailsModel.getIsOnline() == 1) {
                        isOnline = true
                        writePreferences(PreferencesKey.IS_ONLINE, 1)
                        changeView(true)

                    } else {
                        isOnline = false
                        writePreferences(PreferencesKey.IS_ONLINE, 0)
                        changeView(false)
                    }

                    if (isDocumentNeed == 0 || isServiceNeed == 0 || isBankdetailNeed == 0) {
                        showPendingListDialog(1)

                    }else if(providerDetailsModel.getStatus().equals("APPROVED")){
                        showPendingListDialog(2)

                    }else if(providerDetailsModel.getWalletBalance()!! < 1){

                        //for now  in api side not implmented the low balance
                       // showPendingListDialog(3)
                    }


                    //      By Rajaganapathi :: Just for development purpose...

//                    val builder = AlertDialog.Builder(context!!)
//                    builder.setTitle("Incoming request dialog")
//                    builder.setMessage(Gson().toJson(providerDetailsModel))
//                    builder.setPositiveButton("Accept") { dialog, which ->
//                        Toast.makeText(context, "Accept", Toast.LENGTH_SHORT).show()
//                    }
//
//                    builder.setNegativeButton("Reject") { dialog, which ->
//                        Toast.makeText(context, "Reject", Toast.LENGTH_SHORT).show()
//                    }
//
//                    val dialog: AlertDialog = builder.create()
//                    dialog.show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(context!!).registerReceiver(mBroadcastReceiver, IntentFilter(BROADCAST))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(context!!).unregisterReceiver(mBroadcastReceiver)
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private fun updateCurrentLocation() {
        LocationUtils.getLastKnownLocation(activity!!.applicationContext, object : LocationCallBack.LastKnownLocation {
            override fun onSuccess(location: Location?) {
                currentLat = location!!.latitude
                currentLong = location.longitude
            }

            override fun onFailure(messsage: String?) {
                currentLat = 13.0561789
                currentLong = 80.247998
            }
        })
    }

    private fun initalizeMap() {
        fragmentMap = childFragmentManager.findFragmentById(R.id.map_home) as SupportMapFragment
        fragmentMap.getMapAsync(this@HomeFragment)
    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        try {
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, com.xjek.taxiservice.R.raw.style_json))
            val latlong = LatLng(currentLat, currentLong)
            val location = CameraUpdateFactory.newLatLngZoom(latlong, 15f)
            mGoogleMap!!.animateCamera(location)
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
    }

    override fun onLocationChanged(currentLocation: Location?) {
    }

    override fun onCameraMove() {
    }

    override fun onCameraIdle() {
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
        dashBoardNavigator.hideRightIcon(true)
    }

    override fun gotoTaxiModule() {
        startActivity(Intent(activity!!, ActivityTaxiMain::class.java))
    }

    override fun gotoFoodieModule() {
    }

    override fun gotoXuberModule() {
    }

    override fun changeStatus(view: View) {
        when (view.id) {
            R.id.btn_change_status -> {
                if (mHomeDataBinding.btnChangeStatus.text.toString() == activity!!.resources.getString(R.string.offline)) changeView(false)
                else changeView(true)

            }
        }
    }

    override fun showErrormessage(error: String) {
        loadingProgress!!.value = false
    }

    fun showPendingListDialog(type:Int) {
        val pendingListDialog = PendingListDialog(type)
        val bundle = Bundle()
        bundle.putInt("ISDOCUMENTNEED", isDocumentNeed!!)
        bundle.putInt("ISSERVICENEED", isServiceNeed!!)
        bundle.putInt("ISBANCKDETAILNEED", isBankdetailNeed!!)
        pendingListDialog.arguments = bundle
        pendingListDialog.show(activity!!.supportFragmentManager, "pendinglist")
        pendingListDialog.isCancelable = false
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

                callCheckRequestApi()
            }
        }
    }
}
