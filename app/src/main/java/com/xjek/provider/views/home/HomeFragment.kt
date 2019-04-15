package com.xjek.provider.views.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.location.Location
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.xjek.base.base.BaseFragment
import com.xjek.base.data.PreferencesKey
import com.xjek.base.extensions.readPreferences
import com.xjek.base.utils.LocationCallBack
import com.xjek.base.utils.LocationUtils
import com.xjek.provider.R
import com.xjek.provider.databinding.FragmentHomePageBinding
import com.xjek.provider.views.dashboard.DashBoardNavigator
import permissions.dispatcher.NeedsPermission


class HomeFragment : BaseFragment<FragmentHomePageBinding>(), Home_Navigator, OnMapReadyCallback
        , GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraIdleListener, LocationSource.OnLocationChangedListener {



    private lateinit var mHomeDataBinding: com.xjek.provider.databinding.FragmentHomePageBinding
    override fun getLayoutId(): Int = R.layout.fragment_home_page
    private lateinit var dashBoardNavigator: DashBoardNavigator
    private lateinit var fragmentMap: SupportMapFragment
    private var mGoogleMap: GoogleMap? = null
    private var currentLat: Double = -33.8523341
    private var currentLong: Double = 151.2106085


    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mHomeDataBinding = mViewDataBinding as FragmentHomePageBinding
        val mHomeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java);
        mHomeViewModel.navigator=this
        mHomeDataBinding.homemodel = mHomeViewModel
        mHomeDataBinding.btnChangeStatus.bringToFront()
        updateCurrentLocation()
        initalizeMap()
       if(readPreferences<Int>(PreferencesKey.IS_ONLINE)==1){
           mHomeDataBinding.llOffline.visibility=View.GONE
           fragmentMap.view!!.visibility=View.VISIBLE

       }else{
           mHomeDataBinding.llOffline.visibility=View.VISIBLE
           fragmentMap.view!!.visibility=View.GONE
       }
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

    fun initalizeMap() {

        fragmentMap = childFragmentManager.findFragmentById(R.id.map_home) as SupportMapFragment
        fragmentMap.getMapAsync(this@HomeFragment)

    }

    override fun onMapReady(map: GoogleMap?) {
        mGoogleMap = map
        try {
            mGoogleMap!!.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, com.xjek.taxiservice.R.raw.style_json))
            val latlong = LatLng(currentLat, currentLong)
            val location = CameraUpdateFactory.newLatLngZoom(
                    latlong, 15f)
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
    }


    override fun gotoTaxiModule() {
    }

    override fun gotoFoodieModule() {
    }

    override fun gotoXuberModule() {

    }

    override fun changeStatus(view: View) {
        when(view.id){
            R.id.btn_change_status ->{
                if(mHomeDataBinding.btnChangeStatus.text.toString() == activity!!.resources.getString(R.string.offline)){
                    mHomeDataBinding.llOffline.visibility=View.VISIBLE
                    fragmentMap.view!!.visibility=View.GONE
                    mHomeDataBinding.btnChangeStatus.text = activity!!.resources.getString(R.string.online)
                }
                else{
                    mHomeDataBinding.llOffline.visibility=View.GONE
                    fragmentMap.view!!.visibility=View.VISIBLE
                    mHomeDataBinding.btnChangeStatus.text = activity!!.resources.getString(R.string.offline)

                }

            }
        }
    }


}
