package com.gox.partner.views.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.gox.base.base.BaseApplication
import com.gox.base.base.BaseFragment
import com.gox.base.data.Constants
import com.gox.base.data.Constants.DEFAULT_ZOOM
import com.gox.base.data.PreferencesHelper
import com.gox.base.data.PreferencesKey
import com.gox.base.data.PreferencesKey.IS_ONLINE
import com.gox.base.data.PreferencesKey.PROVIDER_ID
import com.gox.base.extensions.observeLiveData
import com.gox.base.extensions.readPreferences
import com.gox.base.extensions.writePreferences
import com.gox.base.persistence.AppDatabase
import com.gox.base.socket.SocketManager
import com.gox.base.utils.LocationCallBack
import com.gox.base.utils.LocationUtils
import com.gox.base.utils.ViewUtils
import com.gox.partner.R
import com.gox.partner.databinding.FragmentHomePageBinding
import com.gox.partner.utils.Constant
import com.gox.partner.views.dashboard.DashBoardNavigator
import com.gox.partner.views.dashboard.DashBoardViewModel
import com.gox.partner.views.pendinglist.PendingListDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_home_page.*

class HomeFragment : BaseFragment<FragmentHomePageBinding>(),
        HomeNavigator,
        OnMapReadyCallback {

    private lateinit var mBinding: FragmentHomePageBinding
    private lateinit var mViewModel: HomeViewModel
    private lateinit var dashBoardNavigator: DashBoardNavigator
    private lateinit var fragmentMap: SupportMapFragment
    private lateinit var mDashboardViewModel: DashBoardViewModel
    private var mGoogleMap: GoogleMap? = null
    private var providerMarker: Marker? = null
    private var isOnline: Boolean? = true
    private var pendingListDialog: PendingListDialog? = null

    override fun getLayoutId() = R.layout.fragment_home_page

    @SuppressLint("MissingPermission")
    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mBinding = mViewDataBinding as FragmentHomePageBinding
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        mDashboardViewModel = ViewModelProviders.of(activity!!).get(DashBoardViewModel::class.java)
        mViewModel.navigator = this
        mBinding.homemodel = mViewModel
        mBinding.btnChangeStatus.bringToFront()
        MapsInitializer.initialize(activity!!)
        initializeMap()
        observeLiveData(mViewModel.showLoading) { loadingObservable.value = it }
        pendingListDialog = PendingListDialog()

        getApiResponse()

        clearDatabase.setOnClickListener {
            AppDatabase.getAppDataBase(context!!)!!.locationPointsDao().deleteAllPoint()
        }
    }

    private fun updateOnlineStatus() {
        if (readPreferences<Int>(IS_ONLINE) == 1) {
            mBinding.llOffline.visibility = View.GONE
            rlOnline.visibility = View.VISIBLE
        } else {
            mBinding.llOffline.visibility = View.VISIBLE
            rlOnline.visibility = View.GONE
        }
    }

    private fun getApiResponse() {
        println("RRR :: HomeFragment.getApiResponse")
        observeLiveData(mDashboardViewModel.checkRequestLiveData) { checkStatusData ->
            if (checkStatusData.statusCode == "200") {
                val providerDetailsModel = checkStatusData.responseData.provider_details

                writePreferences(PROVIDER_ID, providerDetailsModel.id)

                val verificationModel = VerificationModel()
                verificationModel.isBankDetail = providerDetailsModel.is_bankdetail!!
                verificationModel.isDocument = providerDetailsModel.is_document!!
                verificationModel.isService = providerDetailsModel.is_service!!
                verificationModel.providerStatus = checkStatusData.responseData.provider_details.status
                verificationModel.providerWalletBalance = checkStatusData.responseData.provider_details.wallet_balance!!
                Constant.verificationObservable.value = verificationModel

                val verificationData = Constant.verificationObservable.value!!
                if (verificationData.isNeedToShowPendingDialog() && pendingListDialog != null
                        && !pendingListDialog!!.isShown()) showPendingListDialog()
                val onlineStatus = providerDetailsModel.is_online
                writePreferences(PreferencesKey.IS_ONLINE, onlineStatus)
                changeView(onlineStatus == 1)
                dashBoardNavigator.updateLocation(onlineStatus == 1)
            }
        }

        try {
            observeLiveData(mViewModel.onlineStatusLiveData) {
                loadingObservable.value = false
                if (mViewModel.onlineStatusLiveData.value != null) {
                    val isOnline = mViewModel.onlineStatusLiveData.value!!.responseData?.providerStatus
                    if (isOnline.equals("1")) {
                        writePreferences(PreferencesKey.IS_ONLINE, 1)
                        dashBoardNavigator.updateLocation(true)
                        changeView(true)
                    } else {
                        writePreferences(PreferencesKey.IS_ONLINE, 0)
                        dashBoardNavigator.updateLocation(false)
                        changeView(false)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        observeLiveData(mViewModel.mProfileResponse) {
            writePreferences(PreferencesKey.IS_ONLINE, it.profileData.is_online)
            updateOnlineStatus()
        }
    }

    private fun initializeMap() {
        fragmentMap = childFragmentManager.findFragmentById(R.id.map_home) as SupportMapFragment
        fragmentMap.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        try {

            try {
                mGoogleMap!!.isMyLocationEnabled = false
                mGoogleMap!!.uiSettings.isMyLocationButtonEnabled = false
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, com.gox.taxiservice.R.raw.style_json))

        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }

        Dexter.withActivity(activity!!)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        updateCurrentLocation()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }
                }).check()
    }

    fun updateMapLocation(location: LatLng, isAnimateMap: Boolean = false) {
        providerMarker?.remove()

        try {

            providerMarker = mGoogleMap?.addMarker(MarkerOptions().position(location).icon(BitmapDescriptorFactory.fromBitmap
            (bitmapFromVector(BaseApplication.getBaseApplicationContext, R.drawable.ic_marker_provider))))

        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (!isAnimateMap) mGoogleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
        else mGoogleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM))
    }

    private fun bitmapFromVector(context: Context, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onResume() {
        super.onResume()
        fragmentMap.onResume()
        mViewModel.getProfile()
    }

    override fun onPause() {
        super.onPause()
        fragmentMap.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        fragmentMap.onLowMemory()
    }

    @SuppressLint("MissingPermission")
    private fun updateCurrentLocation() {
        LocationUtils.getLastKnownLocation(activity!!, object : LocationCallBack {
            override fun onSuccess(location: Location) {
                updateMapLocation(LatLng(location.latitude, location.longitude))
            }

            override fun onFailure(message: String) {

            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dashBoardNavigator = context as DashBoardNavigator
        dashBoardNavigator.hideRightIcon(true)
        dashBoardNavigator.showLogo(true)
    }

    override fun changeStatus(view: View) {
        when (view.id) {
            R.id.btn_change_status -> {
                if (readPreferences<Int>(IS_ONLINE) == 1) {
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
        ViewUtils.showToast(activity!!, error, false)
    }

    private fun showPendingListDialog() {
        dashBoardNavigator.updateLocation(true)
        pendingListDialog?.show(activity!!.supportFragmentManager, "pendinglist")
        pendingListDialog?.isCancelable = false
    }

    private fun changeView(isOnline: Boolean) {
        if (!isOnline) {
            mBinding.llOffline.visibility = View.VISIBLE
            rlOnline.visibility = View.GONE
            mBinding.btnChangeStatus.text = activity!!.resources.getString(R.string.online)
        } else {
            mBinding.llOffline.visibility = View.GONE
            rlOnline.visibility = View.VISIBLE
            mBinding.btnChangeStatus.text = activity!!.resources.getString(R.string.offline)
        }
    }

    override fun showCurrentLocation() {
        Dexter.withActivity(activity!!)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        updateCurrentLocation()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest()
                    }
                }).check()
    }
}